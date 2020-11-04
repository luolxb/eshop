package com.soubao.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * <p>
 * 发货单
 * </p>
 *
 * @author dyr
 * @since 2019-10-14
 */
@Getter
@Setter
public class DeliveryDoc {

    private Integer id;

    /**
     * 订单ID
     */
    private Integer orderId;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 管理员ID
     */
    private Integer adminId;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 邮编
     */
    private String zipcode;

    /**
     * 联系手机
     */
    private String mobile;

    /**
     * 国ID
     */
    private Integer country;

    /**
     * 省ID
     */
    private Integer province;

    /**
     * 市ID
     */
    private Integer city;

    /**
     * 区ID
     */
    private Integer district;

    private String address;

    /**
     * 物流code
     */
    private String shippingCode;

    /**
     * 快递名称
     */
    private String shippingName;

    /**
     * 运费
     */
    private BigDecimal shippingPrice;

    /**
     * 物流单号
     */
    private String invoiceNo;

    /**
     * 座机电话
     */
    private String tel;

    /**
     * 管理员添加的备注信息
     */
    private String note;

    /**
     * 友好收货时间
     */
    private Long bestTime;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 是否已经删除
     */
    private Boolean isDel;

    /**
     * 店铺商家id
     */
    private Integer storeId;

    /**
     * 发货方式0自填快递1在线预约2电子面单3无需物流
     */
    private Integer sendType;

    /**
     * 店铺发货人
     */
    private String storeAddressConsignee;

    /**
     * 发货人手机
     */
    private String storeAddressMobile;

    /**
     * 发货省
     */
    private Integer storeAddressProvinceId;

    /**
     * 发货市
     */
    private Integer storeAddressCityId;

    /**
     * 发货县/区
     */
    private Integer storeAddressDistrictId;

    /**
     * 发货地址
     */
    private String storeAddress;

    private String createTimeDesc;

    private Seller seller;//管理员 （用于操作记录）

    private String storeAddressProvinceName;//发货地址省份
    private String storeAddressCityName;//发货地址城市
    private String storeAddressDistrictName;//发货地址地区
    private String fullAddress;//完整发货地址


}
