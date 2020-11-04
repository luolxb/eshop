package com.soubao.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Stock {
    private Integer goodsId;
    private Long itemId;
    private String goodsName;
    private String keyName;
    private BigDecimal price;
    private Integer storeCount;
}
