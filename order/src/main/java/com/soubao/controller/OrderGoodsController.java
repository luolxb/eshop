package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.OrderGoods;
import com.soubao.entity.User;
import com.soubao.service.OrderGoodsService;
import com.soubao.service.impl.AuthenticationFacade;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/order_goods")
public class OrderGoodsController {

    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @ApiOperation("订单商品评价列表")
    @GetMapping("/comment/page")
    public Page<OrderGoods> commentPage(
            @RequestParam(value = "is_comment", required = false) Integer isComment,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        IPage<OrderGoods> userOrderGoodsCommentPage = orderGoodsService.getUserOrderGoodsCommentPage(new Page<>(page, size), user.getUserId(), isComment);
        orderGoodsService.withStore(userOrderGoodsCommentPage.getRecords());
        return (Page<OrderGoods>) userOrderGoodsCommentPage;
    }

    @ApiOperation("获取订单商品")
    @GetMapping
    public OrderGoods getOrderGoods(
            @ApiParam("订单商品主键") @RequestParam(value = "rec_id", required = false) Integer recId,
            @RequestParam(value = "order_id", required = false) Integer orderId,
            @RequestParam(value = "prom_id", required = false) Integer promId,
            @RequestParam(value = "prom_type", required = false) Integer promType) {
        QueryWrapper<OrderGoods> queryWrapper = new QueryWrapper<>();
        if (recId != null) {
            queryWrapper.eq("rec_id", recId);
        }
        if (orderId != null) {
            queryWrapper.eq("order_id", orderId);
        }
        if (promId != null && promType != null) {
            queryWrapper.eq("prom_id", promId).eq("prom_type", promType);
        }
        return orderGoodsService.getOne(queryWrapper);
    }

    @GetMapping("list")
    public List<OrderGoods> getOrderGoodsList(
            @RequestParam(value = "order_ids", required = false) Set<Integer> orderIds,
            @RequestParam(value = "order_id", required = false) Integer orderId) {
        QueryWrapper<OrderGoods> orderGoodsQueryWrapper = new QueryWrapper<>();
        if (orderIds != null) {
            orderGoodsQueryWrapper.in("order_id", orderIds);
        }
        if (orderId != null) {
            orderGoodsQueryWrapper.eq("order_id", orderId);
        }
        return orderGoodsService.list(orderGoodsQueryWrapper);
    }

    @GetMapping("goods_count")
    public Integer getOrderGoodsCount(@RequestParam(value = "goods_id", required = false) Integer goodsId) {
        return orderGoodsService.count((new QueryWrapper<OrderGoods>()).eq("goods_id", goodsId));
    }

}
