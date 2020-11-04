package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("spec_type")
@AllArgsConstructor
@ApiModel("商品类型与规格对应表")
public class SpecType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("类型id")
    private Integer typeId;

    @ApiModelProperty("规格id")
    private Integer specId;

}
