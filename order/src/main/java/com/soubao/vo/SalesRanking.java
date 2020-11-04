package com.soubao.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesRanking {
    private String goodsName;
    private String goodsSn;
    private Integer saleNum;
    private BigDecimal saleAmount;
}
