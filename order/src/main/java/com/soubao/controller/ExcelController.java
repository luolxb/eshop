package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.Order;
import com.soubao.service.OrderService;
import com.soubao.service.MallService;
import com.soubao.service.SellerService;
import com.soubao.common.utils.TimeUtil;
import com.soubao.utils.excel.ExcelUtil;
import com.soubao.vo.OrderExcel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ExcelController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private MallService mallService;

    @GetMapping("export")
    public void export(@RequestParam(value = "consignee", required = false) String consignee,
                       @RequestParam(value = "store_name", required = false) String storeName,
                       @RequestParam(value = "order_sn", required = false) String orderSn,
                       @RequestParam(value = "add_time_begin", required = false) Long addTimeBegin,
                       @RequestParam(value = "add_time_end", required = false) Long addTimeEnd,
                       @RequestParam(value = "pay_status", required = false) Set<Integer> payStatusSet,
                       @RequestParam(value = "pay_code", required = false) Set<String> payCodeSet,
                       @RequestParam(value = "shipping_status", required = false) Set<Integer> shippingStatusSet,
                       @RequestParam(value = "order_status", required = false) Set<Integer> orderStatusSet,
                       @RequestParam(value = "store_id", required = false) Integer storeId,
                       @RequestParam(value = "user_id", required = false) Integer userId,
                       @RequestParam(value = "order_ids", required = false) Set<Integer> orderIds,
                       @RequestParam(value = "prom_type", required = false) Set<Integer> promType,
                       @RequestParam(value = "team_id", required = false) Integer teamId,
                       @RequestParam(value = "found_id", required = false) Integer foundId,
                       HttpServletResponse response) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        if (orderIds != null && !orderIds.isEmpty()) {
            wrapper.in("o.order_id", orderIds);
        } else {
            //店铺id 用户id 店铺名称只会同时出现一个
            if (storeId != null) {
                wrapper.eq("o.store_id", storeId);
            } else if (userId != null) {
                wrapper.eq("o.user_id", userId);
            } else if (!StringUtils.isEmpty(storeName)) {
                Set<Integer> storeIds = sellerService.storeIdsByStoreName(storeName);
                if(storeIds.size() > 0){
                    wrapper.in("o.store_id", storeIds);
                }else{
                    wrapper.eq("o.store_id", 0);
                }
            }
            if (!StringUtils.isEmpty(consignee)) {
                wrapper.like("o.consignee", consignee);
            }
            if (!StringUtils.isEmpty(orderSn)) {
                wrapper.eq("o.order_sn", orderSn);
            }
            if (addTimeBegin != null) {
                wrapper.ge("o.add_time", addTimeBegin);
            }
            if (addTimeEnd != null) {
                wrapper.lt("o.add_time", addTimeEnd);
            }
            if (payStatusSet != null && !payStatusSet.isEmpty()) {
                wrapper.in("o.pay_status", payStatusSet);
            }
            if (payCodeSet != null && !payCodeSet.isEmpty()) {
                wrapper.in("o.pay_code", payCodeSet);
            }
            if (shippingStatusSet != null && !shippingStatusSet.isEmpty()) {
                wrapper.in("o.shipping_status", shippingStatusSet);
            }
            if (orderStatusSet != null && !orderStatusSet.isEmpty()) {
                wrapper.in("o.order_status", orderStatusSet);
            }
            if (promType != null && !promType.isEmpty()) {
                wrapper.in("o.prom_type", promType);
            } else {
                wrapper.ne("o.prom_type", 6);//默认不查询拼团订单
            }
            Set<Integer> orderIds2 = new HashSet<>();
            if (teamId != null) {
                orderIds2.addAll(mallService.getOrderIdsByTeamIdOrFoundId(teamId, null));
            }
            if (foundId != null) {
                orderIds2.addAll(mallService.getOrderIdsByTeamIdOrFoundId(null, foundId));
            }
            if (!orderIds2.isEmpty()) {
                wrapper.in("o.order_id", orderIds2);
            }
        }
        wrapper.apply("1=1");
        wrapper.groupBy("o.order_id");
        wrapper.orderByDesc("o.order_id");
        List<OrderExcel> orderExcelList = orderService.getOrderExportData(wrapper);
        orderService.withExcelRegions(orderExcelList);
        String fileName = "订单_" + TimeUtil.transForDateStr(System.currentTimeMillis() / 1000, "yyyy-MM-dd HH.mm.ss");
        ExcelUtil.writeExcel(response, orderExcelList, fileName, fileName, new OrderExcel());
    }

}
