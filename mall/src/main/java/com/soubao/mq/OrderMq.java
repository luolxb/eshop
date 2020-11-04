package com.soubao.mq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.soubao.common.constant.OrderConstant;
import com.soubao.entity.*;
import com.soubao.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class OrderMq {
    @Autowired
    private ConfigService configService;
    @Autowired
    private StockLogService stockLogService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private TeamGoodsItemService teamGoodsItemService;
    @Autowired
    private CouponListService couponListService;
    @Autowired
    private TeamFollowService teamFollowService;

    @RabbitListener(queues = "create_order.mall")
    public void createOrdersAfter(List<Order> orders) {
        log.info("创建订单队列消费:{}", orders.get(0).getOrderId());
    }

    @RabbitListener(queues = "pay_order.mall")
    public void payOrders(List<Order> orders) {
        log.info("支付订单队列消费:{}", orders);
        int reduce = Integer.parseInt((String) configService.getConfigMap().get("shopping_reduce"));
        for (Order order : orders) {
            if (order.getPromType() == 6) {
                teamGoodsItemService.log(order);
            }
            stockLogService.stock(order, false, reduce);
        }
    }

    @RabbitListener(queues = "cancelled_order.mall")
    public void cancelOrderAfter(Order order) {
        log.info("取消订单队列消费:{}", order);
        if (order.getCouponPrice().compareTo(BigDecimal.ZERO) > 0) {
            couponListService.update(CouponList.builder().useTime(Long.parseLong("0")).status(0).orderId(0).build(),
                    (new QueryWrapper<CouponList>()).eq("order_id", order.getOrderId()).eq("user_id", order.getUserId()));
        }
        //未付款的才会返回库存，已付款的要商家确认退款才会返回库存。还和下单减库存开关有关
        int reduce = Integer.parseInt((String) configService.getConfigMap().get("shopping_reduce"));
        if (reduce != 2 && order.getPayStatus() != OrderConstant.PAYED) {
            stockLogService.stock(order, true, reduce);
        }
        if(order.getPromType() == 6){
            //更新团员拼团失败
            teamFollowService.update((new UpdateWrapper<TeamFollow>()).set("status", 3).eq("order_id", order.getOrderId()));
        }
    }

    @RabbitListener(queues = "cancelled_order.mall")
    public void cancelOrderAfter(List<Order> orders) {
        Set<Integer> orderIds = new HashSet<>();
        Set<Integer> refundCouponOrderIds = new HashSet<>();
        Set<Integer> cancelledTeamOrderIds = new HashSet<>();
        for(Order order : orders){
            orderIds.add(order.getOrderId());
            if (order.getCouponPrice().compareTo(BigDecimal.ZERO) > 0) {
                refundCouponOrderIds.add(order.getOrderId());
            }
            if(order.getPromType() == 6){
                cancelledTeamOrderIds.add(order.getOrderId());
            }
        }
        log.info("取消订单列表队列消费:{}", orderIds);
        if(refundCouponOrderIds.size() > 0){
            couponListService.update(CouponList.builder().useTime(Long.parseLong("0")).status(0).orderId(0).build(),
                    (new QueryWrapper<CouponList>()).in("order_id", refundCouponOrderIds));
        }
        stockLogService.stock(orders, true);
        if(cancelledTeamOrderIds.size() > 0){
            teamFollowService.update((new UpdateWrapper<TeamFollow>()).set("status", 3).in("order_id", cancelledTeamOrderIds)); //更新团员拼团失败
        }
    }

    @RabbitListener(queues = "comment_order.mall")
    public void commentOrderGoodsAfter(OrderGoods orderGoods) {
        log.info("评价订单商品队列消费:{}", orderGoods);
        goodsService.update(new UpdateWrapper<Goods>().setSql("comment_count = comment_count + 1").eq("goods_id", orderGoods.getGoodsId()));
    }

    @RabbitListener(queues = "refund_order.mall")
    public void refundOrderAfter(Order order) {
        log.info("退款订单队列消费:{}", order);
        stockLogService.stork(order, true);
    }
}
