package com.soubao.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("spec_goods_price")
@ApiModel(value = "规格商品对象", description = "spec_goods_price表")
public class SpecGoodsPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("规格商品id")
    @TableId(value = "item_id", type = IdType.AUTO)
    private Long itemId;

    @ApiModelProperty("商品id")
    private Integer goodsId;

    @ApiModelProperty("商品规格备注")
    private String goodsMark;

    @ApiModelProperty("规格键名")
    @TableField(value = "`key`")
    private String key;

    @ApiModelProperty("规格键名中文")
    private String keyName;

    @ApiModelProperty("价格")
    private BigDecimal price;

    @ApiModelProperty("库存数量")
    private Integer storeCount;

    @ApiModelProperty("商品条形码")
    private String barCode;

    @ApiModelProperty("SKU")
    private String sku;

    @ApiModelProperty("店铺商家id")
    private Integer storeId;

    @ApiModelProperty("规格商品主图")
    private String specImg;

    @ApiModelProperty("参加活动id")
    private Integer promId;

    @ApiModelProperty("参加活动类型")
    private Integer promType;

    @ApiModelProperty("成本价")
    private BigDecimal cost;


    @TableField(exist = false)
    private String goodsName;

    @TableField(exist = false)
    private BigDecimal shopPrice;

    public BigDecimal getShopPrice() {
        if (price != null) {
            return price;
        }
        return shopPrice;
    }

}
