package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "商品属性对象", description = "goods_attr表")
public class GoodsAttr implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品属性主键")
    @TableId(value = "goods_attr_id", type = IdType.AUTO)
    private Integer goodsAttrId;
    @ApiModelProperty(value = "商品主键")
    private Integer goodsId;
    @ApiModelProperty(value = "属性主键")
    private Integer attrId;
    @ApiModelProperty(value = "属性值")
    private String attrValue;
    @ApiModelProperty(value = "属性价格")
    private String attrPrice;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品属性节点")
    private GoodsAttribute goodsAttribute;

}
