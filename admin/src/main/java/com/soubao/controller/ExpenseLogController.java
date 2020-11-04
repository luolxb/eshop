package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.ExpenseLog;
import com.soubao.service.ExpenseLogService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 平台支出金额或赠送积分记录日志 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-05-06
 */
@RestController
@RequestMapping("/expense_log")
public class ExpenseLogController {
    @Autowired
    private ExpenseLogService expenseLogService;

    @PostMapping
    public SBApi add(@RequestBody ExpenseLog expenseLog) {
        expenseLogService.save(expenseLog);
        return new SBApi();
    }

    @GetMapping("page")
    public IPage<ExpenseLog> getExpenseLogPage(@RequestParam(value = "start_time") Long startTime,
                                               @RequestParam(value = "end_time") Long endTime,
                                               @RequestParam(value = "admin_id", required = false) Integer adminId,
                                               @RequestParam(value = "p", defaultValue = "1") Integer p,
                                               @RequestParam(value = "size", defaultValue = "20") Integer size) {
        QueryWrapper<ExpenseLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("e.addtime", startTime, endTime);
        if (adminId != null) {
            queryWrapper.eq("e.admin_id", adminId);
        }
        return expenseLogService.getExpenseLogPage(new Page<>(p, size), queryWrapper);
    }
}
