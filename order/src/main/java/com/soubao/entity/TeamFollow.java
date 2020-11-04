package com.soubao.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("参团对象")
public class TeamFollow {

    @ApiModelProperty("参团id")
    private Integer followId;

    @ApiModelProperty("参团时间")
    private Long followTime;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("订单编号")
    private String orderSn;

}
