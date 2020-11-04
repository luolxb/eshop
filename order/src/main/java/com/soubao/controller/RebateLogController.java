package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.RebateLog;
import com.soubao.entity.RebateLogSurvey;
import com.soubao.entity.User;
import com.soubao.service.RebateLogService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import com.soubao.vo.UserLowerVo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/rebate_log")
@RestController
public class RebateLogController {
    @Autowired
    private RebateLogService rebateLogService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("获取订单商品分销")
    @GetMapping("order_goods")
    public Page<RebateLog> getRebateOrderGoodsList(
            @RequestParam(value = "status", required = false) Set<Integer> status,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        return (Page<RebateLog>) rebateLogService.getRebateOrderGoodsPage(new Page<>(page, size), user.getUserId(), status);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("获取用户下线订单概况")
    @GetMapping("survey")
    public RebateLogSurvey getRebateLogSurvey() {
        User user = authenticationFacade.getPrincipal(User.class);
        RebateLogSurvey rebateLogSurvey = new RebateLogSurvey();
        RebateLog rebateLog = rebateLogService.getOne((new QueryWrapper<RebateLog>())
                .select("IFNULL(sum(goods_price), 0.00) as goods_price, count(id) as id").eq("user_id", user.getUserId()).in("status", 1, 2, 3));
        rebateLogSurvey.setOrderCount(rebateLog.getId());
        rebateLogSurvey.setGoodsPriceSum(rebateLog.getGoodsPrice());
        rebateLogSurvey.setDistributedMoneySum((rebateLogService.getOne((new QueryWrapper<RebateLog>())
                .select("IFNULL(sum(money), 0.00) as money").eq("user_id", user.getUserId()).eq("status", 3))).getMoney());
        rebateLogSurvey.setTodayIncomeMoney((rebateLogService.getOne((new QueryWrapper<RebateLog>())
                .select("IFNULL(sum(money), 0.00) as money").eq("user_id", user.getUserId()).in("status", 2, 3)
                .apply("create_time > UNIX_TIMESTAMP(CAST(SYSDATE()AS DATE))"))).getMoney());
        rebateLogSurvey.setUnsettleMoneySum((rebateLogService.getOne((new QueryWrapper<RebateLog>())
                .select("IFNULL(sum(money), 0.00) as money").eq("user_id", user.getUserId()).eq("status", 1))).getMoney());
        rebateLogSurvey.setPendingReceiptMoneySum((rebateLogService.getOne((new QueryWrapper<RebateLog>())
                .select("IFNULL(sum(money), 0.00) as money").eq("user_id", user.getUserId()).in("status", 1, 2))).getMoney());
        rebateLogSurvey.setInvalidMoneySum((rebateLogService.getOne((new QueryWrapper<RebateLog>())
                .select("IFNULL(sum(money), 0.00) as money").eq("user_id", user.getUserId()).eq("status", 4))).getMoney());
        return rebateLogSurvey;
    }

    @PutMapping
    public SBApi updateRebateLog(@RequestBody RebateLog rebateLog) {
        rebateLogService.updateById(rebateLog);
        return new SBApi();
    }

    @GetMapping("page")
    public IPage<RebateLog> pageRebateLog(@RequestParam(value = "store_id", required = false) Integer storeId,
                                          @RequestParam(value = "status", required = false) Integer status,
                                          @RequestParam(value = "user_id", required = false) Integer userId,
                                          @RequestParam(value = "order_sn", required = false) String orderSn,
                                          @RequestParam(value = "start_time", required = false) Long startTime,
                                          @RequestParam(value = "end_time", required = false) Long endTime,
                                          @RequestParam(value = "p", defaultValue = "1") Integer page,
                                          @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<RebateLog> queryWrapper = new QueryWrapper<>();
        if (storeId != null) {
            queryWrapper.eq("store_id", storeId);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (StringUtils.isNotEmpty(orderSn)) {
            queryWrapper.like("order_sn", orderSn);
        }
        if (startTime != null) {
            queryWrapper.ge("create_time", startTime);
        }
        if (endTime != null) {
            queryWrapper.lt("create_time", endTime);
        }
        queryWrapper.orderByDesc("id");
        IPage<RebateLog> rebateLogIPage = rebateLogService.page(new Page<>(page, size), queryWrapper);
        rebateLogService.withUser(rebateLogIPage.getRecords());
        return rebateLogIPage;
    }

    @ApiOperation("用户下线列表")
    @GetMapping("lowers")
    public Page<UserLowerVo> LowerUsers(@RequestParam(value = "user_id") Integer userId,
                                        @RequestParam(value = "level", defaultValue = "2") Integer level,
                                        @RequestParam(value = "p", defaultValue = "1") Integer page,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<UserLowerVo> userLowerVoIPage = rebateLogService.selectLowerPage((new Page<>(page, size)), userId, level);
        rebateLogService.withUserByUserLowerVo(userLowerVoIPage.getRecords());
        return (Page<UserLowerVo>) userLowerVoIPage;
    }

}
