package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Coupon;
import com.soubao.entity.CouponList;
import com.soubao.entity.User;
import com.soubao.service.CouponListService;
import com.soubao.service.CouponService;
import com.soubao.service.UserService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Api(value="用户优惠券控制器",tags={"用户优惠券相关接口"})
@RestController
@RequestMapping("/user/coupon")
public class CouponListController {
    @Autowired
    private CouponListService couponListService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private UserService userService;

    @GetMapping
    public CouponList getOne(@RequestParam(value = "uid", required = false) Integer uid,
                         @RequestParam(value = "get_order_id", required = false) Integer getOrderId){
        QueryWrapper<CouponList> queryWrapper = new QueryWrapper<>();
        if(null != uid){
            queryWrapper.eq("uid", uid);
        }
        if(null != getOrderId){
            queryWrapper.eq("get_order_id", getOrderId);
        }
        return couponListService.getOne(queryWrapper);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping
    public SBApi remove(@RequestParam(value = "id") Integer id){
        CouponList couponList = couponListService.getById(id);
        couponService.update((new UpdateWrapper<Coupon>()).setSql("send_num = send_num - 1").eq("id", couponList.getCid()));
        couponListService.removeById(couponList.getId());
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/return")
    public SBApi returnByOrder(@RequestParam(value = "user_id") Integer userId,
                               @RequestParam(value = "order_id") Integer orderId){
        couponListService.update((new UpdateWrapper<CouponList>()).set("status", 0).set("use_time", 0).set("order_id", 0)
                .eq("status", 1).eq("uid", userId).eq("order_id", orderId));
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @ApiOperation("用户优惠券分页")
    @GetMapping("/page")
    public IPage<Coupon> userCoupons(@ApiParam("0:未使用 1：已使用  2：已过期") @RequestParam(value = "type", defaultValue = "0") Integer type,
                                     @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer pageIndex,
                                     @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "12") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        IPage<Coupon> userCouponListByType = couponService.getUserCouponListByType(new Page(pageIndex, size), user, type);
        couponService.withStore(userCouponListByType.getRecords());
        return userCouponListByType;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @ApiOperation("用户优惠券列表")
    @GetMapping("/list")
    public List<CouponList> userCoupons() {
        User user = authenticationFacade.getPrincipal(User.class);
        return couponListService.list(new QueryWrapper<CouponList>().eq("uid", user.getUserId()));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @ApiOperation("用户领取优惠券")
    @PostMapping
    public SBApi updateUserCoupon(@RequestBody CouponList couponList, SBApi sbApi) {
        User user = authenticationFacade.getPrincipal(User.class);
        couponService.addUserCoupon(user, couponList);
        return sbApi;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/list")
    public SBApi updateUserCoupon(@RequestParam(value = "id", required = false) Set<Integer> couponIds, //优惠券编号
                                  SBApi sbApi) {
        User user = authenticationFacade.getPrincipal(User.class);
        for(Integer couponId : couponIds){
            CouponList couponList = new CouponList();
            couponList.setCid(couponId);
            couponService.addUserCoupon(user, couponList);
        }
        return sbApi;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @ApiOperation("新人优惠券列表")
    @GetMapping("/new/list")
    public List<Coupon> getCouponsForNewUser() {
        User user = userService.getUserCurrent();
        return couponService.getUserNewCoupons(user);
    }

    @GetMapping("list/seller")
    public List<CouponList> getCouponList(@RequestParam(value = "cid", required = false) Integer cid) {
        QueryWrapper<CouponList> wrapper = new QueryWrapper<>();
        if (cid != null) {
            wrapper.eq("cid", cid);
        }
        return couponListService.list(wrapper);
    }
}
