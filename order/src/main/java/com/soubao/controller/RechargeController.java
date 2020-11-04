package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Recharge;
import com.soubao.entity.User;
import com.soubao.service.RechargeService;
import com.soubao.service.impl.AuthenticationFacade;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-02-27
 */
@RestController
@RequestMapping("recharge")
public class RechargeController {
    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping("page")
    public Page<Recharge> getPointPage(
            @RequestParam(value = "start_time", required = false) Long startTime,
            @RequestParam(value = "end_time", required = false) Long endTime,
            @RequestParam(value = "nickname", required = false) String nickname,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "16") Integer size) {
        QueryWrapper<Recharge> queryWrapper = new QueryWrapper<>();
        if (startTime != null && endTime != null) {
            queryWrapper.between("ctime", startTime, endTime);
        }
        if (nickname != null) {
            queryWrapper.like("nickname", nickname);
        }
        queryWrapper.orderByDesc("ctime");
        return (Page<Recharge>) rechargeService.page(new Page<>(page, size), queryWrapper);
    }

    @ApiOperation("获取用户充值记录")
    @GetMapping("/page/user")
    public Page<Recharge> getUserPage(
            @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
            @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Recharge> queryWrapper = new QueryWrapper<>();
        User user = authenticationFacade.getPrincipal(User.class);
        queryWrapper.eq("user_id", user.getUserId());
        return (Page<Recharge>) rechargeService.page(new Page<>(page, size), queryWrapper);
    }

}
