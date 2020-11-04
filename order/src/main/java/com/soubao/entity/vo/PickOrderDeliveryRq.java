package com.soubao.entity.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 提货订单实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class PickOrderDeliveryRq {
    /**
     * 提货订单id
     */
    @ApiModelProperty("提货订单id")
    private Integer pickOrderId;


    /**
     * 购买订单id
     */
    @ApiModelProperty("购买订单id")
    private Integer orderId;
    /**
     * 申请提货时间
     */
    @ApiModelProperty("申请提货时间")
    private Long addTime;

    /**
     * 发货人
     */
    @ApiModelProperty("发货人")
    private Integer sellerId;

    /**
     * 提货订单编码
     */
    @ApiModelProperty("提货订单编码")
    private String pickOrderSn;

    /**
     * 收货人id
     */
    @ApiModelProperty("收货人id")
    private String consigneeId;

    /**
     * 物流code
     */
    @ApiModelProperty("物流code")
    private String shippingCode;

    /**
     * 物流名称
     */
    @ApiModelProperty("物流名称")
    private String shippingName;
    /**
     * 邮费
     */
    @ApiModelProperty("邮费")
    private BigDecimal shippingPrice;

    /**
     * 物流最新时间
     */
    @ApiModelProperty("物流最新时间")
    private Integer shippingTime;


    /**
     * 提货状态时间
     */
    @ApiModelProperty("提货状态时间")
    private Integer updateTime;

    /**
     * 提货类型
     * 0. 手工单号
     * 1. 预约到店
     * 2. 电子面单
     * 3. 无需物流
     */
    @ApiModelProperty("提货类型 0. 手工单号 ,1. 预约到店 ,2. 电子面单,3. 无需物流")
    private Integer pickOrderType;

}
