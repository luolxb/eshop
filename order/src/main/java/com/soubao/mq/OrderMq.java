package com.soubao.mq;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.soubao.common.constant.OrderConstant;
import com.soubao.entity.*;
import com.soubao.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderMq {
    @Autowired
    private OrderActionService orderActionService;
    @Autowired
    private RebateLogService rebateLogService;

    @RabbitListener(queues = "create_order.order")
    public void createOrdersAfter(List<Order> orders) {
        log.info("创建订单队列消费:{}", orders);
        List<OrderAction> orderActions = new ArrayList<>();
        for (Order order : orders) {
            orderActions.add(orderActionService.getOrderActionByPlaceOrder(order));
            //如果是已支付的订单插入两条日志，参考php
            if (order.getPayStatus() == OrderConstant.PAYED) {
                orderActions.add(orderActionService.getOrderActionByPayOrder(order));
            }
        }
        orderActionService.saveBatch(orderActions);
        rebateLogService.createRebateLog(orders); //分销
        //todo
        //发微信模板消息
        //发短信
        //发站内信
    }

    @RabbitListener(queues = "pay_order.order")
    @RabbitHandler
    public void payOrders(List<Order> orders) {
        log.info("支付订单队列消费:{}", orders);
        List<OrderAction> orderActions = new ArrayList<>();
        List<Integer> paidOrderId = new ArrayList<>();
        for (Order order : orders) {
            if (order.getPayStatus() == OrderConstant.PAYED) {
                OrderAction payOrderAction = orderActionService.getOrderActionByPayOrder(order);
                orderActions.add(payOrderAction);
                paidOrderId.add(order.getOrderId());
            }
        }
        if(orderActions.size() > 0){
            orderActionService.saveBatch(orderActions);
        }
        if(paidOrderId.size() > 0){
            rebateLogService.update(new UpdateWrapper<RebateLog>().set("status", 1).in("order_id", paidOrderId));
        }
        //todo
        //发微信模板消息
        //发短信
        //发站内信
    }

    @RabbitListener(queues = "cancelled_order.order")
    public void cancelOrderAfter(Order order) {
        log.info("取消订单队列消费:{}", order);
        orderActionService.save(getOrderActionByCancelOrder(order));
        rebateLogService.update(new UpdateWrapper<RebateLog>().set("status", 4).set("remark", "订单取消，取消分成")
                .eq("order_id", order.getOrderId()));
    }

    @RabbitListener(queues = "cancelled_order.order")
    public void cancelOrderAfter(List<Order> orders) {
        List<OrderAction> orderActions = new ArrayList<>();
        Set<Integer> orderIds = new HashSet<>();
        for(Order order : orders){
            orderActions.add(getOrderActionByCancelOrder(order));
            orderIds.add(order.getOrderId());
        }
        log.info("取消订单列表队列消费:{}", orderIds);
        orderActionService.saveBatch(orderActions);
        rebateLogService.update(new UpdateWrapper<RebateLog>().set("status", 4).set("remark", "订单取消，取消分成")
                .in("order_id", orderIds));
    }

    @RabbitListener(queues = "receive_order.order")
    public void receiveOrderAfter(Order order) {
        log.info("用户确认收货订单队列消费:{}", order);
        orderActionService.save(orderActionService.getOrderActionByReceiveOrder(order));
        rebateLogService.update(new UpdateWrapper<RebateLog>().set("status", 2).set("confirm", System.currentTimeMillis() / 1000)
                .eq("order_id", order.getOrderId()).eq("status", 1));
    }

    @RabbitListener(queues = "receive_order.order")
    public void receiveOrderAfter(List<Order> orders) {
        log.info("自动确认收货订单队列消费:{}", orders);
        orders.forEach(order -> orderActionService.save(orderActionService.getOrderActionByReceiveOrder(order)));
        Set<Integer> orderIds = orders.stream().map(Order::getOrderId).collect(Collectors.toSet());
        rebateLogService.update(new UpdateWrapper<RebateLog>().set("status", 2).set("confirm", System.currentTimeMillis() / 1000)
                .in("order_id", orderIds).eq("status", 1));
    }

    private OrderAction getOrderActionByCancelOrder(Order order) {
        OrderAction orderAction = OrderAction.builder().orderId(order.getOrderId()).orderStatus(order.getOrderStatus())
                .payStatus(order.getPayStatus()).shippingStatus(order.getShippingStatus()).logTime(System.currentTimeMillis() / 1000)
                .userType(OrderConstant.CUSTOMER).storeId(order.getStoreId()).build();
        if (null != order.getSellerId()) {
            orderAction.setUserType(OrderConstant.SELLER);
            orderAction.setActionUser(order.getSellerId());
            if (order.getPromType() == 6) {
                orderAction.setActionNote("拼团退款");
                orderAction.setStatusDesc("商家取消订单");
            }
        } else {
            orderAction.setActionUser(order.getUserId());
            if (order.getPayStatus() == OrderConstant.PAYED) {
                orderAction.setActionNote(order.getUserNote());
                orderAction.setStatusDesc("用户取消订单");
            } else {
                orderAction.setActionNote("您取消了订单");
                orderAction.setStatusDesc("用户取消已付款订单");
            }
        }
        return orderAction;
    }

}
