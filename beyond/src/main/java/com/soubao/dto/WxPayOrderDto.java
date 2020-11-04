package com.soubao.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
public class WxPayOrderDto {

    private String orderSn;
    private String openid;
    private BigDecimal amount;
    private String body;
    private String tradeType;
    private String clientIp;
}
