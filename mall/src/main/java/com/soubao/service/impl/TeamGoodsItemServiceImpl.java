package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.*;
import com.soubao.dao.TeamGoodsItemMapper;
import com.soubao.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-10-30
 */
@Service("teamGoodsItemService")
@Slf4j
public class TeamGoodsItemServiceImpl extends ServiceImpl<TeamGoodsItemMapper, TeamGoodsItem> implements TeamGoodsItemService {
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private TeamFollowService teamFollowService;
    @Autowired
    private TeamFoundService teamFoundService;
    @Autowired
    private TeamActivityService teamActivityService;
    @Autowired
    private SpecGoodsPriceService specGoodsPriceService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Order getMasterOrder(User user, GoodsSku goodsSku, Integer goodsNum) {
        Order masterOrder = new Order();
        if (goodsSku.getStoreCount() <= 0 || goodsNum > goodsSku.getStoreCount()) {
            throw new ShopException(ResultEnum.NOT_ENOUGH_STOCK);
        }
        if (goodsSku.getLimitNum() > 0 && goodsNum > goodsSku.getLimitNum()) {
            throw new ShopException(ResultEnum.LIMIT_NUM_ERROR);
        }
        OrderGoods orderGoods = cartService.changeGoodsSkuToOrderGoods(goodsSku, goodsNum);
        GoodsCategory goodsCategory = goodsCategoryService.getById(goodsSku.getCatId3());
        orderGoods.setCommission(goodsCategory.getCommission());
        List<Order> orderList = new ArrayList<>();
        Order storeOrder = cartService.getOrderByOrderGoods(orderGoods);
        storeOrder.setUserId(user.getUserId());
        storeOrder.setAddTime(System.currentTimeMillis() / 1000);
        orderList.add(storeOrder);
        masterOrder.setOrderList(orderList);
        cartService.calculateMasterOrder(masterOrder);
        return masterOrder;
    }

    @Override
    public Order getMasterOrderByTeamOrder(User user, Order teamOrder, Order requestOrder) {
        cartService.checkOrderShoppingConfig(teamOrder);
        OrderGoods orderGoods = teamOrder.getOrderGoods().get(0);
        UserAddress userAddress = userService.getUserAddress(requestOrder.getAddressId());//用户地址
        teamOrderSetUserAddress(teamOrder, userAddress);
        Goods goods = goodsService.getById(orderGoods.getGoodsId());
        orderGoods.setIsDelivery((goodsService.isNotShippingInRegion(goods, userAddress.getDistrict())) ? 1 : 0);
        if (null != requestOrder.getGoodsNum() && !requestOrder.getGoodsNum().equals(teamOrder.getGoodsNum())) {
            orderGoods.setGoodsNum(requestOrder.getGoodsNum());
            orderGoods.setGoodsFee(orderGoods.getMemberGoodsPrice().multiply(BigDecimal.valueOf(orderGoods.getGoodsNum())));
            teamOrder.setGoodsPrice(orderGoods.getGoodsFee());
        }
        teamOrder.setShippingPrice(goodsService.getFreightPriceInRegion(goods, userAddress.getDistrict(), orderGoods.getGoodsNum()));
        teamOrder.setTotalAmount(teamOrder.getGoodsPrice().add(teamOrder.getShippingPrice()));
        teamOrder.setOrderAmount(teamOrder.getTotalAmount());
        List<Order> orderList = new ArrayList<>();
        orderList.add(teamOrder);
        Order masterOrder = new Order();
        masterOrder.setMasterOrderSn(teamOrder.getMasterOrderSn());
        masterOrder.setOrderList(orderList);
        cartService.calculateMasterOrder(masterOrder);
        masterOrder.setIntegral(requestOrder.getIntegral());
        cartService.useIntegral(user, masterOrder);
        return masterOrder;
    }

    private void teamOrderSetUserAddress(Order teamOrder, UserAddress userAddress) {
        teamOrder.setConsignee(userAddress.getConsignee());
        teamOrder.setProvince(userAddress.getProvince());
        teamOrder.setCity(userAddress.getCity());
        teamOrder.setDistrict(userAddress.getDistrict());
        teamOrder.setTwon(userAddress.getTwon());
        teamOrder.setAddress(userAddress.getAddress());
        teamOrder.setMobile(userAddress.getMobile());
        teamOrder.setZipcode(userAddress.getZipcode());
    }

    @Override
    public void checkPayOrder(Order teamOrder, Order requestOrder) {
        if (!teamOrder.getIsAblePay()) {
            throw new ShopException(ResultEnum.ORDER_PAY_ERROR);
        }
        if (null != requestOrder.getGoodsNum() && !requestOrder.getGoodsNum().equals(teamOrder.getGoodsNum())) {
            TeamActivity teamActivity = teamActivityService.getById(teamOrder.getPromId());
            if (teamActivity.getBuyLimit() > 0 && requestOrder.getGoodsNum() > teamActivity.getBuyLimit()) {
                throw new ShopException(ResultEnum.LIMIT_NUM_ERROR);
            }
        }
    }

    @Override
    public void log(Order order) {
        TeamActivity teamActivity = teamActivityService.getById(order.getPromId());
        TeamFound teamFound = teamFoundService.getOne((new QueryWrapper<TeamFound>().eq("order_id", order.getOrderId())));
        if (teamFound != null) {
            //团长的单
            teamFound.setFoundTime(System.currentTimeMillis() / 1000);
            teamFound.setFoundEndTime(Math.toIntExact(teamFound.getFoundTime() + teamActivity.getTimeLimit()));
            teamFound.setStatus(1);
            teamFoundService.updateById(teamFound);
            teamFound.setTimeLimit(teamActivity.getTimeLimit());//为了定时查询里的判断
            redisUtil.lSet("team_found", teamFound);//加入正在拼团拼主缓存
        } else {
            //团员的单
            TeamFollow teamFollow = teamFollowService.getOne((new QueryWrapper<TeamFollow>().eq("order_id", order.getOrderId())));
            if (teamFollow != null) {
                redisUtil.lRemove("team_follow", 1, teamFollow);//移除未支付团员缓存
                teamFollow.setStatus(1);
                teamFollowService.updateById(teamFollow);
                TeamFound followTeamFound = teamFoundService.getById(teamFollow.getFoundId());
                int teamFollowCount = teamFollowService.count((new QueryWrapper<TeamFollow>()).select("follow_id")
                        .eq("found_id", teamFollow.getFoundId()).eq("status", 1));
                //加上团长
                if ((teamFollowCount + 1) >= followTeamFound.getNeed()) {
                    followTeamFound.setTimeLimit(teamActivity.getTimeLimit());
                    redisUtil.lRemove("team_found", 1, followTeamFound);//移除正在拼团拼主缓存
                    followTeamFound.setStatus(2);//团长成团成功
                    //更新团员成团成功
                    teamFollowService.update((new UpdateWrapper<TeamFollow>()).set("status", 2)
                            .eq("found_id", followTeamFound.getFoundId()).eq("status", 1));
                }
                followTeamFound.setJoin(followTeamFound.getJoin() + 1);
                teamFoundService.updateById(followTeamFound);
                orderService.teamOrderSuccess(followTeamFound.getOrderId());
            }
        }
    }

    @Override
    public void withGoodsSku(List<TeamGoodsItem> teamGoodsItems) {
        if (teamGoodsItems.size() > 0) {
            if (teamGoodsItems.get(0).getItemId() == 0) {
                TeamGoodsItem teamGoodsItem = teamGoodsItems.get(0);
                Goods goods = goodsService.getById(teamGoodsItem.getGoodsId());
                teamGoodsItem.setKeyName("--");
                teamGoodsItem.setStoreCount(goods.getStoreCount());
                teamGoodsItem.setShopPrice(goods.getShopPrice());
            } else {
                Set<Integer> itemIds = teamGoodsItems.stream().map(TeamGoodsItem::getItemId).collect(Collectors.toSet());
                List<SpecGoodsPrice> specGoodsPriceList = specGoodsPriceService.list((new QueryWrapper<SpecGoodsPrice>()).in("item_id", itemIds));
                Map<Long, SpecGoodsPrice> specGoodsPriceMap = specGoodsPriceList.stream().collect(Collectors.toMap(SpecGoodsPrice::getItemId, spec -> spec));
                for (TeamGoodsItem teamGoodsItem : teamGoodsItems) {
                    SpecGoodsPrice specGoodsPrice = specGoodsPriceMap.get(teamGoodsItem.getItemId().longValue());
                    teamGoodsItem.setKeyName(specGoodsPrice.getKeyName());
                    teamGoodsItem.setStoreCount(specGoodsPrice.getStoreCount());
                    teamGoodsItem.setShopPrice(specGoodsPrice.getPrice());
                }
            }
        }
    }

    @Override
    public void saveByTeam(List<TeamGoodsItem> teamGoodsItemList, TeamActivity teamActivity) {
        for (TeamGoodsItem teamGoodsItem : teamGoodsItemList) {
            teamGoodsItem.setTeamId(teamActivity.getTeamId());
        }
        saveBatch(teamGoodsItemList);
        if (0 == teamGoodsItemList.get(0).getItemId()) {
            goodsService.update((new UpdateWrapper<Goods>())
                    .set("prom_id", teamActivity.getTeamId()).set("prom_type", 6).eq("goods_id", teamActivity.getGoodsId()));
        } else {
            goodsService.update((new UpdateWrapper<Goods>()).set("prom_type", 6).eq("goods_id", teamActivity.getGoodsId()));
            Set<Integer> itemIds = teamGoodsItemList.stream().map(TeamGoodsItem::getItemId).collect(Collectors.toSet());
            specGoodsPriceService.update((new UpdateWrapper<SpecGoodsPrice>())
                    .set("prom_id", teamActivity.getTeamId()).set("prom_type", 6).in("item_id", itemIds));
        }
    }

}
