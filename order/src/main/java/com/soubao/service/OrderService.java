package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.dto.BatchDelivery;
import com.soubao.dto.DeliveryPickOrderRq;
import com.soubao.dto.UserOrderSum;
import com.soubao.entity.*;
import com.soubao.entity.vo.OrderAndPickOrderVo;
import com.soubao.vo.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OrderService extends IService<Order> {
    //取消订单
    void cancel(Order order);


    //批量取消拼团订单
    void cancelTeamOrderList(Integer sellerId, List<Order> teamOrderList);

    /**
     * 主订单入库
     *
     * @param order
     * @return
     */
    Order addAndGetMasterOrder(Order order,User user );

    void useMoneyPayOrders(List<Order> orders, BigDecimal userMoney);

    Order calculateMasterOrder(Order masterOrder);

    //签收订单
    void receive(Order order, Order requestOrder);

    /**
     * 支付后获取订单支付状态
     *
     * @return
     */
    Integer getPayStatusByPay(Order order);

    //删除订单
    void removeOrder(Integer orderId, Integer storeId);

    void withOrderGoods(List<Order> records);

    void withUser(List<Order> records);

    void withDelivery(List<Order> records);

    //订单打印信息
    List<Order> listOrderPrint(Set<Integer> orderIds);

    void withRegions(List<Order> records);

    void withExcelRegions(List<OrderExcel> orderExcelList);

    //订单批量发货
    void batchDelivery(BatchDelivery batchDelivery);

    /**
     * 设置订单无效
     */
    void invalid(Order order, Seller seller);

    /**
     * 确认订单
     */
    void confirm(Order order, Seller seller);

    /**
     * 取消确认订单
     */
    void cancelConfirm(Order order, Seller seller);

    //发货订单详情
    Order deliveryInfo(Integer orderId);

    /**
     * 获取每天的订单统计
     *
     * @return
     */
    List<OrderDayReport> getOrderDayReportList(QueryWrapper wrapper, Long startTime, Long endTime);

    List<OrderDayReport> getOrderDayFinanceList(QueryWrapper<Order> orderQueryWrapper, Long startTime, Long endTime);

    void setWrapperByType(QueryWrapper<Order> orderQueryWrapper, String type, Integer promType, Integer userId);

    IPage<SalesRanking> getSalesRankingPage(Page<SalesRanking> page, QueryWrapper<OrderGoods> queryWrapper);

    IPage<StoreRanking> getStoreRankingPage(Page<StoreRanking> page, QueryWrapper<Order> orderQueryWrapper);

    IPage<SaleDayDetails> getSaleDayDetailsPage(Page<SaleDayDetails> page, QueryWrapper<Order> orderQueryWrapper);

    //获取订单导出数据
    List<OrderExcel> getOrderExportData(QueryWrapper<Order> wrapper);

    void schedule();

    List<UserOrderStatistics> userOrderStatisticsList(QueryWrapper<Order> queryWrapper);

    void withStore(List<Order> records);

    void withRankingStore(List<StoreRanking> records);

    List<UserOrderSum> getUserOrderSumByUserIds(Set<Integer> userIds);

    void modify(Seller seller, Order order, Order editOrder);

    void cancelPay(Order order, OrderAction orderAction,Integer refundType);

    Order getOrder(Integer orderId);

    IPage<OrderAndPickOrderVo> getOrderAndPickOrderPage(Integer page, Integer size, User user);
}
