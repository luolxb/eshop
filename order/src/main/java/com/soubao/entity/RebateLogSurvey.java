package com.soubao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RebateLogSurvey implements Serializable {
    //下线分销订单总额
    private BigDecimal goodsPriceSum;
    //下线分销订单数
    private Integer orderCount;
    //下线分销金额
    private BigDecimal distributedMoneySum;
    //今日收入
    private BigDecimal todayIncomeMoney;
    //未结算佣金
    private BigDecimal unsettleMoneySum;
    //待收货佣金 = 用户已收货，但未达到售后时间过期的佣金
    private BigDecimal pendingReceiptMoneySum;
    //无效佣金
    private BigDecimal invalidMoneySum;

}
