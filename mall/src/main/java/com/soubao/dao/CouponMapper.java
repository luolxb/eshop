package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Coupon;
import com.soubao.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 优惠券表 Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-08-23
 */
public interface CouponMapper extends BaseMapper<Coupon> {

    //根据优惠券状态查询用户优惠券列表,
    IPage<Coupon> selectUserCouponListByType(Page page, @Param("user") User user, @Param("type") Integer type);

    //查询优惠券列表
    IPage<Coupon> selectCouponPage(Page page, @Param("catId") Integer catId, @Param("type") Integer type);
}
