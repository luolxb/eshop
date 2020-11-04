package com.soubao.service;

import com.soubao.dto.WxPayOrderDto;
import com.soubao.entity.Order;


import java.util.Map;

public interface WxPayService {
    /**
     * 微信支付统一下单
     * @param wxPayOrder
     * @return
     */
    Map<String, String> unifiedOrder(WxPayOrderDto wxPayOrder);


    /**
     * @Description: 订单支付异步通知
     * @param notifyStr: 微信异步通知消息字符串
     * @return
     */
    String notify(String notifyStr) throws Exception;

    /**
     * @Description: 退款
     * @param order: 订单
     * @param amount: 实际退款金额
     * @param refundReason: 退款原因
     * @return
     */
    void refund(Order order, double amount, String refundReason);
}
