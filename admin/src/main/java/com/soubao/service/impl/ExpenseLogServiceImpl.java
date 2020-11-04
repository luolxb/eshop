package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.ExpenseLog;
import com.soubao.dao.ExpenseLogMapper;
import com.soubao.service.ExpenseLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 平台支出金额或赠送积分记录日志 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-05-06
 */
@Service
public class ExpenseLogServiceImpl extends ServiceImpl<ExpenseLogMapper, ExpenseLog> implements ExpenseLogService {

    @Autowired
    private ExpenseLogMapper expenseLogMapper;

    @Override
    public IPage<ExpenseLog> getExpenseLogPage(Page<ExpenseLog> page, QueryWrapper<ExpenseLog> queryWrapper) {
        return expenseLogMapper.getExpenseLogPage(page,queryWrapper);
    }
}
