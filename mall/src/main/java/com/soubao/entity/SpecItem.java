package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("spec_item")
@ApiModel("规格项对象")
public class SpecItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("规格id")
    private Integer specId;

    @ApiModelProperty("规格项")
    private String item;

    @ApiModelProperty("商家id")
    private Integer storeId;

    @ApiModelProperty("规格图片")
    @TableField(exist = false)
    private String src;

    @ApiModelProperty("spec_goods_price的key选中")
    @TableField(exist = false)
    private Boolean isCheck;
}
