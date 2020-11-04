package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2020-05-14
 */
@ApiModel(value = "移动端用户中心自定义配置", description = "用户中心自定义配置表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MenuCfg implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键，移动端建议用这个来唯一标识")
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;
    @ApiModelProperty(value = "自定义名称")
    @NotBlank(message = "自定义名称不能为空")
    private String menuName;
    @ApiModelProperty(value = "默认名称")
    private String defaultName;
    @ApiModelProperty(value = "是否显示")
    private Boolean isShow;


}
