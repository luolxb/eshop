package com.soubao.service;

import com.soubao.entity.ExpenseLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "admin")
public interface AdminService {

    @PostMapping("/expense_log")
    String addExpenseLog(@RequestBody ExpenseLog expenseLog);
}
