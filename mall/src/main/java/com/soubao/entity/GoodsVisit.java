package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品浏览历史表
 * </p>
 *
 * @author dyr
 * @since 2019-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods_visit")
public class GoodsVisit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "visit_id", type = IdType.AUTO)
    private Integer visitId;
    /**
     * 商品ID
     */
    private Integer goodsId;

    /**
     * 会员ID
     */
    private Integer userId;

    /**
     * 浏览时间
     */
    private Long visitTime;

    /**
     * 商品一级分类
     */
    private Integer catId1;

    /**
     * 商品二级分类
     */
    private Integer catId2;

    /**
     * 商品三级分类
     */
    private Integer catId3;


    @TableField(exist = false)
    private String goodsName;

    @TableField(exist = false)
    private BigDecimal shopPrice;

    @TableField(exist = false)
    private Integer isVirtual;

    @TableField(exist = false)
    private long catVisitCount;

    @TableField(exist = false)
    private String catName;

}
