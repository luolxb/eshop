package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("distribut_level")
@ApiModel("分销等级对象")
public class DistributLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("等级ID")
    @TableId(value = "level_id", type = IdType.AUTO)
    private Integer levelId;

    @ApiModelProperty("分销等级类别")
    private Integer levelType;

    @ApiModelProperty("一级佣金比例")
    private BigDecimal rate1;

    @ApiModelProperty("级佣金比例")
    private BigDecimal rate2;

    @ApiModelProperty("三级佣金比例")
    private BigDecimal rate3;

    @ApiModelProperty("升级条件")
    private BigDecimal orderMoney;

    @ApiModelProperty("等级名称")
    @NotBlank(message = "等级名称不能为空")
    private String levelName;

}
