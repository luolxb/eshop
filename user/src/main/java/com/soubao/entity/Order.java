package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;
    private String orderSn;

    private Integer userId;

    private String masterOrderSn;

    private Integer orderStatus;

    private Integer payStatus;

    private Integer shippingStatus;

    private String consignee;
    private Integer country;
    private Integer province;

    private Integer city;

    private Integer district;

    private Integer twon;

    private String address;

    private String zipcode;

    private String mobile;

    private String email;

    private String shippingCode;
    private String shippingName;

    private BigDecimal shippingPrice;
    private Long shippingTime;

    private String payCode;

    private String payName;

    private String invoiceTitle;

    private String taxpayer;

    private BigDecimal goodsPrice;

    private BigDecimal userMoney;
    private BigDecimal couponPrice;

    private Integer integral;
    private BigDecimal integralMoney;
    private BigDecimal orderAmount;

    private BigDecimal totalAmount;

    private BigDecimal paidMoney;
    private Long addTime;

    private Long confirmTime;
    private Long payTime;

    private String transactionId;

    private Integer promId;

    private Integer promType;

    private Integer orderPromId;

    private BigDecimal orderPromAmount;

    private BigDecimal discount;

    private String userNote;

    private String adminNote;
    private String parentSn;

    private Integer storeId;
    private Integer orderStoreId;

    private Integer isComment;
    private Integer shopId;
    private Integer deleted;

    private Integer orderStatisId;

    @TableField(exist = false)
    private List<Order> orderList;

    @TableField(exist = false)
    private Integer GoodsNum;

    @TableField(exist = false)
    private BigDecimal cutFee;

    @TableField(exist = false)
    private String payPwd;

    @TableField(exist = false)
    private Map<Integer, String> userNotes;

    private static final long serialVersionUID = 1L;//mq要用的
}