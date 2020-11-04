package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Coupon;
import com.soubao.entity.CouponList;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.CouponListService;
import com.soubao.service.CouponService;
import com.soubao.validation.group.coupon.NewcomerCoupon;
import com.soubao.validation.group.coupon.StoreCoupon;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Api(value = "优惠券控制器", tags = {"优惠券相关接口"})
@RestController
@RequestMapping("coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponListService couponListService;

    @ApiOperation(value = "优惠券列表接口", notes = "获取优惠券列表接口", httpMethod = "GET")
    @GetMapping("page")
    public IPage<Coupon> coupons(@ApiParam("商品分类三级id") @RequestParam(value = "cat_id", defaultValue = "0") Integer catId,
                                 @ApiParam("2:即将过期,3面额最大排序") @RequestParam(value = "type", defaultValue = "1") Integer type,
                                 @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer pageIndex,
                                 @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "15") Integer size) {
        IPage<Coupon> couponIPage = couponService.selectCouponPage(new Page<>(pageIndex, size), catId, type);
        couponService.withStore(couponIPage.getRecords());
        return couponIPage;
    }

    @GetMapping("/user/page")
    public IPage<CouponList> couponList(@RequestParam(value = "start_time", required = false) Long startTime,
                                        @RequestParam(value = "end_time", required = false) Long endTime,
                                        @RequestParam(value = "cid", required = false) Integer couponId,
                                        @RequestParam(value = "p", defaultValue = "1") Integer pageIndex,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<CouponList> queryWrapper = new QueryWrapper<>();
        if (couponId != null) {
            queryWrapper.eq("cid", couponId);
        }
        IPage<CouponList> couponListPage = couponListService.getCouponListPage(new Page<>(pageIndex, size), queryWrapper);
        couponListService.withSource(couponListPage.getRecords());
        return couponListPage;
    }

    @ApiOperation(value = "优惠券列表接口", notes = "获取店铺优惠券列表接口", httpMethod = "GET")
    @GetMapping("/store/page")
    public IPage<Coupon> getCouponPage(@RequestParam(value = "store_id",required = false) Integer storeId,
                                       @RequestParam(value = "start_time", required = false) Long startTime,
                                       @RequestParam(value = "end_time", required = false) Long endTime,
                                       @RequestParam(value = "type", required = false) Integer type,
                                       @RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "p", defaultValue = "1") Integer p,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Coupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("1=1");
        if (storeId != null) {
            queryWrapper.eq("store_id",storeId);
        }
        if (startTime != null && endTime != null) {
            queryWrapper.between("add_time", startTime, endTime);
        }
        if (type != null) {
            queryWrapper.eq("type", type);
        }
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.like("name",name);
        }
        queryWrapper.orderByDesc("id");
        IPage<Coupon> couponIPage = couponService.page(new Page<>(p, size), queryWrapper);
        couponService.withStore(couponIPage.getRecords());
        return couponIPage;
    }

    @GetMapping("{id}")
    public Coupon getOne(@PathVariable("id") Integer id) {
        return couponService.getById(id);
    }

    @GetMapping
    public Coupon getCoupon(@RequestParam("id") Integer couponId) {
        return couponService.getCoupon(couponId);
    }

    @PostMapping
    public SBApi addCoupon(@Validated(StoreCoupon.class) @RequestBody Coupon coupon) {
        couponService.addCoupon(coupon);
        return new SBApi();
    }

    @PutMapping
    public SBApi updateCoupon(@Validated(StoreCoupon.class) @RequestBody Coupon coupon) {
        couponService.updateCoupon(coupon);
        return new SBApi();
    }

    @PostMapping("newcomer")
    public SBApi addNewcomerCoupon(@Validated(NewcomerCoupon.class) @RequestBody Coupon coupon) {
        couponService.addCoupon(coupon);
        return new SBApi();
    }

    @PutMapping("newcomer")
    public SBApi updateNewcomerCoupon(@Validated(NewcomerCoupon.class) @RequestBody Coupon coupon) {
        couponService.updateCoupon(coupon);
        return new SBApi();
    }

    @DeleteMapping("/{id}")
    public SBApi deleteCoupon(@PathVariable(value = "id") Integer id) {
        couponService.deleteCoupon(id);
        return new SBApi();
    }

    @DeleteMapping("/user_coupon/{id}")
    public SBApi deleteCouponList(@PathVariable("id") Integer id) {
//        couponListService.removeById(id);
        couponListService.update(new UpdateWrapper<CouponList>().set("deleted", 1).eq("id", id));
        return new SBApi();
    }

    @PostMapping("send")
    public SBApi sendCoupons(@RequestParam("ids") Set<Integer> ids,
                             @RequestParam(value = "cid", required = false) Integer cid,
                             @RequestParam(value = "send_time", required = false) Long sendTime,
                             @RequestParam(value = "store_id", required = false) Integer storeId) {

        Coupon coupon = couponService.getById(cid);
        Integer sendNum = coupon.getSendNum();
        int num = coupon.getCreatenum() - sendNum;//优惠券剩余数量
        if (ids.size() > num) {
            throw new ShopException(ResultEnum.COUPON_NUM_ERROR);
        }
        CouponList couponList = new CouponList();
        couponList.setType(1);
        for (Integer uid : ids) {
            couponList.setUid(uid);
            couponList.setCid(cid);
            couponList.setSendTime(sendTime);
            couponList.setStoreId(storeId);
            couponListService.save(couponList);
        }
        couponService.update(new UpdateWrapper<Coupon>().eq("id", cid).set("send_num", sendNum + ids.size()));
        return new SBApi();
    }

    @GetMapping("list")
    public List<Coupon> getCouponList(@RequestParam(value = "store_id",required = false) Integer storeId) {
        List<Coupon> couponList = couponService.list(new QueryWrapper<Coupon>()
                .eq("store_id", storeId)
                .eq("type",0)
                .eq("status", 1)
                .eq("createnum", 0)
                .gt("use_end_time",System.currentTimeMillis() / 1000));
        return couponList;
    }

}
