package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Order;
import com.soubao.entity.StockLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-09-25
 */
public interface StockLogService extends IService<StockLog> {

    void stock(Order order, Boolean increaseOrDecrease, int reduce);

    void stork(Order order, Boolean increaseOrDecrease);

    void stock(List<Order> orderList, Boolean increaseOrDecrease);

    void withStore(List<StockLog> records);
}
