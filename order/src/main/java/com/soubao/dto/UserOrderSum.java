package com.soubao.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserOrderSum {
    private Integer userId;
    private BigDecimal orderSum;
}
