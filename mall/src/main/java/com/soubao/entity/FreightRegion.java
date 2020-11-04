package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-08-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("freight_region")
@ApiModel(value = "运费模板配置地区关系对象", description = "freight_region表")
public class FreightRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模板id")
    private Integer templateId;
    @ApiModelProperty(value = "运费模板配置ID")
    private Integer configId;
    @ApiModelProperty(value = "region表id")
    private Integer regionId;
    @ApiModelProperty(value = "店铺ID")
    private Integer storeId;
    @ApiModelProperty(value = "地区信息对象节点")
    @TableField(exist = false)
    private Region region;
}
