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

import javax.validation.constraints.NotBlank;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("article_cat")
@ApiModel(value = "文章分类对象", description = "article_cat表")
public class ArticleCat implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键索引")
    @TableId(value = "cat_id", type = IdType.AUTO)
    private Integer catId;

    @ApiModelProperty("类别名称")
    @NotBlank(message = "分类名不能为空")
    private String catName;

    @ApiModelProperty("默认分组")
    private Integer catType;

    @ApiModelProperty("夫级ID")
    private Integer parentId;

    @ApiModelProperty("是否导航显示")
    private Integer showInNav;

    @ApiModelProperty("排序")
    private Integer sortOrder;

    @ApiModelProperty("分类描述")
    private String catDesc;

    @ApiModelProperty("搜索关键词")
    private String keywords;

    @ApiModelProperty("别名")
    private String catAlias;

    @ApiModelProperty("子级列表节点")
    @TableField(exist = false)
    private List<ArticleCat> children;

    @ApiModelProperty("文章列表")
    @TableField(exist = false)
    private List<Article> articles;
}
