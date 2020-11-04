package com.soubao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 阶梯价
 */
@Setter
@Getter
public class LadderPrice {
//    @NotNull(message = "价格阶梯必须")
    private Integer amount;
//    @NotNull(message = "价格阶梯必须")
    private BigDecimal price;
}
