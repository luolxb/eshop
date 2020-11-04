package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Withdrawals;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soubao.vo.WithdrawalsExcel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2020-04-08
 */
public interface WithdrawalsMapper extends BaseMapper<Withdrawals> {

    IPage<Withdrawals> selectWithdrawalsPage(Page<Withdrawals> page, @Param(Constants.WRAPPER) QueryWrapper<Withdrawals> queryWrapper);

    Withdrawals selectUserWithdrawalsById(@Param("id") Integer id);
    //查询用户提现申请/转款记录导出数据
    List<WithdrawalsExcel> selectWithDrawalsExportData(@Param(Constants.WRAPPER) QueryWrapper<Withdrawals> wrapper);
}
