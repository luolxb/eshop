package com.soubao.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Coupon {

    private Integer id;
    private String name;
    private Integer type;
    private Integer useType;
    private BigDecimal money;
    private BigDecimal condition;
    private Integer createnum;
    private Integer sendNum;
    private Integer useNum;
    private Long sendStartTime;
    private Long sendEndTime;
    private Long useStartTime;
    private Long useEndTime;
    private Long addTime;
    private Integer storeId;
    private Integer status;
    private String couponInfo;
    private Integer validityDay;
}
