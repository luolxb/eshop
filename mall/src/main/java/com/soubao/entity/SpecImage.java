package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("spec_image")
@ApiModel("商品规格图片对象")
public class SpecImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品规格图片表id")
    private Integer goodsId;

    @ApiModelProperty("tp_spec_item引用规格项id")
    private Integer specImageId;

    @ApiModelProperty("商品规格图片路径")
    private String src;

    @ApiModelProperty("商家id")
    private Integer storeId;

}
