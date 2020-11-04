package com.soubao.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("开团对象")
public class TeamFound {

    @ApiModelProperty("开团id")
    private Integer foundId;

    @ApiModelProperty("拼团活动id")
    private Integer teamId;

    @ApiModelProperty("团长订单id")
    private Integer orderId;

    @ApiModelProperty("团长订单编号")
    private String orderSn;

    @ApiModelProperty("拼团状态0:待开团(表示已下单但是未支付)1:已经开团(团长已支付)2:拼团成功,3拼团失败")
    private Integer status;

}
