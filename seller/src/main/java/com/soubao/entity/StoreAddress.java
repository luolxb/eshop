package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.soubao.validation.group.Insert;
import com.soubao.validation.group.Update;
import com.soubao.validation.group.annotation.Phone;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * <p>
 * 店铺地址表
 * </p>
 *
 * @author dyr
 * @since 2019-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("store_address")
public class StoreAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "store_address_id", type = IdType.AUTO)
    @NotNull(groups = Update.class, message = "修改的地址id不能为空")
    private Integer storeAddressId;

    /**
     * 收货人
     */
    @NotBlank(groups = {Insert.class,Update.class},message = "收货人必填")
    @Size(max = 50,message = "名字不得超过50字符")
    private String consignee;

    /**
     * 省份
     */
    @NotNull(groups = {Insert.class,Update.class},message = "请选择省")
    private Integer provinceId;

    /**
     * 城市
     */
    @NotNull(groups = {Insert.class,Update.class},message = "请选择市")
    private Integer cityId;

    /**
     * 地区
     */
    @NotNull(groups = {Insert.class,Update.class},message = "请选择地址")
    private Integer districtId;

    /**
     * 乡镇
     */
    private Integer townId;

    /**
     * 地址
     */
    @NotBlank(groups = {Insert.class,Update.class},message = "请填写地址")
    @Size(groups = {Insert.class,Update.class},max = 250,message = "地址不能超过250个字符")
    private String address;

    /**
     * 邮政编码
     */
    @NotBlank(groups = {Insert.class,Update.class},message = "请填写邮政编码")
    @Pattern(groups = {Insert.class,Update.class},regexp = "\\d{6}",message = "邮政编码格式错误")
    private String zipCode;

    /**
     * 手机
     */
    @NotBlank(groups = {Insert.class,Update.class},message = "请填写手机号码")
    @Phone(groups = {Insert.class,Update.class})
    private String mobile;

    /**
     * 1为默认收货地址
     */
    @NotNull(groups = {Insert.class,Update.class},message = "请选择是否默认")
    private Integer isDefault;

    /**
     * 0为发货地址。1为收货地址
     */
    @NotNull(groups = {Insert.class,Update.class},message = "请选择类型")
    private Integer type;

    /**
     * 店铺id
     */
    private Integer storeId;

    @TableField(exist = false)
    private String typeDesc;
    public String getTypeDesc(){
        if (type != null){
            if (type == 1){
                return "收货";
            }else{
                return "发货";
            }
        }
        return null;
    }

    @TableField(exist = false)
    private String isDefaultDesc;
    public String getIsDefaultDesc(){
        if (isDefault != null){
            if (isDefault == 1){
                return "是";
            }else{
                return "否";
            }
        }
        return null;
    }

    @TableField(exist = false)
    private String provinceName;
    @TableField(exist = false)
    private String cityName;
    @TableField(exist = false)
    private String districtName;
    @TableField(exist = false)
    private String fullAddress;
    public String getFullAddress(){
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(provinceName)){
            sb.append(provinceName);
        }
        if (!StringUtils.isEmpty(cityName)){
            sb.append(cityName);
        }
        if (!StringUtils.isEmpty(districtName)){
            sb.append(districtName);
        }
        if (!StringUtils.isEmpty(address)){
            sb.append(address);
        }
        return sb.toString();
    }

}
