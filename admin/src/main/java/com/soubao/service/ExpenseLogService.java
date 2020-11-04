package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.ExpenseLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 平台支出金额或赠送积分记录日志 服务类
 * </p>
 *
 * @author dyr
 * @since 2020-05-06
 */
public interface ExpenseLogService extends IService<ExpenseLog> {

    IPage<ExpenseLog> getExpenseLogPage(Page<ExpenseLog> page, QueryWrapper<ExpenseLog> queryWrapper);

}
