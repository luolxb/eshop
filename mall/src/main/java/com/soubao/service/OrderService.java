package com.soubao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.*;
import com.soubao.common.vo.SBApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(name = "order")
public interface OrderService {

    @GetMapping("/user")
    Order getUserOrder(@RequestParam(value = "order_sn") String orderSn);

    @PutMapping("/team/order_status/3")
    String cancelTeamOrder(@RequestParam(value = "order_id") Set<Integer> orderIds);

    @PutMapping("/order_status/1")
    String confirm(@RequestBody Map<String, Object> requestOrderAction);

    @PostMapping("/order")
    Order addAndGetMasterOrder(@RequestBody Order order);

    @PutMapping("pay/master_order")
    String updateMasterOrder(Order masterOrder);

    @GetMapping("order_goods/list")
    List<OrderGoods> getOrderGoodsListByOrderIds(@RequestParam(value = "order_ids") Set<Integer> orderIds);

    @GetMapping("order_goods/list")
    List<OrderGoods> getOrderGoodsListByOrderId(@RequestParam(value = "order_id") Integer orderId);

    @GetMapping("/order_list")
    List<Order> getOrderListByIds(@RequestParam(value = "order_ids") Set<Integer> orderIds);

    @GetMapping("/order/user_id")
    Order getOrderAndStoreByUserIds(@RequestParam(value = "user_id") Integer userId,
                                    @RequestParam(value = "goods_id") Integer goodsId);

    @GetMapping("/order_list/seller")
    List<Order> getSellerOrderListByIds(@RequestParam(value = "order_ids") Set<Integer> orderIds);

    @GetMapping("/page")
    Page<Order> getTeamOrderPage(@RequestParam Map<String, Object> orderMap,
                                 @RequestParam(value = "p") Integer page,
                                 @RequestParam(value = "size") Integer size);

    @GetMapping("/order_goods/goods_count")
    Integer getOrderGoodsCountGoods(@RequestParam(value = "goods_id") Integer goodsId);

    @DeleteMapping("/comments")
    SBApi deleteCommentsByGoodsId(@RequestParam(value = "goods_id") Integer goodsId);

    @GetMapping("/delivery_info")
    Order getOrder(@RequestParam("order_id") Integer orderId);

    @GetMapping("/team/success")
    SBApi teamOrderSuccess(@RequestParam(value = "order_id") Integer orderId);

    @GetMapping("team/order_list")
    List<Order> teamOrderList(@RequestParam("team_id") Integer teamId);

    @GetMapping("/miniapp_config")
    MiniappConfig getMiniAppConfig();

    /**
     * 根据商店ID获取订单
     * @param storeId
     * @return
     */
    @GetMapping("/order/list/store_id")
    List<Order> getOrderListByStoreId(@RequestParam("store_id") Integer storeId);

    @GetMapping("/order/list/user_id/store_id")
    List<Order> getOrderListByUserId(@RequestParam("user_id") Integer userId,
                                     @RequestParam("store_id") Integer storeId);
}
