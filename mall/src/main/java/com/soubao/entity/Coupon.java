package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.common.utils.TimeUtil;
import com.soubao.validation.group.coupon.NewcomerCoupon;
import com.soubao.validation.group.coupon.StoreCoupon;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 优惠券表
 * </p>
 *
 * @author dyr
 * @since 2019-08-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("coupon")
@ApiModel(value = "优惠券对象", description = "coupon表")
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "店铺名称")
    @TableField(exist = false)
    private String storeName;

    @NotBlank(message = "优惠券名称必须")
    @ApiModelProperty(value = "优惠券名字")
    @NotBlank(groups = {NewcomerCoupon.class, StoreCoupon.class}, message = "优惠券名称不能为空")
    private String name;

    @ApiModelProperty(value = "发放类型 0下单赠送 1 按用户发放 2 免费领取 3 线下发放")
    private Integer type;

    @ApiModelProperty(value = "使用范围：0全店通用1指定商品可用2指定分类商品可用")
    @NotNull(groups = {NewcomerCoupon.class}, message = "请选择使用范围")
    private Integer useType;

    @ApiModelProperty(value = "优惠券金额")
    @NotNull(groups = {NewcomerCoupon.class, StoreCoupon.class}, message = "优惠券面额不能为空")
    @Digits(groups = {NewcomerCoupon.class, StoreCoupon.class}, integer = 7, fraction = 2, message = "额度不正确，至多七位整数与两位小数")
    @DecimalMin(groups = {NewcomerCoupon.class}, value = "0.01", message = "额度最小为0.01")
//    @DecimalMax(groups = {NewcomerCoupon.class}, value = "9999999", message = "额度最大为9999999")
    private BigDecimal money;

    @ApiModelProperty(value = "使用条件")
    @TableField("`condition`")
    @NotNull(groups = {NewcomerCoupon.class, StoreCoupon.class}, message = "消费金额使用条件不能为空")
    @Digits(groups = {NewcomerCoupon.class, StoreCoupon.class}, integer = 7, fraction = 2, message = "额度不正确，至多七位整数与两位小数")
    @DecimalMin(groups = {NewcomerCoupon.class}, value = "0.01", message = "额度最小为0.01")
//    @DecimalMax(value = "9999999", message = "额度最大为9999999")
    private BigDecimal condition;

    @NotNull(message = "请填写发放数量")
    @ApiModelProperty(value = "发放数量")
    private Integer createnum;

    @ApiModelProperty(value = "已领取数量")
    private Integer sendNum;

    @ApiModelProperty(value = "已使用数量")
    private Integer useNum;

    @ApiModelProperty(value = "发放开始时间")
    @NotNull(groups = {NewcomerCoupon.class}, message = "发放起始日期不能为空")
    private Long sendStartTime;

    @ApiModelProperty(value = "发放结束时间")
    @NotNull(groups = {NewcomerCoupon.class}, message = "发放截止日期不能为空")
    private Long sendEndTime;

    @ApiModelProperty(value = "使用开始时间")
    private Long useStartTime;

    @ApiModelProperty(value = "使用结束时间")
    private Long useEndTime;

    @ApiModelProperty(value = "添加时间")
    private Long addTime;

    @ApiModelProperty(value = "商家店铺ID")
    private Integer storeId;

    @ApiModelProperty(value = "1有效2无效")
    private Integer status;

    @ApiModelProperty(value = "优惠券描述")
    private String couponInfo;
    
    @ApiModelProperty(value = "使用有效期x天，仅用于新人优惠券")
    @NotNull(groups = {NewcomerCoupon.class}, message = "新人优惠券有效期不能为空")
    private Integer validityDay;

    @ApiModelProperty(value = "优惠券使用店铺限制")
    @TableField(exist = false)
    private String limitStore;

    @ApiModelProperty(value = "优惠券可用时间限制")
    @TableField(exist = false)
    private String deadTimeFormat;

    @ApiModelProperty(value = "优惠券发放截至剩余时间")
    @TableField(exist = false)
    private Long spacingTime;

    @ApiModelProperty(value = "商品id")
    @TableField(exist = false)
    private Integer goodsId;

    @ApiModelProperty(value = "商品分类id")
    @TableField(exist = false)
    private Integer goodsCategoryId;

    @ApiModelProperty(value = "用户优惠券id")
    @TableField(exist = false)
    private Integer couponListId;

    @ApiModelProperty(value = "用户是否已领该优惠券")
    @TableField(exist = false)
    private Integer isGet;

    @ApiModelProperty(value = "优惠券图片、店铺LOGO")
    @TableField(exist = false)
    private String image;

    public String getLimitStore() {
        if (storeId != null && storeId > 0) {
            switch (useType) {
                case 0:
                    return "可在" + limitStore + "通用购买";
                case 1:
                    return "可购买" + limitStore + "指定的商品";
                case 2:
                    return "可购买" + limitStore + "指定分类的商品";
                default:
                    throw new ShopException(ResultEnum.UNKNOWN_ERROR);
            }
        } else {
            return "可用于全平台的商品";
        }
    }
    public Integer getIsGet() {
        //接收的是uid，如果存在则表示优惠券已领
        return isGet == null ? 0 : 1;
    }

    @TableField(exist = false)
    private String useEndTimeDesc;

    public String getUseEndTimeDesc() {
        if (useEndTime != null) {
            return TimeUtil.transForDateStr(useEndTime, "yyyy-MM-dd");
        }
        return useEndTimeDesc;
    }

    @TableField(exist = false)
    private String typeDetail;

    public String getTypeDetail() {
        if (type != null) {
            if (type == 0) {
                return "下单赠送";
            }
            if (type == 1) {
                return "按用户发放";
            }
            if (type == 2) {
                return "免费领取";
            }
            if (type == 3) {
                return "线下发放";
            }
            if (type == 4) {
                return "新人优惠券";
            }
        }
        return typeDetail;
    }

    @TableField(exist = false)
    private String statusDetail;

    public String getStatusDetail() {
        if (status != null) {
            if (status == 1) {
                return "有效";
            }
            if (status == 2) {
                return "无效";
            }
        }
        return statusDetail;
    }

    @TableField(exist = false)
    private List<Goods> goodsList;

    @TableField(exist = false)
    private GoodsCategory goodsCategory;

    @TableField(exist = false)
    private GoodsCoupon goodsCoupon;
}
