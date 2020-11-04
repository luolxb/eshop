package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-11-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods_type")
public class GoodsType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "商品模型id", example="1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 类型名称
     */
    @ApiModelProperty(value = "商品模型名称", example="手机")
    @NotBlank(message = "模型名称不能为空")
    private String name;

    @ApiModelProperty(value = "商品分类列表")
    @NotEmpty(message = "绑定分类不能为空")
    @TableField(exist = false)
    private List<GoodsCategory> goodsCategoryList;

    @ApiModelProperty(value = "商品规格")
    @TableField(exist = false)
    private List<Spec> specList;

    @ApiModelProperty(value = "商品属性")
    @TableField(exist = false)
    private List<GoodsAttribute> goodsAttributes;

    @ApiModelProperty(value = "商品品牌")
    @TableField(exist = false)
    private List<BrandType> brandTypeList;
}
