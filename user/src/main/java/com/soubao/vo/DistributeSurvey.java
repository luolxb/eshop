package com.soubao.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel(value = "分销佣金概况")
@Data
public class DistributeSurvey {
    @ApiModelProperty(value = "正在提现的佣金")
    private BigDecimal withdrawingMoney;
    @ApiModelProperty(value = "可提现的佣金")
    private BigDecimal ableWithdrawMoney;
    @ApiModelProperty(value = "提现申请中的佣金")
    private BigDecimal applyWithdrawMoney;
    @ApiModelProperty(value = "提现审核通过待打款的佣金")
    private BigDecimal waitWithdrawMoney;
}
