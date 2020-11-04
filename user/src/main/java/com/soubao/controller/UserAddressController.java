package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.User;
import com.soubao.entity.UserAddress;
import com.soubao.service.UserAddressService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2019-11-28
 */
@RestController
@RequestMapping("address")
@Api(value = "用户地址控制器", tags = {"用户地址相关接口"})
public class UserAddressController {
    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("list/admin")
    public List<UserAddress> getUserAddressList(@RequestParam("user_id") Integer userId) {
        return userAddressService.list(new QueryWrapper<UserAddress>().eq("user_id", userId));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/list")
    @ApiOperation(value = "用户地址列表")
    public List<UserAddress> addresses() {
        User user = authenticationFacade.getPrincipal(User.class);
        List<UserAddress> userAddressList = userAddressService.list((new QueryWrapper<UserAddress>())
                .eq("user_id", user.getUserId()).orderByDesc("is_default"));
        userAddressService.withRegionName(userAddressList);
        return userAddressList;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation(value = "单个用户地址")
    @GetMapping("/{address_id}")
    public UserAddress address(@ApiParam("用户地址id") @PathVariable("address_id") Integer addressId) {
        User user = authenticationFacade.getPrincipal(User.class);
        UserAddress userAddress = userAddressService.getOne(new QueryWrapper<UserAddress>().eq("address_id", addressId)
                .eq("user_id", user.getUserId()));
        userAddressService.withRegionName(userAddress);
        return userAddress;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping
    public UserAddress getAddress(@RequestParam(value = "id") Integer addressId) {
        User user = authenticationFacade.getPrincipal(User.class);
        return userAddressService.getOne((new QueryWrapper<UserAddress>()
                .eq("user_id", user.getUserId()).eq("address_id", addressId)));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation(value = "添加用户地址")
    @PostMapping
    public SBApi addAddress(@RequestBody @Valid UserAddress userAddress) {
        User user = authenticationFacade.getPrincipal(User.class);
        userAddressService.addUserAddress(user, userAddress);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation(value = "修改用户地址")
    @PutMapping
    public SBApi updateAddress(@RequestBody @Valid UserAddress userAddress) {
        User user = authenticationFacade.getPrincipal(User.class);
        userAddressService.updateUserAddress(user, userAddress);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation(value = "删除用户地址")
    @DeleteMapping("/{address_id}")
    public SBApi removeUserAddress(@ApiParam("用户地址id") @PathVariable("address_id") Integer addressId, SBApi sbApi) {
        User user = authenticationFacade.getPrincipal(User.class);
        userAddressService.removeUserAddress(user, addressId);
        return new SBApi();
    }
}
