package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_address")
public class UserAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "address_id", type = IdType.AUTO)
    private Integer addressId;

    private Integer userId;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 国家
     */
    private Integer country;

    /**
     * 省份
     */
    private Integer province;

    /**
     * 城市
     */
    private Integer city;

    /**
     * 地区
     */
    private Integer district;

    /**
     * 乡镇
     */
    private Integer twon;

    /**
     * 地址
     */
    private String address;

    /**
     * 邮政编码
     */
    private String zipcode;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 默认收货地址
     */
    private Integer isDefault;

    /**
     * 地址经度
     */
    private BigDecimal longitude;

    /**
     * 地址纬度
     */
    private BigDecimal latitude;

    @ApiModelProperty("省份名")
    @TableField(exist = false)
    private String provinceName;

    @ApiModelProperty("城市名")
    @TableField(exist = false)
    private String cityName;

    @ApiModelProperty("地区名")
    @TableField(exist = false)
    private String districtName;

    @ApiModelProperty("乡镇名")
    @TableField(exist = false)
    private String twonName;


}
