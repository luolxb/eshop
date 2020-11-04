package com.soubao.controller;


import com.soubao.entity.CouponPrice;
import com.soubao.service.CouponPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 优惠券面额表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2019-12-17
 */
@RestController
@RequestMapping("/coupon_price")
public class CouponPriceController {
    @Autowired
    private CouponPriceService couponPriceService;

    @GetMapping("/list")
    public List<CouponPrice> getList(){
        return couponPriceService.list();
    }
}
