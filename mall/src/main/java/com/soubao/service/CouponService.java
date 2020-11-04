package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Coupon;
import com.soubao.entity.CouponList;
import com.soubao.entity.Order;
import com.soubao.entity.User;

import java.util.List;

/**
 * <p>
 * 优惠券表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-23
 */
public interface CouponService extends IService<Coupon> {
    //根据优惠券状态获取用户优惠券列表
    IPage<Coupon> getUserCouponListByType(Page page, User user, Integer type);

    //获取优惠券分页
    IPage<Coupon> selectCouponPage(Page page, Integer catId, Integer type);

    //优惠券领取/兑换
    void addUserCoupon(User user, CouponList couponList);

    //计算使用优惠券抵扣的额度
    void calculateCouponAmount(User user, Order masterOrder);

    /**
     * 获取用户未领取的新人优惠券
     * @param user
     * @return
     */
    List<Coupon> getUserNewCoupons(User user);

    Coupon getCoupon(Integer couponId);

    void updateCoupon(Coupon coupon);

//    IPage<Coupon> getCouponPage(Page<Coupon> page, QueryWrapper<Coupon> queryWrapper);

    void deleteCoupon(Integer id);

    void addCoupon(Coupon coupon);

    void withStore(List<Coupon> records);
}
