package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.CouponList;
import com.soubao.entity.Order;

import java.util.List;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-09-02
 */
public interface CouponListService extends IService<CouponList> {

    void deductionMasterOrder(Order preMasterOrder, Order masterOrder);

    IPage<CouponList> getCouponListPage(Page<CouponList> page, QueryWrapper<CouponList> queryWrapper);

    void withSource(List<CouponList> records);
}
