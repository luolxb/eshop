package com.soubao.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2020-03-03
 */
@Getter
@Setter
public class CouponList {
    private Integer id;

    private Integer cid;

    private Boolean type;

    private Integer uid;

    private Integer orderId;

    private Integer getOrderId;

    private Integer useTime;

    private String code;

    private Integer sendTime;

    private Integer storeId;

    private Integer status;

    private Boolean deleted;

}
