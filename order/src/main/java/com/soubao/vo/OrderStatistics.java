package com.soubao.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class OrderStatistics {

    @ApiModelProperty("待付款数量")
    private Integer waitPayCount;
    @ApiModelProperty("待发货数量")
    private Integer waitSendCount;
    @ApiModelProperty("待收货数量")
    private Integer waitReceiveCount;
    @ApiModelProperty("中出售")
    private Integer sellCount;
}
