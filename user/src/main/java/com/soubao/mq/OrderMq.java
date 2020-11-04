package com.soubao.mq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.common.constant.OrderConstant;
import com.soubao.entity.*;
import com.soubao.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderMq {
    @Autowired
    private AccountLogService accountLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserExtendService userExtendService;
    @Autowired
    private UserLevelService userLevelService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private SellerService sellerService;

    @RabbitListener(queues = "create_order.user")
    public void createOrdersAfter(List<Order> orders) {
        List<AccountLog> accountLogs = new ArrayList<>();
        List<Invoice> invoices = new ArrayList<>();

        User user = userService.getOne((new QueryWrapper<User>()).select("user_id,pay_points").eq("user_id", orders.get(0).getUserId()));
        UserExtend userExtend = userExtendService.getOne((new QueryWrapper<UserExtend>()).eq("user_id", orders.get(0).getUserId()));
        for (Order order : orders) {
            if (order.getIntegral() > 0) {
                accountLogs.add(AccountLog.builder().userId(order.getUserId()).payPoints(-order.getIntegral())
                        .changeTime(System.currentTimeMillis() / 1000).desc("下单消费").orderSn(order.getOrderSn())
                        .orderId(order.getOrderId()).build());
                user.setPayPoints(user.getPayPoints() - order.getIntegral());
            }
            //如果是已支付的订单生成发票，参考php
            if (order.getPayStatus() == OrderConstant.PAYED) {
                if (null != userExtend && !"不开发票".equals(userExtend.getInvoiceDesc())) {
                    invoices.add(Invoice.builder().orderId(order.getOrderId()).orderSn(order.getOrderSn()).userId(order.getUserId()).storeId(order.getStoreId())
                            .atime(System.currentTimeMillis() / 1000).ctime(System.currentTimeMillis() / 1000)
                            .invoiceMoney(order.getTotalAmount().subtract(order.getShippingPrice())).invoiceDesc("明细").taxpayer(order.getTaxpayer())
                            .invoiceTitle(order.getInvoiceTitle()).build());
                }
            }
        }
        if (accountLogs.size() > 0) {
            accountLogService.saveBatch(accountLogs);
            userService.updateById(user);
        }
        if (invoices.size() > 0) {
            invoiceService.saveBatch(invoices);
        }
    }

    @RabbitListener(queues = "pay_order.user")
    public void userMoneyPayOrders(List<Order> orders) {
        log.info("pay_order.user====>{}",orders);

        List<AccountLog> accountLogs = new ArrayList<>();
        List<Invoice> invoices = new ArrayList<>();
        User user = userService.getById(orders.get(0).getUserId());
        UserExtend userExtend = userExtendService.getOne((new QueryWrapper<UserExtend>()).eq("user_id", orders.get(0).getUserId()));
        for (Order order : orders) {
            Long time = System.currentTimeMillis() / 1000;
            if ("微信支付".equals(order.getPayName())){
                order.setUserMoney(order.getGoodsPrice());
            }
            accountLogs.add(AccountLog.builder().userId(order.getUserId()).userMoney(order.getUserMoney().negate())
                    .changeTime(time).desc("下单消费【"+order.getPayName() +"】").orderSn(order.getOrderSn())
                    .orderId(order.getOrderId()).build());

            // 根据商店ID 获取卖家信息
            Integer userId = sellerService.getUserByStoreId(order.getStoreId());
            accountLogs.add(AccountLog.builder().userId(userId).userMoney(order.getUserMoney().plus())
                    .changeTime(time).desc("出售收入【"+order.getPayName() +"】").orderSn(order.getOrderSn())
                    .orderId(order.getOrderId()).build());
            user.setUserMoney(user.getUserMoney().subtract(order.getUserMoney()));
            //如果是已支付的订单生成发票，参考php
            if (order.getPayStatus() == OrderConstant.PAYED) {
                if (null != userExtend && !"不开发票".equals(userExtend.getInvoiceDesc())) {
                    invoices.add(Invoice.builder().orderId(order.getOrderId()).orderSn(order.getOrderSn()).userId(order.getUserId()).storeId(order.getStoreId())
                            .atime(System.currentTimeMillis() / 1000).ctime(System.currentTimeMillis() / 1000)
                            .invoiceMoney(order.getTotalAmount().subtract(order.getShippingPrice())).invoiceDesc("明细").taxpayer(order.getTaxpayer())
                            .invoiceTitle(order.getInvoiceTitle()).build());
                }
            }
        }
        if (accountLogs.size() > 0) {
            accountLogService.saveBatch(accountLogs);
            userService.updateById(user);
        }
        if (invoices.size() > 0) {
            invoiceService.saveBatch(invoices);
        }
    }

    @RabbitListener(queues = "cancelled_order.user")
    public void cancelOrderAfter(Order order) {
        log.info("cancelled_order.user 取消订单==>{}", order);
        List<AccountLog> accountLogs = new ArrayList<>();
        Long time = System.currentTimeMillis() / 1000;
        cancelOrderMethod(order, accountLogs, time);
        if (accountLogs.size() > 0) {
            accountLogService.saveBatch(accountLogs);
        }

    }

    private void cancelOrderMethod(Order order, List<AccountLog> accountLogs, Long time) {
        log.info("支付订单取消，返还支付金额");
        if (order.getPayStatus() == OrderConstant.PAYED) {
            if (order.getUserMoney().compareTo(BigDecimal.ZERO) > 0 || order.getIntegral() > 0) {
                accountLogs.add(AccountLog.builder().userId(order.getUserId()).userMoney(order.getUserMoney().plus())
                        .payPoints(order.getIntegral()).changeTime(time).desc("支付订单取消，返还支付金额")
                        .orderSn(order.getOrderSn()).orderId(order.getOrderId()).build());

                // 根据商店ID 获取卖家信息
                Integer userId = sellerService.getUserByStoreId(order.getStoreId());
                accountLogs.add(AccountLog.builder().userId(userId).userMoney(order.getUserMoney().negate())
                        .payPoints(order.getIntegral()).changeTime(time).desc("支付订单取消，返还支付金额")
                        .orderSn(order.getOrderSn()).orderId(order.getOrderId()).build());
            }
        }
    }

    @RabbitListener(queues = "cancelled_order.user")
    public void cancelOrderAfter(List<Order> orders) {
        log.info("cancelled_order.user 取消订单==>{}", orders);
        List<AccountLog> accountLogs = new ArrayList<>();
        Long time = System.currentTimeMillis() / 1000;
        for (Order order : orders) {
            cancelOrderMethod(order, accountLogs, time);
        }

    }

    @RabbitListener(queues = "receive_order.user")
    public void receiveOrderAfter(Order order) {
        User user = userService.getById(order.getUserId());
        BigDecimal totalAmount = user.getTotalAmount().add(order.getOrderAmount().add(order.getUserMoney()));
        user.setTotalAmount(totalAmount);
        UserLevel userLevel = userLevelService.getOne((new QueryWrapper<UserLevel>()).le("amount", user.getTotalAmount())
                .orderByDesc("amount").last("LIMIT 1"));
        if (userLevel != null) {
            user.setLevel(userLevel.getLevelId());
            user.setDiscount(BigDecimal.valueOf(userLevel.getDiscount() / 100));
        }
        userService.updateById(user);
    }

    @RabbitListener(queues = "receive_order.user")
    public void receiveOrdersAfter(List<Order> orders) {
        Set<Integer> userIds = orders.stream().map(Order::getUserId).collect(Collectors.toSet());
        List<User> users = userService.list((new QueryWrapper<User>()).in("user_id", userIds));
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(User::getUserId, user -> user));
        for (Order order : orders) {
            if (userMap.containsKey(order.getUserId())) {
                User user = userMap.get(order.getUserId());
                user.setTotalAmount(user.getTotalAmount().add(order.getOrderAmount().add(order.getUserMoney())));
            }
        }
        List<UserLevel> userLevels = userLevelService.list((new QueryWrapper<UserLevel>())
                .orderByDesc("amount"));
        List<User> updateUserList = new ArrayList<>();
        for (Map.Entry<Integer, User> entry : userMap.entrySet()) {
            User user = entry.getValue();
            if (userLevels.size() > 0) {
                for (int i = 0; i < userLevels.size(); i++) {
                    if (i == 0) {
                        //初始等级
                        user.setLevel(userLevels.get(i).getLevelId());
                        user.setDiscount(BigDecimal.valueOf(userLevels.get(i).getDiscount() / 100));
                    }
                    if (user.getTotalAmount().compareTo(userLevels.get(i).getAmount()) >= 0) {
                        //打怪升级
                        user.setLevel(userLevels.get(i).getLevelId());
                        user.setDiscount(BigDecimal.valueOf(userLevels.get(i).getDiscount() / 100));
                    }
                    if (user.getTotalAmount().compareTo(userLevels.get(i).getAmount()) < 0) {
                        //打不过了，退出
                        break;
                    }
                }
            }
            updateUserList.add(user);
        }
        if (updateUserList.size() > 0) {
            userService.updateBatchById(updateUserList);
        }
    }

    @RabbitListener(queues = "confirm_rebate.user")
    public void confirmRebateAfter(List<RebateLog> rebateLogs) {
        Set<Integer> userIds = new HashSet<>();
        List<AccountLog> accountLogs = new ArrayList<>();
        for (RebateLog rebateLog : rebateLogs) {
            accountLogs.add(AccountLog.builder().userId(rebateLog.getUserId()).userMoney(rebateLog.getMoney())
                    .payPoints(0).changeTime(System.currentTimeMillis() / 1000).desc("订单:" + rebateLog.getOrderSn() + "分佣")
                    .orderSn(rebateLog.getOrderSn())
                    .orderId(rebateLog.getOrderId()).build());
            userIds.add(rebateLog.getUserId());
        }
        Map<Integer, User> userMap = userService.list(new QueryWrapper<User>().in("user_id", userIds)).stream()
                .collect(Collectors.toMap(User::getUserId, item -> item));
        List<DistributLevel> distributLevels = orderService.distributLevelList().stream()
                .sorted(Comparator.comparing(DistributLevel::getOrderMoney)).collect(Collectors.toList());
        if (accountLogs.size() > 0) {
            accountLogService.saveBatch(accountLogs);
            for (AccountLog accountLog : accountLogs) {
                if (userMap.containsKey(accountLog.getUserId())) {
                    User user = userMap.get(accountLog.getUserId());
                    user.setDistributMoney(user.getDistributMoney().add(accountLog.getUserMoney()));
                    for (DistributLevel distributLevel : distributLevels) {
                        if (user.getDistributMoney().compareTo(distributLevel.getOrderMoney()) > 0) {
                            user.setDistributLevel(distributLevel.getLevelId());
                        }
                    }

                }
            }
            userService.updateBatchById(userMap.values());
        }

    }
}
