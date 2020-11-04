package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.ExpenseLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 平台支出金额或赠送积分记录日志 Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2020-05-06
 */
public interface ExpenseLogMapper extends BaseMapper<ExpenseLog> {

    IPage<ExpenseLog> getExpenseLogPage(Page<ExpenseLog> page, @Param(Constants.WRAPPER) QueryWrapper<ExpenseLog> queryWrapper);

}
