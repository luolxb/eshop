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
public class PickOrderRq {

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
     * 提货人
     */
    @ApiModelProperty("提货人")
    private Integer userId;

    /**
     * 提货人
     */
    @ApiModelProperty("提货人")
    @NotBlank(message = "提货人不能为空")
    private String nickname;
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
     * 国家
     */
    @ApiModelProperty("国家")
//    @NotNull(message = "收货地点国家不能为空")
    private Integer country;
    /**
     * 省
     */
    @ApiModelProperty("省")
    @NotNull(message = "收货地点省份不能为空")
    private Integer province;
    /**
     * 市
     */
    @ApiModelProperty("市")
    @NotNull(message = "收货地点市不能为空")
    private Integer city;
    /**
     * 区
     */
    @ApiModelProperty("区")
    @NotNull(message = "收货地点区不能为空")
    private Integer district;
    /**
     * 街道
     */
    @ApiModelProperty("街道")
//    @NotNull(message = "收货地点街道不能为空")
    private Integer twon;
    /**
     * 地址
     */
    @ApiModelProperty("地址")
    @NotBlank(message = "收货地点详细地址不能为空")
    private String address;
    /**
     * 邮编
     */
    @ApiModelProperty("邮编")
//    @NotBlank(message = "邮编不能为空")
    private String zipcode;

    /**
     * 电话
     */
    @ApiModelProperty("电话")
    @NotBlank(message = "联系人电话不能为空")
    private String mobile;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

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
     * 商品id
     */
    @NotNull(message = "提货商品ID不能为空")
    @ApiModelProperty("商品id")
    private Integer goodsId;

    /**
     * 提货状态时间
     */
    @ApiModelProperty("提货状态时间")
    private Integer updateTime;
}
