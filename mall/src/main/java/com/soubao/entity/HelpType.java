package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("help_type")
@ApiModel(value = "帮助类型对象", description = "help_type表")
public class HelpType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("类型ID")
    @TableId(value = "type_id", type = IdType.AUTO)
    private Integer typeId;

    @ApiModelProperty("类型名称")
    @NotBlank(message = "分类名称不能为空")
    private String typeName;

    @ApiModelProperty("排序")
    private Integer sortOrder;

    @ApiModelProperty("调用编号(auto的可删除)")
    private String helpCode;

    @ApiModelProperty("是否显示,0为否,1为是,默认为1")
    private Integer isShow;

    @ApiModelProperty("页面类型:1为店铺,2为会员,默认为1")
    private Integer helpShow;

    @ApiModelProperty("默认0为一级分类")
    private Integer pid;

    private Integer level;

    @ApiModelProperty("子级列表节点")
    @TableField(exist = false)
    private List<HelpType> children;

}
