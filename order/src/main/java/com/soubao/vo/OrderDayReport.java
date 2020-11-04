package com.soubao.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDayReport {
    private String days;//日期
    private Integer orderCount;//新增订单个数
    private BigDecimal totalAmount;//日期总额
    private BigDecimal shippingPrice;//日期物流费
    private BigDecimal goodsPrice;//日期商品总价
    private BigDecimal couponPrice;//日期优惠券金额
    private BigDecimal orderPromAmount;//日期订单优惠
    private BigDecimal costPrice;//日期成本价
}
