package com.soubao.service;

import com.soubao.entity.DeliveryDoc;
import com.soubao.entity.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(name = "order")
public interface OrderService {

    @GetMapping("user/order_sum")
    Map<Integer, BigDecimal> getUserOrderSumByUserIds(@RequestParam("user_ids") Set<Integer> userIds);

    @GetMapping("/order_list/seller")
    List<Order> orderList(@RequestParam(value = "order_ids", required = false) Set<Integer> orderIds);

    @GetMapping("delivery_doc/list/ids")
    List<DeliveryDoc> deliveryDocByOrderIds(@RequestParam(value = "order_ids", required = false) Set<Integer> ids);
}
