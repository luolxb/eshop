package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.dto.UserWithdrawalDto;
import com.soubao.entity.User;
import com.soubao.entity.Withdrawals;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.UserService;
import com.soubao.service.WithdrawalsService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.vo.DistributeSurvey;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2019-09-11
 */
@Api(value = "用户提现控制器", tags = {"用户提现相关接口"})
@RestController
@RequestMapping("/withdrawals")
public class WithdrawalsController {
    @Autowired
    private WithdrawalsService withdrawalsService;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private UserService userService;

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Value("${security.salt}")
    private String salt;

    @ApiOperation("提现申请分页")
    @GetMapping("page")
    public Page<Withdrawals> page(
            @ApiParam("提现类型:0用户余额,1佣金提现") @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        QueryWrapper<Withdrawals> withdrawalsQueryWrapper = new QueryWrapper<>();
        withdrawalsQueryWrapper.eq("user_id", user.getUserId()).orderByDesc("id");
        if (!StringUtils.isEmpty(type)) {
            withdrawalsQueryWrapper.eq("type", type);
        }
        return (Page<Withdrawals>) withdrawalsService.page((new Page<>(page, size)), withdrawalsQueryWrapper);
    }

    @ApiOperation("统计数")
    @GetMapping("count")
    public Integer count(@ApiParam("提现类型:0用户余额,1佣金提现") @RequestParam(value = "type") Integer type) {
        User user = authenticationFacade.getPrincipal(User.class);
        QueryWrapper<Withdrawals> withdrawalsQueryWrapper = new QueryWrapper<>();
        withdrawalsQueryWrapper.eq("user_id", user.getUserId());
        if (null != type) {
            withdrawalsQueryWrapper.eq("type", type);
        }
        return withdrawalsService.count(withdrawalsQueryWrapper);
    }

    @GetMapping("count/admin")
    public Integer withdrawalsCount(@RequestParam(value = "status",required = false) Integer status) {
        QueryWrapper<Withdrawals> withdrawalsQueryWrapper = new QueryWrapper<>();
        if (null != status) {
            withdrawalsQueryWrapper.eq("status", status);
        }
        return withdrawalsService.count(withdrawalsQueryWrapper);
    }

    @ApiOperation("分销提现概况")
    @GetMapping("/survey")
    public DistributeSurvey distributeSurvey() {
        User user = userService.getById(authenticationFacade.getPrincipal(User.class).getUserId());
        return withdrawalsService.getDistributeSurvey(user);
    }

    @ApiOperation("用户余额提现")
    @PostMapping
    public SBApi userWithdrawal(@Valid @RequestBody UserWithdrawalDto userWithdrawal) {
        User user = userService.getById(authenticationFacade.getPrincipal(User.class).getUserId());
        if (StringUtils.isEmpty(user.getPaypwd())) {
            throw new ShopException(ResultEnum.PAYPWD_NOT_SET);
        }
        if (!passwordEncoder.matches(userWithdrawal.getPayPwd(), user.getPaypwd().replace(salt, ""))) {
            throw new ShopException(ResultEnum.INVALID_PAY_PWD);
        }
        Withdrawals withdrawals = new Withdrawals();
        withdrawals.setType(userWithdrawal.getType());
        withdrawals.setBankCard(userWithdrawal.getBankCard());
        withdrawals.setBankName(userWithdrawal.getBankName());
        withdrawals.setMoney(userWithdrawal.getMoney());
        withdrawals.setRealname(withdrawals.getRealname());
        withdrawalsService.userWithdrawal(user, withdrawals);
        return new SBApi();
    }

    @GetMapping("withdrawals_money")
    public SBApi withdrawalsMoney(SBApi sbApi) {
        User user = authenticationFacade.getPrincipal(User.class);
        Withdrawals withdrawals = withdrawalsService.getOne(new QueryWrapper<Withdrawals>().select("SUM(money) AS money").eq("user_id", user.getUserId()).eq("status", 1));
        sbApi.setResult(withdrawals != null ? withdrawals.getMoney() : 0);
        return sbApi;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("withdrawals_page")
    public IPage<Withdrawals> withdrawalsPage(@RequestParam(value = "start_time", required = false) Long startTime,
                                              @RequestParam(value = "end_time", required = false) Long endTime,
                                              @RequestParam(value = "status", required = false) Integer status,
                                              @RequestParam(value = "user_id", required = false) Integer userId,
                                              @RequestParam(value = "realname", required = false) String realname,
                                              @RequestParam(value = "bank_card", required = false) String bankCard,
                                              @RequestParam(value = "page", defaultValue = "1") Integer page,
                                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Withdrawals> queryWrapper = new QueryWrapper<>();
        if (startTime != null) {
            queryWrapper.ge("w.create_time", startTime);
        }
        if (endTime != null) {
            queryWrapper.lt("w.create_time", endTime);
        }
        if (status != null) {
            queryWrapper.eq("w.status", status);
        } else {
            queryWrapper.lt("w.status", 2);
        }
        if (userId != null) {
            queryWrapper.eq("w.user_id", userId);
        }
        if (realname != null) {
            queryWrapper.like("w.realname", realname);
        }
        if (bankCard != null) {
            queryWrapper.like("w.bank_card", bankCard);
        }
        queryWrapper.apply("1=1");
        queryWrapper.orderByDesc("w.id");
        return withdrawalsService.getWithdrawalsPage(new Page<>(page, size), queryWrapper);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("withdrawals_status")
    public SBApi updateWithdrawalsStatus(@RequestParam("ids") Set<Integer> ids,
                                         @RequestParam("status") Integer status) {
        withdrawalsService.updateWithdrawalsStatus(ids, status);
        return new SBApi();
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping
    public SBApi updateWithdrawals(@RequestBody Withdrawals withdrawals) {
        withdrawalsService.updateWithdrawals(withdrawals);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public Withdrawals getWithdrawalsById(@RequestParam(value = "id", required = false) Integer id) {
        return withdrawalsService.getWithdrawalsById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("{id}")
    public SBApi deleteWithdrawals(@PathVariable("id") Integer id) {
        withdrawalsService.removeById(id);
        return new SBApi();
    }

}
