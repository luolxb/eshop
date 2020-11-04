package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.*;
import com.soubao.service.GoodsService;
import com.soubao.service.OrderService;
import com.soubao.service.SellerService;
import com.soubao.service.UserService;
import com.soubao.service.impl.AuthenticationFacade;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 散户/在商城购买，出售商品的人，没有商店
 */
@RestController
@RequestMapping("/retail")
public class RetailController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UserService userService;
    @Autowired
    private SellerService sellerService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private OrderService orderService;

    /**
     * 根据散户的ID获取拥有/发布的商品
     */
    @ApiOperation(value = "获取散户发布的商品", notes = "获取分页记录", httpMethod = "GET")
    @GetMapping("goods/page/depositCertificate")
    public IPage<Goods> goodsPage(
            @ApiParam("是否在卖") @RequestParam(value = "is_on_sale", required = false, defaultValue = "1") Integer isOnSale,
            @ApiParam("散户id") @RequestParam(value = "user_id", required = false) Integer userId,
            @ApiParam("店铺ID") @RequestParam(value = "store_id", required = false) Integer storeId,
            @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
            @ApiParam("二级分类") @RequestParam(value = "cat_id2") Integer catId2,
            @ApiParam("三级分类") @RequestParam(value = "cat_id3", required = false) Integer catId3,
            @ApiParam("排序字段") @RequestParam(value = "sort_type", defaultValue = "1") Integer sortType,
            @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "20") Integer size) {
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!"anonymousUser".equals(principal)) {
            User user = authenticationFacade.getPrincipal(User.class);
            wrapper.ne("owner_id", user.getUserId());
        }
        if (null != isOnSale) {
            wrapper.eq("is_on_sale", isOnSale);
        }
        if (sortType == 1) {
            wrapper.orderByAsc("shop_price");
        } else {
            wrapper.orderByDesc("shop_price");
        }

        Store store = null;
        if (userId != null) {
            store = sellerService.getUserSellerStore(userId);
            if (store == null) {
                wrapper.eq("store_id", 0);
            } else {
                wrapper.eq("store_id", store.getStoreId());
            }
        } else if (storeId != null) {
            wrapper.eq("store_id", storeId);

        } else {
            List<Store> storeList = sellerService.getUserSellerStoreList();
            // 查询所有散户的商店
            if (!CollectionUtils.isEmpty(storeList)) {
                wrapper.in("store_id", storeList.stream().map(Store::getStoreId).collect(Collectors.toList()));
            } else {
                wrapper.in("store_id", 0);
            }
        }

        //只显示通证的
        wrapper.eq("cat_id1", 188);
        // 酒类，艺术品，纪念币
        wrapper.eq("cat_id2", catId2);
        if (catId3 != null) {
            // 酒类[茅台、五粮液，江小白...]
            wrapper.eq("cat_id3", catId3);
        }
        wrapper.eq("delete_flag",0);

        IPage<Goods> goodsPage = goodsService.page(new Page<>(page, size), wrapper);
        // 获取存证归属人
        for (Goods goods : goodsPage.getRecords()) {
            if (store != null) {
                // 获取当前商店出售成功的订单
                List<Order> orderAllList = orderService.getOrderListByUserId(userId, store.getStoreId());
                List<Order> orderList = orderService.getOrderListByStoreId(store.getStoreId());
                DecimalFormat df = new DecimalFormat("0.##");
                double o = (double) orderList.size() / (double) orderAllList.size();
                store.setTurnoverRate(Double.parseDouble(df.format(o * 100)));
                store.setTransactionNum(orderAllList.size());
            }
            goods.setStore(store);
            Set<Integer> integers = Arrays.asList(goods.getOwnerId()).stream().collect(Collectors.toSet());
            List<User> users = userService.listByIds(integers);
            if (!CollectionUtils.isEmpty(users)) {
                goods.setUser(users.get(0));
            }
        }

        return goodsPage;
    }
}
