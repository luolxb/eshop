package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.SellerLog;
import com.soubao.service.SellerLogService;
import com.soubao.service.impl.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 卖家日志表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-03-26
 */
@RestController
@RequestMapping
public class SellerLogController {
    @Autowired
    private SellerLogService sellerLogService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @GetMapping("seller_log/page")
    public IPage<SellerLog> getSellerLogPage(@RequestParam(value = "store_id", required = false) Integer storeId,
                                             @RequestParam(value = "p", defaultValue = "1") Integer p,
                                             @RequestParam(value = "size", defaultValue = "20") Integer size) {
        QueryWrapper<SellerLog> queryWrapper = new QueryWrapper<>();
        if (storeId != null) {
            queryWrapper.eq("log_store_id",storeId);
        }
        queryWrapper.orderByDesc("log_time");
        return sellerLogService.page(new Page<>(p,size),queryWrapper);
    }

}
