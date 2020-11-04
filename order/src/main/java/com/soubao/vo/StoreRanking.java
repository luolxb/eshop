package com.soubao.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreRanking {
    private Integer storeId;
    private String storeName;
    private Integer orderNum;
    private BigDecimal amount;
}
