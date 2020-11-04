package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.OrderStatis;
import com.soubao.entity.Seller;
import com.soubao.entity.User;
import com.soubao.service.OrderStatisService;
import com.soubao.service.SellerService;
import com.soubao.service.impl.AuthenticationFacade;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * <p>
 * 商家订单结算表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2019-12-19
 */
@RestController
@RequestMapping("/order_statis")
public class OrderStatisController {
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private OrderStatisService orderStatisService;
    @Autowired
    private SellerService sellerService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/page")
    public IPage<OrderStatis> getPage(
            @RequestParam(value = "start_time", required = false) Long startTime,
            @RequestParam(value = "end_time", required = false) Long endTime,
            @RequestParam(value = "store_name", required = false) String storeName,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<OrderStatis> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        IPage<OrderStatis> orderStatisIPage = new Page<>();
        if (startTime != null) {
            wrapper.ge("start_date", startTime);
        }
        if (endTime != null) {
            wrapper.lt("end_date", endTime);
        }
        if(null != storeName){
            Set<Integer> sIds = sellerService.storeIdsByStoreName(storeName);
            if(sIds.size() > 0){
                wrapper.in("store_id", sIds);
            }else{
                return orderStatisIPage;
            }
        }
        orderStatisIPage = orderStatisService.page(new Page<>(page, size), wrapper);
        orderStatisService.withStore(orderStatisIPage.getRecords());
        return orderStatisIPage;
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("/page/seller")
    public IPage<OrderStatis> getPage(
            @RequestParam(value = "start_time", required = false) Long startTime,
            @RequestParam(value = "end_time", required = false) Long endTime,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        QueryWrapper<OrderStatis> wrapper = new QueryWrapper<>();
        wrapper.eq("store_id", seller.getStoreId()).orderByDesc("id");
        if (startTime != null) {
            wrapper.ge("start_date", startTime);
        }
        if (endTime != null) {
            wrapper.lt("end_date", endTime);
        }
        return orderStatisService.page(new Page<>(page, size), wrapper);
    }

}
