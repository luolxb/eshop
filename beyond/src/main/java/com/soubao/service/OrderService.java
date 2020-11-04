package com.soubao.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.*;



public interface OrderService extends IService<Order> {
    /**
     * 支付后获取订单支付状态
     *
     * @return
     */
    Integer getPayStatusByPay(Order order);
}
