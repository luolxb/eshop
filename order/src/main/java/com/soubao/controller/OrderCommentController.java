package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Order;
import com.soubao.entity.OrderComment;
import com.soubao.entity.Store;
import com.soubao.entity.User;
import com.soubao.service.OrderCommentService;
import com.soubao.service.OrderService;
import com.soubao.service.SellerService;
import com.soubao.service.UserService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order_comment")
public class OrderCommentController {
    @Autowired
    private OrderCommentService orderCommentService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private UserService userService;
    @Autowired
    private SellerService sellerService;

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("订单评价")
    @PostMapping
    public SBApi addOrderComment(@RequestBody OrderComment requestOrderComment) {
        User user = authenticationFacade.getPrincipal(User.class);
        requestOrderComment.setUserId(user.getUserId());
        Order order = orderService.getOne((new QueryWrapper<Order>()).eq("order_id", requestOrderComment.getOrderId())
                .eq("user_id", requestOrderComment.getUserId()));
        orderCommentService.orderAddComment(order, requestOrderComment);
        return new SBApi();
    }

    @GetMapping
    public OrderComment getOrderComment(@RequestParam("order_id") Integer orderId) {
        return orderCommentService.getOne(new QueryWrapper<OrderComment>().eq("order_id", orderId));
    }

    @GetMapping("/page/store_satisfaction")
    public IPage<OrderComment> storeSatisfactionPage(@RequestParam(value = "nickname", required = false) String nickname,
                                                     @RequestParam(value = "store_name", required = false) String storeName,
                                                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<OrderComment> orderCommentQueryWrapper = new QueryWrapper<>();
        orderCommentQueryWrapper.orderByDesc("order_commemt_id");
        IPage<OrderComment> orderCommentIPage = new Page<>();
        if(null != nickname){
            Set<Integer> uIds = userService.userIdsByName(nickname);
            if(uIds.size() > 0){
                orderCommentQueryWrapper.in("user_id", uIds);
            }else{
                return orderCommentIPage;
            }
        }
        if(null != storeName){
            Set<Integer> sIds = sellerService.storeIdsByStoreName(storeName);
            if(sIds.size() > 0){
                orderCommentQueryWrapper.in("store_id", sIds);
            }else{
                return orderCommentIPage;
            }
        }
        orderCommentIPage = orderCommentService.page(new Page<>(page, size), orderCommentQueryWrapper);
        orderCommentService.withUser(orderCommentIPage.getRecords());
        orderCommentService.withStore(orderCommentIPage.getRecords());
        return orderCommentIPage;
    }

    @GetMapping("page/store_score")
    public IPage<OrderComment> storeScorePage(
            @RequestParam(value = "store_name", required = false) String storeName,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<OrderComment> orderCommentQueryWrapper = new QueryWrapper<>();
        IPage<OrderComment> orderCommentIPage = new Page<>();
        if (null != storeName) {
            Set<Integer> sIds = sellerService.storeIdsByStoreName(storeName);
            if(sIds.size() > 0){
                orderCommentQueryWrapper.in("store_id", sIds);
            }else{
                return orderCommentIPage;
            }
        }
        orderCommentQueryWrapper.select(" store_id,COUNT(1) AS comment_count,AVG(describe_score) AS describe_score," +
                "AVG(seller_score) AS seller_score,AVG(logistics_score) AS logistics_score")
                .orderByDesc("store_id").groupBy("store_id");
        orderCommentIPage = orderCommentService.page(new Page<>(page, size), orderCommentQueryWrapper);
        orderCommentService.withStore(orderCommentIPage.getRecords());
        return orderCommentIPage;
    }
}

