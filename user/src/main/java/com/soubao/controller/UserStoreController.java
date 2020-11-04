package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.User;
import com.soubao.entity.UserStore;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.UserService;
import com.soubao.service.UserStoreService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(value = "用户店铺控制器", tags = {"用户店铺相关接口"})
@PreAuthorize("hasAnyRole('ROLE_USER')")
@RequestMapping("store")
@RestController
public class UserStoreController {
    @Autowired
    private UserStoreService userStoreService;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private UserService userService;

//    @GetMapping
//    @ApiOperation(value = "获取用户店铺信息")
//    public UserStore getUser() {
//        User user = authenticationFacade.getPrincipal(User.class);
//        return userStoreService.getOne((new QueryWrapper<UserStore>()).eq("user_id", user.getUserId()));
//    }

    @ApiOperation(value = "添加用户店铺信息")
    @PostMapping
    public SBApi addStore(@RequestBody UserStore userStore, SBApi sbApi) {
        User user = userService.getById(authenticationFacade.getPrincipal(User.class).getUserId());
        UserStore exUserStore = userStoreService.getOne((new QueryWrapper<UserStore>()).eq("user_id", user.getUserId()));
        if (exUserStore != null) {
            throw new ShopException(ResultEnum.USER_HAVE_STORE);
        }
        userStore.setStoreTime(System.currentTimeMillis() / 1000);
        userStore.setStoreName(user.getNickname());
        userStore.setTrueName(user.getNickname());
        userStore.setMobile(user.getMobile());
        userStore.setUserId(user.getUserId());
        userStoreService.save(userStore);
        return sbApi;
    }

    @ApiOperation(value = "修改用户店铺信息")
    @PutMapping
    public SBApi updateStore(@RequestBody UserStore userStore, SBApi sbApi) {
        User user = authenticationFacade.getPrincipal(User.class);
        UserStore exUserStore = userStoreService.getOne((new QueryWrapper<UserStore>()).eq("user_id", user.getUserId()));
        userStore.setId(exUserStore.getId());
        userStoreService.updateById(userStore);
        return sbApi;
    }
}
