package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.soubao.common.constant.OrderConstant;
import com.soubao.entity.*;
import com.soubao.service.*;
import com.soubao.dao.StockLogMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-09-25
 */
@Slf4j
@Service("stockLogService")
public class StockLogServiceImpl extends ServiceImpl<StockLogMapper, StockLog> implements StockLogService {
    @Autowired
    private SpecGoodsPriceService specGoodsPriceService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private PreSellService preSellService;
    @Autowired
    private TeamActivityService teamActivityService;
    @Autowired
    private TeamGoodsItemService teamGoodsItemService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private ConfigService configService;

    @Override
    public void stock(List<Order> orders, Boolean increaseOrDecrease) {
        int reduce = Integer.parseInt((String) configService.getConfigMap().get("shopping_reduce"));

        log.info("orders  ==> {}",orders);
        for(Order order : orders){
            if ((reduce == 2 && order.getPayStatus() == OrderConstant.PAYED)
                    || (reduce != 2 && order.getPayStatus() != OrderConstant.PAYED)) {
                this.stork(order, increaseOrDecrease);
            }
        }
    }
    /**
     * @param order
     * @param increaseOrDecrease
     * @param reduce
     */
    @Override
    public void stock(Order order, Boolean increaseOrDecrease, int reduce) {
        if (!((reduce == 2 && order.getPayStatus() == OrderConstant.PAYED)
                || (reduce != 2 && order.getPayStatus() != OrderConstant.PAYED))) {
            return;
        }
        this.stork(order, increaseOrDecrease);
    }

    @Override
    public void stork(Order order, Boolean increaseOrDecrease) {
        Map<Integer, Goods> goodsMap = new HashMap<>();
        List<StockLog> stockLogList = new ArrayList<>();
        for (OrderGoods orderGoods : order.getOrderGoods()) {
            if(orderGoods.getPromType() == 4){
                preSellService.update((new UpdateWrapper<PreSell>()).eq("pre_sell_id", order.getPromId())
                        .setSql("deposit_order_num = deposit_order_num + 1,deposit_goods_num = deposit_goods_num + " + orderGoods.getGoodsNum()));
            }
            if(orderGoods.getPromType() == 6){
                TeamActivity teamActivity = teamActivityService.getById(order.getPromId());
                if(null != teamActivity){
                    if(teamActivity.getTeamType() == 2 && !increaseOrDecrease){
                        //抽奖的时候再减库存
                        return;
                    }
                    teamActivity.setSalesSum(teamActivity.getSalesSum() + order.getGoodsNum());
                    teamActivityService.updateById(teamActivity);
                    long itemId = 0;
                    if(!StringUtils.isEmpty(orderGoods.getSpecKey())){
                        SpecGoodsPrice specGoodsPrice = specGoodsPriceService.getOne((new QueryWrapper<SpecGoodsPrice>()).select("item_id")
                                .eq("goods_id", orderGoods.getGoodsId()).eq("`key`", orderGoods.getSpecKey()));
                        itemId = specGoodsPrice.getItemId();
                    }
                    teamGoodsItemService.update((new UpdateWrapper<TeamGoodsItem>()).setSql("sales_sum = sales_sum + " + orderGoods.getGoodsNum())
                            .eq("team_id", teamActivity.getTeamId()).eq("item_id", itemId));
                }
            }

            StockLog stockLog = new StockLog();
            stockLog.setCtime(System.currentTimeMillis() / 1000);
            stockLog.setStock(orderGoods.getGoodsNum());
            stockLog.setMuid(order.getUserId());
            stockLog.setGoodsId(orderGoods.getGoodsId());
            stockLog.setGoodsName(orderGoods.getGoodsName());
            stockLog.setGoodsSpec(orderGoods.getSpecKeyName());
            stockLog.setStoreId(order.getStoreId());
            stockLog.setOrderSn(order.getOrderSn());
            stockLogList.add(stockLog);
            if (!StringUtils.isEmpty(orderGoods.getSpecKey())) {
                int storeCount = (increaseOrDecrease) ? orderGoods.getGoodsNum() : - orderGoods.getGoodsNum();
                specGoodsPriceService.update(new UpdateWrapper<SpecGoodsPrice>().setSql("store_count = store_count + " + storeCount)
                        .eq("goods_id", orderGoods.getGoodsId()).eq("`key`", orderGoods.getSpecKey()));
            }
            if (goodsMap.containsKey(orderGoods.getGoodsId())) {
                Goods goods = goodsMap.get(orderGoods.getGoodsId());
                goods.setStoreCount((increaseOrDecrease) ? (goods.getStoreCount() + orderGoods.getGoodsNum())
                        : -(goods.getStoreCount() + orderGoods.getGoodsNum()));
            } else {
                Goods goods = new Goods();
                goods.setGoodsId(orderGoods.getGoodsId());
                goods.setStoreCount((increaseOrDecrease) ? orderGoods.getGoodsNum() : -orderGoods.getGoodsNum());
                goodsMap.put(orderGoods.getGoodsId(), goods);
            }
        }
        goodsMap.forEach((goodId, goods) -> goodsService.stock(goods));
        this.saveBatch(stockLogList);
    }

    @Override
    public void withStore(List<StockLog> records) {
        Set<Integer> storeIds = records.stream().map(StockLog::getStoreId).collect(Collectors.toSet());
        Map<Integer, Store> storeMap = sellerService.getStoreListByIds(storeIds).stream().collect(Collectors.toMap(Store::getStoreId, store -> store));
        records.forEach(stockLog -> {
            stockLog.setStoreName(storeMap.get(stockLog.getStoreId()).getStoreName());
        });
    }
}
