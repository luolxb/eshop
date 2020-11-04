package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.common.constant.OrderConstant;
import com.soubao.dao.OrderActionMapper;
import com.soubao.entity.Order;
import com.soubao.entity.OrderAction;
import com.soubao.entity.Seller;
import com.soubao.entity.User;
import com.soubao.service.OrderActionService;
import com.soubao.service.SellerService;
import com.soubao.service.UserService;
import com.soubao.common.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-09-25
 */
@Service("orderActionService")
public class OrderActionServiceImpl extends ServiceImpl<OrderActionMapper, OrderAction> implements OrderActionService {
    @Autowired
    private UserService userService;
    @Autowired
    private SellerService sellerService;

    @Override
    public OrderAction getOrderActionByPlaceOrder(Order order) {
        OrderAction orderAction = new OrderAction();
        orderAction.setOrderId(order.getOrderId());
        orderAction.setActionUser(order.getUserId());
        orderAction.setUserType(OrderConstant.CUSTOMER);
        orderAction.setActionNote("您提交了订单，请等待系统确认");
        orderAction.setStatusDesc("提交订单");
        orderAction.setLogTime(System.currentTimeMillis() / 1000);
        orderAction.setStoreId(order.getStoreId());
        return orderAction;
    }

    @Override
    public OrderAction getOrderActionByPayOrder(Order order) {
        OrderAction payOrderAction = new OrderAction();
        payOrderAction.setOrderId(order.getOrderId());
        payOrderAction.setActionUser(order.getUserId());
        payOrderAction.setUserType(OrderConstant.CUSTOMER);
        payOrderAction.setOrderStatus(order.getOrderStatus());
        payOrderAction.setShippingStatus(order.getShippingStatus());
        payOrderAction.setPayStatus(order.getPayStatus());
        payOrderAction.setActionNote("订单付款成功");
        payOrderAction.setStatusDesc("付款成功");
        payOrderAction.setLogTime(System.currentTimeMillis() / 1000);
        payOrderAction.setStoreId(order.getStoreId());
        return payOrderAction;
    }

    @Override
    public OrderAction getOrderActionByReceiveOrder(Order order) {
        OrderAction payOrderAction = new OrderAction();
        payOrderAction.setOrderId(order.getOrderId());
        payOrderAction.setActionUser(order.getUserId());
        payOrderAction.setUserType(OrderConstant.CUSTOMER);
        payOrderAction.setOrderStatus(order.getOrderStatus());
        payOrderAction.setShippingStatus(order.getShippingStatus());
        payOrderAction.setPayStatus(order.getPayStatus());
        payOrderAction.setActionNote("订单已确认收货");
        payOrderAction.setStatusDesc("订单已确认收货");
        payOrderAction.setLogTime(System.currentTimeMillis() / 1000);
        payOrderAction.setStoreId(order.getStoreId());
        return payOrderAction;
    }


    @Override
    public boolean addOrderActionLog(Order order, String statusDesc, String actionNote, Integer actionUser, Integer userType, Integer storeId) {
        if (StringUtils.isEmpty(actionNote)){
            actionNote = "";
        }
        if (actionUser == null){
            actionUser = 0;
        }
        if (userType == null){
            userType = 0;
        }
        OrderAction orderAction = new OrderAction();
        orderAction.setOrderId(order.getOrderId());
        orderAction.setActionUser(actionUser);
        orderAction.setStoreId(storeId);
        orderAction.setUserType(userType);
        orderAction.setActionNote(actionNote);
        orderAction.setOrderStatus(order.getOrderStatus());
        orderAction.setPayStatus(order.getPayStatus());
        orderAction.setShippingStatus(order.getShippingStatus());
        orderAction.setLogTime(System.currentTimeMillis() / 1000);
        orderAction.setStatusDesc(statusDesc);
        return save(orderAction);
    }

    @Override
    public void withUser(List<OrderAction> records){
        Set<Integer> sellerIds = new HashSet<>();
        Set<Integer> userIds = new HashSet<>();
        for (OrderAction oa : records) {
            Integer userType = oa.getUserType();
            if (userType == 1) {
                sellerIds.add(oa.getActionUser());
            } else if (userType == 2) {
                userIds.add(oa.getActionUser());
            }
        }
        List<Seller> sellers;
        List<User> users;
        Map<Integer, Seller> sellerMap = new HashMap<>();
        Map<Integer, User> userMap = new HashMap<>();
        if (!sellerIds.isEmpty()){
            sellers = sellerService.sellers(sellerIds);
            sellerMap = sellers.stream().collect(Collectors.toMap(Seller::getSellerId, seller -> seller));
        }
        if (!userIds.isEmpty()){
            users = userService.usersByIds(userIds);
            userMap = users.stream().collect(Collectors.toMap(User::getUserId, user -> user));
        }
        for (OrderAction oa : records) {
            Integer userType = oa.getUserType();
            if (userType == 1) {
                if (sellerMap.containsKey(oa.getActionUser())) {
                    oa.setActionUserDesc("商家(" + sellerMap.get(oa.getActionUser()).getSellerName() + ")");
                }
            } else if (userType == 2) {
                if (userMap.containsKey(oa.getActionUser())) {
                    oa.setActionUserDesc("用户(" + userMap.get(oa.getActionUser()).getNickname() + ")");
                }
            } else {
                oa.setActionUserDesc("平台管理员");
            }
        }
    }
}
