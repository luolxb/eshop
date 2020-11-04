package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 组合促销商品映射关系表
 * </p>
 *
 * @author dyr
 * @since 2020-05-19
 */
@ApiModel(value = "组合促销商品映射关系对象", description = "combination_goods表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CombinationGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "组合促销主键")
    private Integer combinationId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "规格名称")
    private String keyName;
    @ApiModelProperty(value = "商品主键")
    private Integer goodsId;
    @ApiModelProperty(value = "规格主键")
    private Long itemId;
    @ApiModelProperty(value = "原价/商城价")
    private BigDecimal originalPrice;
    @ApiModelProperty(value = "优惠价格")
    private BigDecimal price;
    @ApiModelProperty(value = "1主0从")
    private Boolean isMaster;
    @ApiModelProperty(value = "商品库存")
    @TableField(exist = false)
    private Integer goodsCount;
    @ApiModelProperty(value = "商品成本价")
    @TableField(exist = false)
    private BigDecimal costPrice;
    @ApiModelProperty(value = "商品图片")
    @TableField(exist = false)
    private String goodsImg;

    @TableField(exist = false)
    private String isMasterDesc;

    public String getIsMasterDesc() {
        if (isMaster) {
            return "主商品";
        } else {
            return "副商品";
        }
    }

    @ApiModelProperty(value = "用来标识唯一,商品id和商品规格id组合，用-分隔")
    @TableField(exist = false)
    private String skuSn;
    public String getSkuSn(){
        if(goodsId != null && itemId != null){
            skuSn = goodsId + "-" +  itemId;
        }
        return skuSn;
    }

    @ApiModelProperty(value = "商品sku")
    @TableField(exist = false)
    private GoodsSku goodsSku;
}
