package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("spec")
@ApiModel("规格对象")
public class Spec implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增id")
    @TableId(type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("规格名称")
    private String name;

    @ApiModelProperty("排序")
    @TableField(value = "`order`")
    private Integer order;

    @ApiModelProperty("是否可上传规格图:0不可，1可以")
    private Boolean isUploadImage;

    @ApiModelProperty("是否需要检索")
    private Boolean searchIndex;

    @ApiModelProperty("一级分类")
    private Integer catId1;

    @ApiModelProperty("二级分类")
    private Integer catId2;

    @ApiModelProperty("三级分类")
    private Integer catId3;

    @ApiModelProperty("规格子项")
    @TableField(exist = false)
    private List<SpecItem> specItemList;

    @ApiModelProperty("商品类型与规格对应表类型id")
    @TableField(exist = false)
    private Integer typeId;
}
