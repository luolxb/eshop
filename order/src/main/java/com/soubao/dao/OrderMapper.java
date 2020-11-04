package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.dto.UserOrderSum;
import com.soubao.entity.Order;
import com.soubao.entity.OrderGoods;
import com.soubao.entity.vo.OrderAndPickOrderVo;
import com.soubao.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface OrderMapper extends BaseMapper<Order> {
    List<OrderDayReport> getOrderDayReportList(@Param(Constants.WRAPPER) QueryWrapper wrapper, Long startTime, Long endTime, Integer dayNum);

    List<OrderDayReport> getOrderDayFinanceList(@Param(Constants.WRAPPER) QueryWrapper orderQueryWrapper, Long startTime, Long endTime, Integer dayNum);

    IPage<SalesRanking> getSalesRankingPage(Page<SalesRanking> page, @Param(Constants.WRAPPER) QueryWrapper<OrderGoods> queryWrapper);

    IPage<StoreRanking> getStoreRankingPage(Page<StoreRanking> page, @Param(Constants.WRAPPER) QueryWrapper<Order> wrapper);

    IPage<SaleDayDetails> getSaleDayDetailsPage(Page<SaleDayDetails> page,  @Param(Constants.WRAPPER) QueryWrapper<Order> wrapper);

    List<OrderExcel> selectOrderExportData(@Param(Constants.WRAPPER) QueryWrapper<Order> wrapper);

    List<UserOrderStatistics> selectUserOrderStatisticsList(@Param(Constants.WRAPPER) QueryWrapper<Order> wrapper);

    List<UserOrderSum> selectUserOrderSumByUserIds(@Param("userIds") Set<Integer> userIds);

    IPage<OrderAndPickOrderVo> getOrderAndPickOrderPage(Page<OrderAndPickOrderVo> page,@Param("userId") Integer userId);
}