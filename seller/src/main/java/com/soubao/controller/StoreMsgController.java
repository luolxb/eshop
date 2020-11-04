package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Seller;
import com.soubao.entity.StoreMsg;
import com.soubao.service.StoreMsgService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 店铺消息表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-03-19
 */
@RestController
@RequestMapping("/store_msg")
public class StoreMsgController {
    @Autowired
    private StoreMsgService storeMsgService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @GetMapping("page")
    public IPage<StoreMsg> page(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "10") Integer size){
        return storeMsgService.page(new Page<>(page, size), new QueryWrapper<StoreMsg>()
                .eq("store_id", (authenticationFacade.getPrincipal(Seller.class)).getStoreId()).orderByDesc("sm_id"));
    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @PutMapping("open")
    public SBApi updateOpen(@RequestParam("sm_ids") Set<Integer> smIds){
        storeMsgService.update(new UpdateWrapper<StoreMsg>().set("open", 1)
                .in("sm_id", smIds).eq("store_id", (authenticationFacade.getPrincipal(Seller.class)).getStoreId()));
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @DeleteMapping
    public SBApi remove(@RequestParam("sm_ids") Set<Integer> smIds){
        storeMsgService.remove(new QueryWrapper<StoreMsg>().in("sm_id", smIds)
                .eq("store_id", (authenticationFacade.getPrincipal(Seller.class)).getStoreId()));
        return new SBApi();
    }

}
