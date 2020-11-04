package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.*;
import com.soubao.dao.TeamActivityMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 拼团活动表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-10-30
 */
@Slf4j
@Service("teamActivityService")
public class TeamActivityServiceImpl extends ServiceImpl<TeamActivityMapper, TeamActivity> implements TeamActivityService {
    @Autowired
    private TeamActivityMapper teamActivityMapper;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SpecGoodsPriceService specGoodsPriceService;
    @Autowired
    private TeamFoundService teamFoundService;
    @Autowired
    private TeamFollowService teamFollowService;
    @Autowired
    private TeamGoodsItemService teamGoodsItemService;
    @Autowired
    private TeamLotteryService teamLotteryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private StockLogServiceImpl stockLogServiceImpl;
    @Autowired
    private SellerService sellerService;

    @Override
    public IPage<TeamActivity> getGoodsPage(Page page, TeamActivity teamActivity) {
        return teamActivityMapper.selectGoodsPage(page, teamActivity);
    }

    @Override
    public IPage<TeamActivity> selectPageWithStore(Page page, TeamActivity teamActivityQuery) {
        return teamActivityMapper.selectPageWithStore(page, teamActivityQuery);
    }

    @Override
    public void restoreGoodsSku(TeamActivity teamActivity) {
        specGoodsPriceService.update((new UpdateWrapper<SpecGoodsPrice>()).set("prom_type", 0).set("prom_id", 0)
                .eq("prom_type", 6).eq("prom_id", teamActivity.getTeamId()));
        int noPromSpecCount = specGoodsPriceService.count((new QueryWrapper<SpecGoodsPrice>())
                .eq("goods_id", teamActivity.getGoodsId()).ne("prom_type", 0));
        if(noPromSpecCount == 0){
            //为零表示两种情况，1，商品无规格。2，商品没有规格参与活动。
            goodsService.update((new UpdateWrapper<Goods>()).set("prom_type", 0).set("prom_id", 0)
                    .eq("prom_type", 0).eq("goods_id", teamActivity.getGoodsId()));
        }
    }

    @Override
    public void withTeamActivity(List<Order> records) {
        Set<Integer> teamActivityIds = records.stream().map(Order::getPromId).collect(Collectors.toSet());
        if(teamActivityIds.size() > 0){
            List<TeamActivity> teamActivityList = this.list((new QueryWrapper<TeamActivity>()).in("team_id", teamActivityIds));
            Map<Integer, TeamActivity> teamActivityMap = teamActivityList.stream().collect(Collectors.toMap(TeamActivity::getTeamId, team -> team));
            for(Order order : records){
                order.setTeamActivity(teamActivityMap.get(order.getPromId()));
            }
        }
    }

    @Override
    public void lottery(Integer sellerId, TeamActivity teamActivity) {
        if(null == teamActivity){
            throw new ShopException(ResultEnum.NO_FIND_TEAM);
        }
        if(teamActivity.getStatus() != 1){
            throw new ShopException(ResultEnum.TEAM_STATUS_NO_WAY);
        }
        if(teamActivity.getTeamType() != 2){
            throw new ShopException(ResultEnum.TEAM_TYPE_NO_WAY);
        }
        if(teamActivity.getIsLottery() == 1){
            throw new ShopException(ResultEnum.TEAM_HAD_LOTTERY);
        }
        //先找到成团的团长们
        List<TeamFound> teamFounds = teamFoundService.list((new QueryWrapper<TeamFound>()).eq("status", 2)
                .eq("team_id", teamActivity.getTeamId()));
        if(teamFounds.size() == 0){
            throw new ShopException(ResultEnum.NO_FIND_SUCCESS_FOUND);
        }
        Set<Integer> teamFoundIds =  new HashSet<>();
        List<TeamLottery> waitTeamLotteries = new ArrayList<>();//可抽奖人员列表
        for(TeamFound teamFound : teamFounds){
            teamFoundIds.add(teamFound.getFoundId());
            TeamLottery teamLottery = new TeamLottery();
            teamLottery.setUserId(teamFound.getUserId());
            teamLottery.setOrderId(teamFound.getOrderId());
            teamLottery.setOrderSn(teamFound.getOrderSn());
            teamLottery.setTeamId(teamFound.getTeamId());
            teamLottery.setNickname(teamFound.getNickname());
            teamLottery.setHeadPic(teamFound.getHeadPic());
            waitTeamLotteries.add(teamLottery);
        }
        //然后找到团长们的团员们
        List<TeamFollow> teamFollows = teamFollowService.list((new QueryWrapper<TeamFollow>()).eq("status", 2)
                .in("found_id", teamFoundIds));
        for(TeamFollow teamFollow : teamFollows){
            TeamLottery teamLottery = new TeamLottery();
            teamLottery.setUserId(teamFollow.getFollowUserId());
            teamLottery.setOrderId(teamFollow.getOrderId());
            teamLottery.setOrderSn(teamFollow.getOrderSn());
            teamLottery.setTeamId(teamFollow.getTeamId());
            teamLottery.setNickname(teamFollow.getFollowUserNickname());
            teamLottery.setHeadPic(teamFollow.getFollowUserHeadPic());
            waitTeamLotteries.add(teamLottery);
        }
        Collections.shuffle(waitTeamLotteries);//打乱抽奖数组
        List<TeamLottery> teamLotteries = new ArrayList<>();//中奖人员
        if(waitTeamLotteries.size() > teamActivity.getStockLimit()){
            for (int i = 0; i < teamActivity.getStockLimit(); i++) {
                teamLotteries.add(waitTeamLotteries.get(0));
                waitTeamLotteries.remove(0);
            }
        }else{
            teamLotteries = waitTeamLotteries;
        }
        Set<Integer> lotteryOrderIds = teamLotteries.stream().map(TeamLottery::getOrderId).collect(Collectors.toSet());
        List<Order> orderList = orderService.getSellerOrderListByIds(lotteryOrderIds);
        Map<Integer, Order> orderMap = orderList.stream().collect(Collectors.toMap(Order::getOrderId, order -> order));
        for(TeamLottery teamLottery : teamLotteries){
            teamLottery.setMobile(orderMap.get(teamLottery.getOrderId()).getMobile());
        }
        teamLotteryService.saveBatch(teamLotteries);
        for (TeamLottery teamLottery : teamLotteries) {
            Map<String, Object> requestOrderAction = new HashMap<>();
            requestOrderAction.put("order_id", teamLottery.getOrderId());
            requestOrderAction.put("store_id", teamActivity.getStoreId());
            requestOrderAction.put("action_user", sellerId);
            requestOrderAction.put("action_note", "抽奖团确认订单");
            String orderConfirmResult = orderService.confirm(requestOrderAction);
            log.info("抽奖团确认订单：{}", orderConfirmResult);
        }
        Set<Integer> orderId = waitTeamLotteries.stream().map(TeamLottery::getOrderId).collect(Collectors.toSet());
        orderService.cancelTeamOrder(orderId);
        //减库存的操作
        List<OrderGoods> orderGoodsList = orderService.getOrderGoodsListByOrderIds(lotteryOrderIds);
        List<StockLog> stockLogList = new ArrayList<>();
        for (OrderGoods orderGoods : orderGoodsList) {
            Order order = orderMap.get(orderGoods.getOrderId());
            List<OrderGoods> decreaseOrderGoods = new ArrayList<>();
            decreaseOrderGoods.add(orderGoods);
            order.setOrderGoods(decreaseOrderGoods);
            stockLogServiceImpl.stork(order, false);
        }
        stockLogServiceImpl.saveBatch(stockLogList);
        //恢复普通商品，商品规格
        this.restoreGoodsSku(teamActivity);
        teamGoodsItemService.update((new UpdateWrapper<TeamGoodsItem>()).set("deleted", 1).eq("team_id", teamActivity.getTeamId()));
        this.update((new UpdateWrapper<TeamActivity>()).set("is_lottery", 1).eq("team_id", teamActivity.getTeamId()));//抽奖结束
    }

    @Override
    public void withStore(List<TeamActivity> records) {
        Set<Integer> storeIds = records.stream().map(TeamActivity::getStoreId).collect(Collectors.toSet());
        Map<Integer, Store> storeMap = sellerService.getStoreListByIds(storeIds).stream().collect(Collectors.toMap(Store::getStoreId, store -> store));
        records.forEach(teamActivity -> {
            Store store = new Store();
            store.setStoreName(storeMap.get(teamActivity.getStoreId()).getStoreName());
            teamActivity.setStore(store);
        });
    }

}
