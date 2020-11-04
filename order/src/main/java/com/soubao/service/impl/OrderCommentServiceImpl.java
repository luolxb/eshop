package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.OrderCommentMapper;
import com.soubao.dao.OrderGoodsMapper;
import com.soubao.entity.Order;
import com.soubao.entity.OrderComment;
import com.soubao.entity.Store;
import com.soubao.entity.User;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.OrderCommentService;
import com.soubao.service.OrderService;
import com.soubao.service.SellerService;
import com.soubao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * 订单评分表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-12
 */
@Service("orderCommentService")
@Slf4j
public class OrderCommentServiceImpl extends ServiceImpl<OrderCommentMapper, OrderComment> implements OrderCommentService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private UserService userService;

    @Override
    public void orderAddComment(Order order, OrderComment orderComment) {
        if (order == null) {
            throw new ShopException(ResultEnum.NO_FIND_ORDER);
        }
        OrderComment exOrderComment = getOne((new QueryWrapper<OrderComment>())
                .eq("order_id", order.getOrderId()).eq("deleted", 0));
        if (exOrderComment != null) {
            throw new ShopException(ResultEnum.ORDER_HAVE_COMMENT);
        }
        orderComment.setStoreId(order.getStoreId());
        orderComment.setCommentTime(System.currentTimeMillis() / 1000);
        save(orderComment);
        Store store = new Store();
        store.setStoreId(order.getStoreId());
        sellerService.updateById(getStoreScore(store));//更新店铺评分
        order.setIsComment(1);
        orderService.updateById(order);
    }

    @Override
    public Store getStoreScore(Store store) {
        return ((OrderCommentMapper)baseMapper).selectStoreScore(store);
    }

    @Override
    public void withUser(List<OrderComment> records) {
        if (records.size() > 0) {
            Set<Integer> userIds = records.stream().map(OrderComment::getUserId).collect(Collectors.toSet());
            Map<Integer, User> userMap = userService.usersByIds(userIds).stream().collect(Collectors.toMap(User::getUserId, user -> user));
            records.forEach(orderComment -> {
                orderComment.setNickname(userMap.get(orderComment.getUserId()).getNickname());
            });
        }
    }

    @Override
    public void withStore(List<OrderComment> records) {
        if (records.size() > 0) {
            Set<Integer> storeIds = records.stream().map(OrderComment::getStoreId).collect(Collectors.toSet());
            Map<Integer, Store> storeMap = sellerService.getStoresById(storeIds).stream().collect(Collectors.toMap(Store::getStoreId, store -> store));
            records.forEach(orderComment -> {
                if (storeMap.containsKey(orderComment.getStoreId())) {
                    orderComment.setStoreName(storeMap.get(orderComment.getStoreId()).getStoreName());
                }
            });
        }
    }

}
