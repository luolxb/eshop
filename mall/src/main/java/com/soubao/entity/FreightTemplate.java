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

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>
 * 运费模板表
 * </p>
 *
 * @author dyr
 * @since 2019-08-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("freight_template")
@ApiModel(value = "运费模板对象", description = "freight_template表")
public class FreightTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("运费模板ID")
    @TableId(value = "template_id", type = IdType.AUTO)
    private Integer templateId;

    @ApiModelProperty("模板名称")
    @NotBlank(message = "模板名称不能为空")
    private String templateName;

    @ApiModelProperty("0 件数；1 商品重量；2 商品体积")
    @NotNull(message = "请选择计价方式")
    private Integer type;

    @ApiModelProperty("是否启用使用默认运费配置,0:不启用，1:启用")
    @NotNull(message = "请选择是否启用默认配置")
    private Integer isEnableDefault;

    @ApiModelProperty("店铺Id")
    private Integer storeId;

    @ApiModelProperty("运费配置列表节点")
    @TableField(exist = false)
    @Valid // 嵌套验证必须用@Valid
    @NotNull(message = "请添加配送配置")
    @Size(min = 1, message = "请添加配送配置")
    private List<FreightConfig> freightConfigList;
}
