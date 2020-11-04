package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.CouponList;
import com.soubao.entity.Order;
import com.soubao.entity.User;
import com.soubao.service.CouponListService;
import com.soubao.dao.CouponListMapper;
import com.soubao.service.OrderService;
import com.soubao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-09-02
 */
@Service("couponListServiceImpl")
public class CouponListServiceImpl extends ServiceImpl<CouponListMapper, CouponList> implements CouponListService {

    @Autowired
    private CouponListMapper couponListMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @Override
    public void deductionMasterOrder(Order preMasterOrder, Order masterOrder) {
        for (int orderIndex = 0; orderIndex < preMasterOrder.getOrderList().size(); orderIndex++) {
            if (preMasterOrder.getOrderList().get(orderIndex).getCouponListId() != null) {
                update((new UpdateWrapper<CouponList>()).set("order_id", masterOrder.getOrderList().get(orderIndex).getOrderId())
                        .set("use_time", System.currentTimeMillis() / 1000).set("status", 1)
                        .in("id", preMasterOrder.getOrderList().get(orderIndex).getCouponListId()));
            }
        }
    }

    @Override
    public IPage<CouponList> getCouponListPage(Page<CouponList> page, QueryWrapper<CouponList> queryWrapper) {
        return couponListMapper.getCouponListPage(page, queryWrapper);
    }

    @Override
    public void withSource(List<CouponList> records) {
        Set<Integer> orderIds = records.stream().map(CouponList::getOrderId).collect(Collectors.toSet());
        Set<Integer> userIds = records.stream().map(CouponList::getUid).collect(Collectors.toSet());

        if (!userIds.isEmpty()) {
            Map<Integer, User> userMap = userService.listByIds(userIds).stream().collect(Collectors.toMap(User::getUserId, user -> user));
            records.forEach(couponList -> {
                couponList.setUsername(userMap.get(couponList.getUid()).getNickname());
            });
        }
        if (!orderIds.isEmpty()) {
            Map<Integer, Order> orderMap = orderService.getSellerOrderListByIds(orderIds).stream().collect(Collectors.toMap(Order::getOrderId, order -> order));
            records.forEach(couponList -> {
                if (orderMap.containsKey(couponList.getOrderId())) {
                    couponList.setOrderSn(orderMap.get(couponList.getOrderId()).getOrderSn());
                }
            });
        }
    }
}
