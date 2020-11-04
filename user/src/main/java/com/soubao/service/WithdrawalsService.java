package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.User;
import com.soubao.entity.Withdrawals;
import com.soubao.vo.DistributeSurvey;
import com.soubao.vo.WithdrawalsExcel;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2020-04-08
 */
public interface WithdrawalsService extends IService<Withdrawals> {
    //获取用户提现申请/转款记录导出数据
    List<WithdrawalsExcel> getWithdrawalsExportData(QueryWrapper<Withdrawals> wrapper);
    //用户申请提现
    void userWithdrawal(User user, Withdrawals withdrawals);

    IPage<Withdrawals> getWithdrawalsPage(Page<Withdrawals> page, QueryWrapper<Withdrawals> queryWrapper);

    Withdrawals getWithdrawalsById(Integer id);

    void updateWithdrawals(Withdrawals withdrawals);

    void updateWithdrawalsStatus(Set<Integer> ids, Integer status);
    //获取用户提现概况
    DistributeSurvey getDistributeSurvey(User user);
}
