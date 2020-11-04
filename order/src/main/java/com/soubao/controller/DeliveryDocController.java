package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.DeliveryDoc;
import com.soubao.entity.User;
import com.soubao.service.DeliveryDocService;
import com.soubao.service.impl.AuthenticationFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 发货单 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2019-10-14
 */
@Api(value = "发货单控制器", tags = {"发货单相关接口"})
@RestController
@RequestMapping("delivery_doc")
public class DeliveryDocController {
    @Autowired
    private DeliveryDocService deliveryDocService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/list/admin")
    public List<DeliveryDoc> deliveryDocs(@RequestParam(value = "order_id") Integer orderId) {
        return deliveryDocService.list(new QueryWrapper<DeliveryDoc>().eq("order_id", orderId));
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("/list/seller")
    public List<DeliveryDoc> deliveryDocs(@RequestParam(value = "order_id") String orderId,
                                          @RequestParam(value = "is_seller", defaultValue = "0") Integer isSeller) {
        List<DeliveryDoc> deliveryDocs = deliveryDocService.list(new QueryWrapper<DeliveryDoc>().eq("order_id", orderId));
        if(!deliveryDocs.isEmpty() && isSeller == 1){
            deliveryDocService.withRegions(deliveryDocs);
            deliveryDocService.withSeller(deliveryDocs);
        }
        return deliveryDocs;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("用户发货单")
    @GetMapping("/list/user")
    public List<DeliveryDoc> deliveryDoc(@RequestParam(value = "pick_order_id") Integer pickOrderId) {
        User user = authenticationFacade.getPrincipal(User.class);
        List<DeliveryDoc> deliveryDocs = deliveryDocService.list(new QueryWrapper<DeliveryDoc>().eq("order_id", pickOrderId)
                .eq("user_id", user.getUserId()));
        deliveryDocService.withRegions(deliveryDocs);
        return deliveryDocs;
    }

    @ApiOperation("获取第三方物流信息")
    @GetMapping("express")
    public Object express(
            @RequestParam(value = "shipping_code") String shippingCode,
            @RequestParam(value = "invoice_no") String invoiceNo,
            @RequestParam(value = "mobile", required = false) String mobile) {
        DeliveryDoc deliveryDoc = new DeliveryDoc();
        deliveryDoc.setShippingCode(shippingCode);
        deliveryDoc.setInvoiceNo(invoiceNo);
        deliveryDoc.setMobile(mobile);
        return deliveryDocService.getExpress(deliveryDoc);
    }

    @GetMapping("list/ids")
    public List<DeliveryDoc> deliveryDocList(@RequestParam(value = "order_ids", required = false) Set<Integer> ids,
                                             @RequestParam(value = "order_sn", required = false) Set<String> orderSn) {
        QueryWrapper<DeliveryDoc> wrapper = new QueryWrapper<>();
        if (null != ids && ids.size() > 0) {
            wrapper.in("order_id", ids);
        }
        if (null != orderSn && orderSn.size() > 0) {
            wrapper.in("order_sn", orderSn);
        }
        List<DeliveryDoc> deliveryDocList = deliveryDocService.list(wrapper);
        deliveryDocService.withRegions(deliveryDocList);
        return deliveryDocList;
    }

}
