package com.soubao.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.soubao.validation.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cart")
@ApiModel(value = "购物车对象", description = "cart表")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "购物车id")
    @NotNull(groups = Update.class, message = "购物车id为空")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "session")
    private String sessionId;

    @ApiModelProperty(value = "商品id")
    @NotNull(message = "商品id不能为空")
    private Integer goodsId;

    @ApiModelProperty(value = "商品货号")
    private String goodsSn;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "市场价")
    private BigDecimal marketPrice;

    @ApiModelProperty(value = "本店价")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "会员折扣价")
    private BigDecimal memberGoodsPrice;

    @ApiModelProperty(value = "购买数量")
    @NotNull
    @Min(value = 0, message = "购买商品数量不能小于1") //最小值为1
    private Integer goodsNum;

    @ApiModelProperty(value = "商品规格key 对应tp_spec_goods_price 表")
    private String specKey;

    @ApiModelProperty(value = "商品规格组合名称")
    private String specKeyName;

    @ApiModelProperty(value = "商品条码")
    private String barCode;

    @ApiModelProperty(value = "购物车选中状态")
    private Integer selected;

    @ApiModelProperty(value = "加入购物车的时间")
    private Long addTime;

    @ApiModelProperty(value = "0 普通订单,1 限时抢购, 2 团购 , 3 促销优惠")
    private Integer promType;

    @ApiModelProperty(value = "活动id")
    private Integer promId;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "商家店铺ID")
    private Integer storeId;

    @ApiModelProperty(value = "门店ID")
    private Integer shopId;

    @ApiModelProperty(value = "门店商品表ID")
    private Integer sgsId;

    @ApiModelProperty(value = "规格ID")
    private Long itemId;

    @ApiModelProperty(value = "属于哪个店铺的购物车，store_id是记哪个店铺的商品")
    private Integer cartStoreId;

    @ApiModelProperty(value = "搭配购,购物车父主键")
    private Integer combinationGroupId;

    @ApiModelProperty(value = "单条商品会员打折后总额")
    @TableField(exist = false)
    private BigDecimal goodsFee;
    public BigDecimal getGoodsFee(){
        if(memberGoodsPrice != null && goodsNum != null){
            goodsFee = memberGoodsPrice.multiply(BigDecimal.valueOf(goodsNum));
        }
        return goodsFee;
    }

    @ApiModelProperty(value = "商品总额优惠")
    @TableField(exist = false)
    private BigDecimal cutFee;
    public BigDecimal getCutFee() {
        if(memberGoodsPrice != null && goodsNum != null && goodsPrice != null){
            cutFee = goodsPrice.subtract(memberGoodsPrice).multiply(BigDecimal.valueOf(goodsNum));
        }
        return cutFee;
    }

    @ApiModelProperty(value = "商品总额")
    @TableField(exist = false)
    private BigDecimal totalFee;
    public BigDecimal getTotalFee(){
        if(goodsPrice != null && goodsNum != null){
            totalFee = goodsPrice.multiply(BigDecimal.valueOf(goodsNum));
        }
        return totalFee;
    }

    @TableField(exist = false)
    private Integer isDelivery;


    @TableField(exist = false)
    private Integer limitNum;
}
