package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.Withdrawals;
import com.soubao.service.UserService;
import com.soubao.service.WithdrawalsService;
import com.soubao.common.utils.TimeUtil;
import com.soubao.utils.excel.ExcelUtil;
import com.soubao.vo.UserExcel;
import com.soubao.vo.WithdrawalsExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

@Controller
public class ExcelController {
    @Autowired
    private UserService userService;
    @Autowired
    private WithdrawalsService withdrawalsService;

    @GetMapping("export")
    public void export(@RequestParam(value = "user_ids", required = false) Set<Integer> userIds,
                       @RequestParam(value = "account", required = false) String account,
                       @RequestParam(value = "nickname", required = false) String nickname,
                       @RequestParam(value = "first_leader", required = false) Integer firstLeader,
                       @RequestParam(value = "second_leader", required = false) Integer secondLeader,
                       @RequestParam(value = "third_leader", required = false) Integer thirdLeader,
                       HttpServletResponse response) {
        QueryWrapper<UserExcel> wrapper = new QueryWrapper<>();
        if (userIds != null && !userIds.isEmpty()) {
            wrapper.in("user_id", userIds);
        } else {
            if (!StringUtils.isEmpty(account)) {
                wrapper.and(w -> w.like("email", account).or().like("mobile", account));
            }
            if (!StringUtils.isEmpty(nickname)) {
                wrapper.like("nickname", nickname);
            }
            if (firstLeader != null) {
                wrapper.eq("first_leader", firstLeader);
            }
            if (secondLeader != null) {
                wrapper.eq("second_leader", secondLeader);
            }
            if (thirdLeader != null) {
                wrapper.eq("third_leader", thirdLeader);
            }
        }
        wrapper.apply("1=1");
        wrapper.orderByAsc("user_id");
        List<UserExcel> userExcelList = userService.getUserExportData(wrapper);
        String fileName = "会员_" + TimeUtil.transForDateStr(System.currentTimeMillis() / 1000, "yyyy-MM-dd HH.mm.ss");
        ExcelUtil.writeExcel(response, userExcelList, fileName, fileName, new UserExcel());
    }

    @GetMapping("withdrawals/export")
    public void export(@RequestParam(value = "ids", required = false) Set<Integer> ids,
                       @RequestParam(value = "user_id", required = false) Integer userId,
                       @RequestParam(value = "start_time", required = false) Integer startTime,
                       @RequestParam(value = "end_time", required = false) Integer endTime,
                       @RequestParam(value = "status", required = false) Integer status,
                       @RequestParam(value = "bank_card", required = false) String bankCard,
                       @RequestParam(value = "realname", required = false) String realname,
                       HttpServletResponse response) {
        QueryWrapper<Withdrawals> wrapper = new QueryWrapper<>();
        if (ids != null && !ids.isEmpty()) {
            wrapper.in("id", ids);
        } else {
            if (userId != null) {
                wrapper.eq("w.user_id", userId);
            }
            if (startTime != null) {
                wrapper.ge("w.create_time", startTime);
            }
            if (endTime != null) {
                wrapper.lt("w.create_time", endTime);
            }
            if (status != null) {
                wrapper.eq("w.status", status);
            }
            if (!org.apache.commons.lang.StringUtils.isEmpty(bankCard)) {
                wrapper.like("w.bank_card", bankCard);
            }
            if (!org.apache.commons.lang.StringUtils.isEmpty(realname)) {
                wrapper.like("w.realname", realname);
            }
        }
        wrapper.apply("1=1");
        wrapper.orderByDesc("w.id");
        List<WithdrawalsExcel> orderExcelList = withdrawalsService.getWithdrawalsExportData(wrapper);
        String fileName = "商家转款_" + TimeUtil.transForDateStr(System.currentTimeMillis() / 1000, "yyyy-MM-dd HH.mm.ss");
        ExcelUtil.writeExcel(response, orderExcelList, fileName, fileName, new WithdrawalsExcel());
    }

}
