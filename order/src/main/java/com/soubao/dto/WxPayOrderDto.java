package com.soubao.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
public class WxPayOrderDto {
    @NotNull
    private String orderSn;
    private String openid;
    private BigDecimal amount;
    private String body;
    private String tradeType;//JSAPI,
    private String clientIp;
}
