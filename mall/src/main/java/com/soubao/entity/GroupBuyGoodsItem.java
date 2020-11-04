package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dyr
 * @since 2019-08-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("group_buy_goods_item")
public class GroupBuyGoodsItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 团购id */
    private Integer groupBuyId;

    /** 商品id */
    private Integer goodsId;

    /** 对应spec_goods_price商品规格id */
    private Integer itemId;

    /** 团购价格 */
    @NotNull(message = "团购价格不能为空")
    private BigDecimal price;

    /** 商品参团数 */
    @NotNull(message = "参团数量不能为空")
    private Integer goodsNum;

    /** 虚拟购买数 */
    private Integer virtualNum;

    /** 折扣 */
    private BigDecimal rebate;

    /** 商品原价 */
    private BigDecimal goodsPrice;

    /** 购买数量 */
    private Integer buyNum;

    /** 已下单人数 */
    private Integer orderNum;

    /** 商品sku名称 */
    @TableField(exist = false)
    private String keyName;

    /** 商品sku库存 */
    @TableField(exist = false)
    private Integer storeCount;

    /** 商品sku价格 */
    @TableField(exist = false)
    private BigDecimal shopPrice;
}
