package com.soubao.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 店铺地址表
 *
 * @author dyr
 * @since 2019-11-25
 */
@Getter
@Setter
public class StoreAddress {

    private Integer storeAddressId;

    /** 收货人 */
    private String consignee;

    /** 省份 */
    private Integer provinceId;

    /** 城市 */
    private Integer cityId;

    /** 地区 */
    private Integer districtId;

    /** 乡镇 */
    private Integer townId;

    /** 地址 */
    private String address;

    /** 邮政编码 */
    private String zipCode;

    /** 手机 */
    private String mobile;

    /** 1为默认收货地址 */
    private Integer isDefault;

    /** 0为发货地址。1为收货地址 */
    private Integer type;

    /** 店铺id */
    private Integer storeId;
}
