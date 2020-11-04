package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Seller;
import com.soubao.entity.Store;
import com.soubao.entity.StoreCollect;
import com.soubao.entity.User;
import com.soubao.service.SellerService;
import com.soubao.service.StoreCollectService;
import com.soubao.service.UserService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-03-23
 */
@RestController
@RequestMapping("/store_collect")
public class StoreCollectController {
    @Autowired
    private StoreCollectService storeCollectService;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private UserService userService;
    @Autowired
    private SellerService sellerService;

    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("/page")
    public IPage<StoreCollect> page(@RequestParam(value = "user_name", required = false) String userName,
                                    @RequestParam(value = "page", defaultValue = "1") Integer page,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size){
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        QueryWrapper<StoreCollect> storeCollectQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(userName)){
            storeCollectQueryWrapper.like("user_name", userName);
        }
        storeCollectQueryWrapper.eq("store_id", seller.getStoreId());
        return storeCollectService.page(new Page<>(page, size), storeCollectQueryWrapper);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("用户店铺关注记录列表")
    @GetMapping("/list")
    public List<StoreCollect> storeCollects() {
        User user = authenticationFacade.getPrincipal(User.class);
        return storeCollectService.list((new QueryWrapper<StoreCollect>()).eq("user_id", user.getUserId()));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("用户店铺关注记录分页")
    @GetMapping("user/page")
    public IPage<StoreCollect> storeCollectPage(@RequestParam(value = "sc_id", required = false) Integer scId,
                                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        QueryWrapper<StoreCollect> collectQueryWrapper = new QueryWrapper<>();
        if (null != scId) {
            Set<Integer> storeIds = sellerService.getStoresByScId(scId).stream().map(Store::getStoreId).collect(Collectors.toSet());
            if (storeIds.size() > 0) {
                collectQueryWrapper.in("store_id", storeIds);
            }
        }
        collectQueryWrapper.eq("user_id", user.getUserId());
        IPage<StoreCollect> collectPage = storeCollectService.page(new Page<>(page, size), collectQueryWrapper);
        storeCollectService.withStore(collectPage.getRecords());
        return collectPage;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("获取用户的店铺收藏的记录")
    @GetMapping("/{store_id}")
    public StoreCollect storeCollect(@ApiParam("店铺id") @PathVariable(value = "store_id") Integer storeId) {
        User user = authenticationFacade.getPrincipal(User.class);
        return storeCollectService.getOne((new QueryWrapper<StoreCollect>()).eq("user_id", user.getUserId())
                .eq("store_id", storeId));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("用户关注店铺")
    @PostMapping("/{store_id}")
    public SBApi addStoreCollect(@ApiParam("店铺id") @PathVariable("store_id") Integer storeId) {
        User user = userService.getById(authenticationFacade.getPrincipal(User.class).getUserId());
        storeCollectService.addStoreCollect(user, storeId);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("用户取消关注店铺")
    @DeleteMapping("/{store_id}")
    public SBApi removeStoreCollect(@PathVariable("store_id") Integer storeId) {
        User user = authenticationFacade.getPrincipal(User.class);
        storeCollectService.removeStoreCollect(user, storeId);
        return new SBApi();
    }

}
