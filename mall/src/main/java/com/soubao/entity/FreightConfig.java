package com.soubao.entity;

import java.math.BigDecimal;

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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>
 * 运费配置表
 * </p>
 *
 * @author dyr
 * @since 2019-08-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("freight_config")
@ApiModel(value = "运费配置对象", description = "freight_config表")
public class FreightConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "配置id")
    @TableId(value = "config_id", type = IdType.AUTO)
    private Integer configId;
    @ApiModelProperty(value = "首(重：体积：件）")

    private Double firstUnit;

    @ApiModelProperty(value = "(重：体积：件）运费")
    private BigDecimal firstMoney;

    @ApiModelProperty(value = "继续加（件：重量：体积）区间")
    private Double continueUnit;

    @ApiModelProperty(value = "继续加（件：重量：体积）的运费")
    private BigDecimal continueMoney;

    @ApiModelProperty(value = "运费模板ID")
    private Integer templateId;

    @ApiModelProperty(value = "是否是默认运费配置.0不是，1是")
    private Integer isDefault;

    @ApiModelProperty(value = "店铺ID")
    private Integer storeId;

    @ApiModelProperty(value = "配置地区关系节点")
    @TableField(exist = false)
    private List<FreightRegion> freightRegionList;
}
