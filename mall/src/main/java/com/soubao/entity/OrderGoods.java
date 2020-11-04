package com.soubao.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "订单商品对象", description = "order_goods表")
public class OrderGoods implements Serializable {
    @ApiModelProperty(value = "订单商品主键")
    @TableId(value = "rec_id", type = IdType.AUTO)
    private Integer recId;
    @ApiModelProperty(value = "订单表主键")
    private Integer orderId;
    @ApiModelProperty(value = "商品表主键")
    private Integer goodsId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品货号")
    private String goodsSn;
    @ApiModelProperty(value = "购买数量")
    private Integer goodsNum;
    @ApiModelProperty(value = "商品实际购买价格")
    private BigDecimal finalPrice;
    @ApiModelProperty(value = "本店价")
    private BigDecimal goodsPrice;
    @ApiModelProperty(value = "商品成本价")
    private BigDecimal costPrice;
    @ApiModelProperty(value = "会员折扣价")
    private BigDecimal memberGoodsPrice;
    @ApiModelProperty(value = "购买商品赠送积分")
    private Integer giveIntegral;
    @ApiModelProperty(value = "商品规格key")
    private String specKey;
    @ApiModelProperty(value = "规格对应的中文名字")
    private String specKeyName;
    @ApiModelProperty(value = "条码")
    private String barCode;
    @ApiModelProperty(value = "是否评价")
    private Integer isComment;
    @ApiModelProperty(value = "0普通订单商品,1限时抢购,2团购,3促销优惠,4预售")
    private Integer promType;
    @ApiModelProperty(value = "活动表主键")
    private Integer promId;
    @ApiModelProperty(value = "0未发货，1已发货，2已换货，3已退货")
    private Integer isSend;
    @ApiModelProperty(value = "发货单ID")
    private Integer deliveryId;
    @ApiModelProperty(value = "sku")
    private String sku;
    @ApiModelProperty(value = "商家店铺id")
    private Integer storeId;
    @ApiModelProperty(value = "商家抽成比例")
    private Integer commission;
    @ApiModelProperty(value = "是否已跟商家结账0 否1是")
    private Integer isCheckout;
    @ApiModelProperty(value = "0:为删除；1：已删除")
    private Integer deleted;
    @ApiModelProperty(value = "三级分销的金额")
    private BigDecimal distribut;
    @ApiModelProperty(value = "门店ID")
    private Integer shopId;

    private static final long serialVersionUID = 1L;

    /**
     * 以下是非数据库字段
     */
    @ApiModelProperty(value = "店铺名称")
    @TableField(exist = false)
    private String storeName;
    @ApiModelProperty(value = "订单金额")
    @TableField(exist = false)
    private BigDecimal orderAmount;
    @ApiModelProperty(value = "生成时间")
    @TableField(exist = false)
    private Long addTime;
    @ApiModelProperty(value = "订单编号")
    @TableField(exist = false)
    private String orderSn;
    @ApiModelProperty(value = "优惠金额")
    @TableField(exist = false)
    private BigDecimal cutFee;

    @ApiModelProperty(value = "是否已配送")
    @TableField(exist = false)
    private Integer isDelivery;

    @ApiModelProperty(value = "购买限制数量")
    @TableField(exist = false)
    private Integer limitNum;

    @ApiModelProperty(value = "商品总价")
    @TableField(exist = false)
    private BigDecimal goodsFee;

    @ApiModelProperty(value = "商品订金")
    @TableField(exist = false)
    private BigDecimal paidMoney;

    @ApiModelProperty(value = "订单节点")
    @TableField(exist = false)
    private Order order;
}