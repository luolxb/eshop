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

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("news_cat")
@ApiModel(value = "新闻分类对象", description = "news_cat表")
public class NewsCat implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "cat_id", type = IdType.AUTO)
    private Integer catId;

    @ApiModelProperty(" 类别名称")
    @NotBlank(message = "分类名称不能为空")
    private String catName;

    @ApiModelProperty(" 默认分组")
    private Integer catType;

    @ApiModelProperty(" 夫级ID")
    private Integer parentId;

    @ApiModelProperty(" 是否导航显示")
    private Integer showInNav;

    @ApiModelProperty(" 排序")
    private Integer sortOrder;

    @ApiModelProperty(" 分类描述")
    private String catDesc;

    @ApiModelProperty(" 搜索关键词")
    private String keywords;

    @ApiModelProperty(" 别名")
    private String catAlias;


}
