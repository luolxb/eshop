package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Seller;
import com.soubao.entity.StoreNavigation;
import com.soubao.service.StoreNavigationService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 卖家店铺导航信息表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-03-24
 */
@RestController
@RequestMapping("store_navigation")
public class StoreNavigationController {
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private StoreNavigationService storeNavigationService;

    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("page")
    public IPage<StoreNavigation> page(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                       @RequestParam(value = "size", defaultValue = "1") Integer size) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        return storeNavigationService.page(new Page<>(page, size), new QueryWrapper<StoreNavigation>()
                .eq("sn_store_id", seller.getStoreId()).orderByAsc("sn_sort"));
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping
    public StoreNavigation getOne(@RequestParam("sn_id") Integer snId) {
        return storeNavigationService.getById(snId);
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @PostMapping
    public SBApi save(@Valid @RequestBody StoreNavigation storeNavigation) {
        storeNavigation.setSnStoreId(authenticationFacade.getPrincipal(Seller.class).getStoreId());
        storeNavigation.setSnAddTime(System.currentTimeMillis() / 1000);
        storeNavigationService.save(storeNavigation);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @PutMapping
    public SBApi update(@Valid @RequestBody StoreNavigation storeNavigation) {
        storeNavigationService.updateById(storeNavigation);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @DeleteMapping
    public SBApi remove(@RequestParam("sn_id") Integer snId) {
        storeNavigationService.removeById(snId);
        return new SBApi();
    }

}
