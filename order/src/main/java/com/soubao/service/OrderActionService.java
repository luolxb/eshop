package com.soubao.service;

import com.soubao.entity.Order;
import com.soubao.entity.OrderAction;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyr
 * @since 2019-09-25
 */
public interface OrderActionService extends IService<OrderAction> {
    OrderAction getOrderActionByPlaceOrder(Order order);
    OrderAction getOrderActionByPayOrder(Order order);

    OrderAction getOrderActionByReceiveOrder(Order order);

    //添加操作记录
    boolean addOrderActionLog(Order order, String statusDesc, String actionNote, Integer actionUser, Integer userType, Integer storeId);

    void withUser(List<OrderAction> records);
}
