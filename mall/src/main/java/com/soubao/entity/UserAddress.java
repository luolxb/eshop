package com.soubao.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@ApiModel("用户地址对象")
public class UserAddress {
    @ApiModelProperty("地址id")
    private Integer addressId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("收货人")
    private String consignee;

    @ApiModelProperty("邮箱地址")
    private String email;

    @ApiModelProperty("国家")
    private Integer country;

    @ApiModelProperty("省份")
    private Integer province;

    @ApiModelProperty("城市")
    private Integer city;

    @ApiModelProperty("地区")
    private Integer district;

    @ApiModelProperty("乡镇")
    private Integer twon;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("邮政编码")
    private String zipcode;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("默认收货地址")
    private Integer isDefault;

    @ApiModelProperty("地址经度")
    private BigDecimal longitude;

    @ApiModelProperty("地址纬度")
    private BigDecimal latitude;


    @ApiModelProperty("省份名")
    private String provinceName;

    @ApiModelProperty("城市名")
    private String cityName;

    @ApiModelProperty("地区名")
    private String districtName;

    @ApiModelProperty("乡镇名")
    private String twonName;

}
