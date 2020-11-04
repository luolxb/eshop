package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.User;
import com.soubao.entity.UserSign;
import com.soubao.service.UserSignService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import com.soubao.vo.UserSignStatisticsVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-04-08
 */
@RestController
@RequestMapping("/sign")
public class UserSignController {
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private UserSignService userSignService;

    @GetMapping("/page")
    public IPage<UserSign> getUserSignPage(@RequestParam(value = "mobile", required = false) String mobile,
                                           @RequestParam(value = "p", defaultValue = "1") Integer p,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<UserSign> wrapper = new QueryWrapper<>();
        wrapper.apply("1=1");
        if (!StringUtils.isEmpty(mobile)) {
            wrapper.like("u.mobile", mobile);
        }
        wrapper.orderByDesc("s.id");
        return userSignService.getUserSignPage(new Page(p, size), wrapper);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("获取会员签到统计")
    @GetMapping
    public UserSignStatisticsVo signStatistics() {
        User user = authenticationFacade.getPrincipal(User.class);
        UserSign sign = userSignService.getOne(new QueryWrapper<UserSign>()
                .eq("user_id", user.getUserId())
                .last("limit 1"));
        return userSignService.getStatisticsBySign(sign);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("会员签到")
    @PostMapping
    public SBApi sign() {
        User user = authenticationFacade.getPrincipal(User.class);
        userSignService.sign(user);
        return new SBApi();
    }
}
