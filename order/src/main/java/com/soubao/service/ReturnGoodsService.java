package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.OrderGoods;
import com.soubao.entity.ReturnGoods;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-20
 */
public interface ReturnGoodsService extends IService<ReturnGoods> {
    //添加退换货
    int addReturnGoods(OrderGoods orderGoods, ReturnGoods returnGoods);
    //根据订单id获取退款退货商品
    List<ReturnGoods> getReturnGoodsByOrderId(Integer orderId);
    //退款
    void refund(ReturnGoods returnGoods);

    void withUser(List<ReturnGoods> records);

    void withStore(List<ReturnGoods> records);

    void withOrderGoods(List<ReturnGoods> records);
}
