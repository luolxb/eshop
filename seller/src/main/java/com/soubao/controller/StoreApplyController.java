package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Region;
import com.soubao.entity.StoreApply;
import com.soubao.entity.User;
import com.soubao.service.MallService;
import com.soubao.service.StoreApplyService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2019-12-06
 */
@RestController
@RequestMapping("/store_apply")
@Api(value = "店铺入驻控制器", tags = {"店铺入驻相关接口"})
public class StoreApplyController {
    @Autowired
    private StoreApplyService storeApplyService;

    @Autowired
    private MallService mallService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation(value = "获取用户的入驻资料")
    @GetMapping("/user")
    public StoreApply getOne() {
        User user = authenticationFacade.getPrincipal(User.class);
        return storeApplyService.getOne((new QueryWrapper<StoreApply>()).eq("user_id", user.getUserId()));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation(value = "添加入驻资料")
    @PostMapping("/user")
    public SBApi storeApplyStepOne(@RequestBody StoreApply storeApply) {
        User user = authenticationFacade.getPrincipal(User.class);
        storeApplyService.add(user, storeApply);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation(value = "更新入驻资料")
    @PutMapping("/user")
    public SBApi storeApplyStepTwo(@RequestBody StoreApply storeApply) {
        User user = authenticationFacade.getPrincipal(User.class);
        storeApplyService.updateByUser(user, storeApply);
        return new SBApi();
    }

    @GetMapping
    public StoreApply storeApply(@RequestParam("user_id") Integer userId) {
        StoreApply storeApply = storeApplyService.getOne(new QueryWrapper<StoreApply>()
                .eq("user_id", userId), false);
        storeApplyService.withRegionsName(storeApply);
        return storeApply;
    }

    @GetMapping("page")
    public IPage<StoreApply> page(@RequestParam(value = "sg_id", required = false)Integer sgId,
                                  @RequestParam(value = "sc_id", required = false)Integer scId,
                                  @RequestParam(value = "seller_name", required = false)String sellerName,
                                  @RequestParam(value = "store_name", required = false)String storeName,
                                  @RequestParam(value = "p", defaultValue = "1") Integer page,
                                  @RequestParam(value = "size", defaultValue = "10") Integer size){
        QueryWrapper<StoreApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("apply_state", 1).gt("add_time", 0);
        if (sgId != null){
            queryWrapper.eq("sg_id", sgId);
        }
        if (scId != null){
            queryWrapper.eq("sc_id", scId);
        }
        if (StringUtils.isNotEmpty(sellerName)){
            queryWrapper.like("seller_name", sellerName);
        }
        if (StringUtils.isNotEmpty(storeName)){
            queryWrapper.like("store_name", storeName);
        }
        queryWrapper.orderByDesc("add_time");
        IPage<StoreApply> storeApplyIPage = storeApplyService.page(new Page<>(page, size), queryWrapper);
        storeApplyService.withUser(storeApplyIPage.getRecords());
        return storeApplyIPage;
    }

    @PutMapping
    public SBApi storeAudit(@RequestBody StoreApply storeApply, SBApi sbApi){
        storeApplyService.storeAudit(storeApply);
        return sbApi;
    }

    @DeleteMapping
    public SBApi remove(@RequestParam("id")Integer id, SBApi sbApi){
        storeApplyService.removeById(id);
        return sbApi;
    }

    @GetMapping("/count")
    public Integer getCount(@RequestParam(value = "apply_state", required = false) Set<Integer> applyState) {
        QueryWrapper<StoreApply> storeApplyQueryWrapper = new QueryWrapper<>();
        storeApplyQueryWrapper.gt("add_time", 0);
        if(applyState != null){
            storeApplyQueryWrapper.in("apply_state", applyState);
        }
        return storeApplyService.count(storeApplyQueryWrapper);
    }

}
