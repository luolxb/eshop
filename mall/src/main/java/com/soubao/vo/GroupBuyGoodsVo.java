package com.soubao.vo;

import com.soubao.entity.Goods;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@ApiModel(value = "团购主体对象")
public class GroupBuyGoodsVo extends Goods {
    @ApiModelProperty(value = "折扣")
    private BigDecimal rebate;
    @ApiModelProperty(value = "结束时间")
    private Long endTime;
}
