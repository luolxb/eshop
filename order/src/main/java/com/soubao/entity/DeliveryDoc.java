package com.soubao.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.soubao.common.utils.TimeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 发货单
 * </p>
 *
 * @author dyr
 * @since 2019-10-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("delivery_doc")
public class DeliveryDoc implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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

    @TableField(exist = false)
    private String createTimeDesc;
    public String getCreateTimeDesc(){
        if (createTime != null) {
            return TimeUtil.transForDateStr(this.createTime, "yyyy-MM-dd HH:mm:ss");
        }
        return createTimeDesc;
    }

    @TableField(exist = false)
    private Seller seller;//管理员 （用于操作记录）

    @TableField(exist = false)
    private String storeAddressProvinceName;//发货地址省份
    @TableField(exist = false)
    private String storeAddressCityName;//发货地址城市
    @TableField(exist = false)
    private String storeAddressDistrictName;//发货地址地区
    @TableField(exist = false)
    private String fullAddress;//完整发货地址
    public String getFullAddress(){
        StringBuffer sb = new StringBuffer();
        if (!StringUtils.isEmpty(storeAddressProvinceName)){
            sb.append(storeAddressProvinceName);
        }
        if (!StringUtils.isEmpty(storeAddressCityName)){
            sb.append(storeAddressCityName);
        }
        if (!StringUtils.isEmpty(storeAddressDistrictName)){
            sb.append(storeAddressDistrictName);
        }
        if (!StringUtils.isEmpty(storeAddress)){
            sb.append(storeAddress);
        }
        return sb.toString();
    }


}
