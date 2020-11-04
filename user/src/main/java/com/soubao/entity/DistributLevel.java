package com.soubao.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
@ApiModel("分销等级对象")
public class DistributLevel {

    @ApiModelProperty("等级ID")
    private Integer levelId;

    @ApiModelProperty("分销等级类别")
    private Integer levelType;

    @ApiModelProperty("一级佣金比例")
    private BigDecimal rate1;

    @ApiModelProperty("级佣金比例")
    private BigDecimal rate2;

    @ApiModelProperty("三级佣金比例")
    private BigDecimal rate3;

    @ApiModelProperty("升级条件")
    private BigDecimal orderMoney;

    @ApiModelProperty("等级名称")
    private String levelName;

}
