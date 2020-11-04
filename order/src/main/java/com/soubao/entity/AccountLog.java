package com.soubao.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-08-29
 */
@Getter
@Setter
public class AccountLog{

    private Integer logId;

    private Integer userId;

    private BigDecimal userMoney;

    private BigDecimal frozenMoney;

    private Integer payPoints;

    private Long changeTime;

    private String desc;

    private String orderSn;

    private Integer orderId;

}
