package com.soubao.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleDayDetails {
    private Integer goodsId;
    private String goodsName;
    private String goodsSn;
    private Integer goodsNum;
    private BigDecimal goodsPrice;
    private String days;
}
