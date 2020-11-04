package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Order;
import com.soubao.entity.OrderStatis;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商家订单结算表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-12-19
 */
public interface OrderStatisService extends IService<OrderStatis> {
    void withStore(List<OrderStatis> records);

    void createOrderSettlement(Map<Object, Object> config, List<Order> orders);
}
