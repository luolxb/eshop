package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.OrderStatisMapper;
import com.soubao.entity.*;
import com.soubao.service.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 商家订单结算表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-12-19
 */
@Service
public class OrderStatisServiceImpl extends ServiceImpl<OrderStatisMapper, OrderStatis> implements OrderStatisService {
    @Autowired
    private SellerService sellerService;
    @Autowired
    private ReturnGoodsService returnGoodsService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public void withStore(List<OrderStatis> records) {
        if (records.size() > 0) {
            Set<Integer> storeIds = records.stream().map(OrderStatis::getStoreId).collect(Collectors.toSet());
            Map<Integer, Store> storeMap = sellerService.getStoresById(storeIds).stream().collect(Collectors.toMap(Store::getStoreId, store -> store));
            records.forEach(record -> {
                if (storeMap.containsKey(record.getStoreId())) {
                    record.setStoreName(storeMap.get(record.getStoreId()).getStoreName());
                }
            });
        }
    }

    @Override
    public void createOrderSettlement(Map<Object, Object> config, List<Order> orders) {
        if (orders.size() == 0) {
            return;
        }
        Set<Integer> orderIds = orders.stream().map(Order::getOrderId).collect(Collectors.toSet());
        Set<Integer> cutOrderIds = returnGoodsService.list(new QueryWrapper<ReturnGoods>()
                .in("order_id", orderIds).notIn("status", -2, 5)).stream().map(ReturnGoods::getOrderId).collect(Collectors.toSet());
        orderIds.removeAll(cutOrderIds);
        orders.removeIf(tempOrder -> cutOrderIds.contains(tempOrder.getOrderId()));
        if (orders.size() == 0) {
            return;
        }
        Map<Integer, List<OrderGoods>> orderGoodsMap = orderGoodsService.list(new QueryWrapper<OrderGoods>().in("order_id", orderIds))
                .stream().collect(Collectors.groupingBy(OrderGoods::getOrderId));
        String pointRate = (String) config.getOrDefault("shopping_point_rate", "1");
        MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
        Set<Integer> returnGoodsRecIds = new HashSet<>();
        Map<Integer, OrderStatis> orderStatisMap = new HashMap<>();
        String date = (String) config.getOrDefault("shopping_auto_service_date", "7");
        for (Order order : orders) {
            List<OrderGoods> orderGoodsList = orderGoodsMap.getOrDefault(order.getOrderId(), new ArrayList<>());
            if (orderGoodsList.size() == 0) {
                continue;
            }
            OrderStatis orderStatis;
            if (!orderStatisMap.containsKey(order.getStoreId())) {
                OrderStatis temOrderStatis = new OrderStatis();
                temOrderStatis.setStartDate(orders.get(0).getConfirmTime());
                temOrderStatis.setEndDate(System.currentTimeMillis() / 1000 - TimeUnit.DAYS.toSeconds(Long.parseLong(date)));
                temOrderStatis.setCreateDate(System.currentTimeMillis() / 1000);
                temOrderStatis.setStoreId(order.getStoreId());
                temOrderStatis.setOrderTotals(BigDecimal.ZERO);//订单商品金额
                temOrderStatis.setShippingTotals(BigDecimal.ZERO);//运费
//                temOrderStatis.setReturnIntegral(0);//退还积分
                temOrderStatis.setCommisTotals(BigDecimal.ZERO);// 平台抽成
                temOrderStatis.setGiveIntegral(BigDecimal.ZERO);// 送出积分金额
                temOrderStatis.setResultTotals(BigDecimal.ZERO);// 本期应结
                temOrderStatis.setOrderPromAmount(BigDecimal.ZERO);// 优惠价
                temOrderStatis.setCouponPrice(BigDecimal.ZERO);// 优惠券抵扣
                temOrderStatis.setDistribut(BigDecimal.ZERO);// 分销金额
                temOrderStatis.setIntegral(0);// 订单使用积分
                temOrderStatis.setReturnTotals(BigDecimal.ZERO);// 若订单商品退款，退还金额
                temOrderStatis.setPayMoney(BigDecimal.ZERO);// 实付款
                temOrderStatis.setDiscount(BigDecimal.ZERO);
                orderStatisMap.put(order.getStoreId(), temOrderStatis);
            }
            orderStatis = orderStatisMap.get(order.getStoreId());
            BigDecimal orderAmount = BigDecimal.ZERO;
            orderStatis.setShippingTotals(orderStatis.getShippingTotals().add(order.getShippingPrice()));
            orderStatis.setOrderPromAmount(orderStatis.getOrderPromAmount().add(order.getOrderAmount()));
            orderStatis.setCouponPrice(orderStatis.getCouponPrice().add(order.getCouponPrice()));
            orderStatis.setIntegral(orderStatis.getIntegral() + order.getIntegral());
            orderStatis.setDiscount(orderStatis.getDiscount().add(order.getDiscount()));
            for (OrderGoods orderGoods : orderGoodsList) {
                BigDecimal orderGoodsAmount = orderGoods.getMemberGoodsPrice().multiply(BigDecimal.valueOf(orderGoods.getGoodsNum()));
                orderAmount = orderAmount.add(orderGoodsAmount);
            }
            orderStatis.setOrderTotals(orderStatis.getOrderTotals().add(orderAmount));
            orderStatis.setPayMoney(orderStatis.getPayMoney().add(orderAmount.subtract(order.getOrderPromAmount().add(order.getCouponPrice()))));

            for (OrderGoods orderGoods : orderGoodsList) {
                BigDecimal orderGoodsDistributTotal = orderGoods.getDistribut().multiply(BigDecimal.valueOf(orderGoods.getGoodsNum()));
                orderStatis.setDistribut(orderStatis.getDistribut().add(orderGoodsDistributTotal));
                BigDecimal goodsAmount = orderGoods.getMemberGoodsPrice().multiply(BigDecimal.valueOf(orderGoods.getGoodsNum()));//此商品该结算金额初始值
                if (orderGoods.getIsSend() == 3) {
                    returnGoodsRecIds.add(orderGoods.getRecId());
                } else {
                    BigDecimal giveIntegralMoney = BigDecimal.valueOf(orderGoods.getGoodsNum())
                            .multiply(BigDecimal.valueOf(orderGoods.getGiveIntegral())).divide((new BigDecimal(pointRate)), mc); //商品赠送的积分金额
                    BigDecimal settlement = goodsAmount;
                    orderStatis.setGiveIntegral(giveIntegralMoney);//订单赠送积分金额
                    if (orderGoods.getIsSend() < 3) {
                        //减去购买该商品赠送的积分金额
                        settlement = settlement.subtract(giveIntegralMoney);
                    }
                    //去掉商品优惠的价格
                    settlement = settlement.subtract(goodsAmount.divide(orderAmount, mc).multiply(order.getCouponPrice().add(order.getOrderPromAmount())));
                    BigDecimal orderGoodSettlements = settlement
                            .multiply(BigDecimal.valueOf(orderGoods.getCommission()))
                            .divide(new BigDecimal("100"), mc);
                    orderStatis.setCommisTotals(orderStatis.getCommisTotals().add(orderGoodSettlements));
                    orderStatis.setResultTotals(orderStatis.getResultTotals().add(settlement.subtract(orderGoodSettlements))
                            .subtract(orderGoodsDistributTotal));
                }
            }
        }
        if (returnGoodsRecIds.size() > 0) {
            List<ReturnGoods> returnGoodsList = returnGoodsService.list(new QueryWrapper<ReturnGoods>()
                    .select("store_id,sum(refund_deposit + refund_money) as refund_money,sum(refund_integral) as refund_integral")
                    .in("rec_id", returnGoodsRecIds).groupBy("store_id"));
            for (ReturnGoods returnGoods : returnGoodsList) {
                if (orderStatisMap.containsKey(returnGoods.getStoreId())) {
                    OrderStatis orderStatis = orderStatisMap.get(returnGoods.getStoreId());
                    orderStatis.setReturnTotals(returnGoods.getRefundMoney());
                    orderStatis.setIntegral(orderStatis.getIntegral() - returnGoods.getRefundIntegral());//订单使用积分
                }
            }
        }
        if(orderStatisMap.size() > 0){
            saveBatch(orderStatisMap.values());
            for (Order order : orders){
                if (orderStatisMap.containsKey(order.getStoreId())) {
                    order.setOrderStatisId(orderStatisMap.get(order.getStoreId()).getId());
                }
            }
            orderService.updateBatchById(orders);
            List<OrderStatis> orderStatisList = new ArrayList<>(orderStatisMap.values());
            rabbitTemplate.convertAndSend("settle_order", "", orderStatisList);
        }
    }
}
