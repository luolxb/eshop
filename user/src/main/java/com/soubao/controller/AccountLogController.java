package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.AccountLog;
import com.soubao.entity.User;
import com.soubao.service.AccountLogService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "用户资金记录", tags = {"用户资金记录相关接口"})
@RestController
@RequestMapping("account_log")
public class AccountLogController {
    @Autowired
    private AccountLogService accountLogService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @ApiOperation(value = "用户获取资金余额列表信息")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("user_money/page")
    public Page<AccountLog> getUserMoneyPage(@RequestParam(value = "is_increase", required = false) Integer isIncrease,
                                             @RequestParam(value = "p", defaultValue = "1") Integer page,
                                             @RequestParam(value = "size", defaultValue = "16") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        QueryWrapper<AccountLog> accountLogQueryWrapper = new QueryWrapper<>();
        accountLogQueryWrapper.eq("user_id", user.getUserId()).orderByDesc("change_time");
        if (StringUtils.isEmpty(isIncrease)) {
            accountLogQueryWrapper.ne("user_money", 0);
        } else {
            if (isIncrease == 1) {
                accountLogQueryWrapper.gt("user_money", 0);
            }
            if (isIncrease == 0) {
                accountLogQueryWrapper.lt("user_money", 0);
            }
        }
        return (Page<AccountLog>) accountLogService.page(new Page<>(page, size), accountLogQueryWrapper);
    }

    @ApiOperation(value = "用户获取资金积分列表信息")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("pay_points/page")
    public Page<AccountLog> getPointPage(@RequestParam(value = "is_increase", required = false) Integer isIncrease,
                                         @RequestParam(value = "type", required = false) Integer type,
                                         @RequestParam(value = "p", defaultValue = "1") Integer page,
                                         @RequestParam(value = "size", defaultValue = "16") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        QueryWrapper<AccountLog> accountLogQueryWrapper = new QueryWrapper<>();
        accountLogQueryWrapper.eq("user_id", user.getUserId()).orderByDesc("change_time");
        if (StringUtils.isEmpty(isIncrease)) {
            accountLogQueryWrapper.ne("pay_points", 0);
        } else {
            if (isIncrease == 1) {
                accountLogQueryWrapper.gt("pay_points", 0);
            }
            if (isIncrease == 0) {
                accountLogQueryWrapper.lt("pay_points", 0);
            }
        }
        if (!StringUtils.isEmpty(type)) {
            if (type == 0) {//下单赠送积分
                accountLogQueryWrapper.eq("`desc`", "下单赠送积分");
            }
            if (type == 1) {//下单消费
                accountLogQueryWrapper.eq("`desc`", "下单消费");
            }
            if (type == 2) {//订单退货
                accountLogQueryWrapper.eq("`desc`", "订单退货");
            }
            if (type == 3) {//退款到用户余额
                accountLogQueryWrapper.eq("`desc`", "退款到用户余额");
            }
        }
        return (Page<AccountLog>) accountLogService.page(new Page<>(page, size), accountLogQueryWrapper);
    }

    @PostMapping
    public SBApi addAccountLog(@Valid @RequestBody AccountLog accountLog) {
        accountLogService.saveAccountLog(accountLog);
        return new SBApi();
    }

    @ApiOperation(value = "管理员获取用户资金信息列表")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/list")
    public Page<AccountLog> getAccountLogList(@RequestParam(value = "user_id") Integer userId,
                                   @RequestParam(value = "p", defaultValue = "1") Integer page,
                                   @RequestParam(value = "size", defaultValue = "16") Integer size) {
        QueryWrapper<AccountLog> accountLogQueryWrapper = new QueryWrapper<>();
        accountLogQueryWrapper.eq("user_id", userId);
        accountLogQueryWrapper.orderByDesc("change_time");
        return (Page<AccountLog>) accountLogService.page(new Page<>(page, size), accountLogQueryWrapper);
    }
}
