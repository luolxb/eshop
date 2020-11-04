package com.soubao.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-09-02
 */
@Getter
@Setter
public class CouponList {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "用户名称")
    private String username;

    @ApiModelProperty(value = "优惠券 对应coupon表id")
    private Integer cid;

    @ApiModelProperty(value = "优惠券名称")
    private String name;

    @ApiModelProperty(value = "发放类型 0下单赠送 1 按用户发放 2 免费领取 3 线下发放")
    private Integer type;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "订单id")
    private Integer orderId;

    @ApiModelProperty(value = "送券订单ID")
    private Integer getOrderId;

    @ApiModelProperty(value = "使用时间")
    private Long useTime;

    @ApiModelProperty(value = "优惠券兑换码")
    private String code;

    @ApiModelProperty(value = "发放时间")
    private Long sendTime;

    @ApiModelProperty(value = "商家店铺ID")
    private Integer storeId;

    @ApiModelProperty(value = "0未使用1已使用2已过期")
    private Integer status;

    @ApiModelProperty(value = "删除标识;1:删除,0未删除")
    private Boolean deleted;

    private String orderSn;

    private String typeDetail;

    private String orderSnShow;

    private String useTimeDetail;

    private String statusDetail;


}
