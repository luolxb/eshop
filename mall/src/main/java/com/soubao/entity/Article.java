package com.soubao.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.soubao.common.utils.TimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "文章对象", description = "article表")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章id")
    @TableId(value = "article_id", type = IdType.AUTO)
    private Integer articleId;

    @ApiModelProperty(value = "类别ID")
    @NotNull(message = "所属分类必须选择")
    private Integer catId;

    @ApiModelProperty(value = "文章标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty(value = "文章作者")
    private String author;

    @ApiModelProperty(value = "作者邮箱")
    private String authorEmail;

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "文章类别")
    private Integer articleType;

    @ApiModelProperty(value = "是否开启")
    private Integer isOpen;

    @ApiModelProperty(value = "添加时间")
    private Long addTime;

    @ApiModelProperty(value = "附件地址")
    private String fileUrl;

    @ApiModelProperty(value = "开启类型")
    private Integer openType;

    @ApiModelProperty(value = "链接地址")
    private String link;

    @ApiModelProperty(value = "浏览量")
    private Integer click;

    @ApiModelProperty(value = "文章预告发布时间")
    private Integer publishTime;

    @ApiModelProperty(value = "文章缩略图")
    private String thumb;

    @ApiModelProperty(value = "文章内容")
    @NotBlank(message = "内容不能为空")
    private String content;

    public String getContent(){
        if (!StringUtils.isEmpty(content)) {
            return HtmlUtils.htmlUnescape(content);
        }
        return null;
    }

    @ApiModelProperty(value = "文章摘要")
    private String description;


    @ApiModelProperty("发布时间描述")
    @TableField(exist = false)
    private String addTimeDesc;

    public String getAddTimeDesc(){
        if (addTime != null) {
            return TimeUtil.transForDateStr(addTime, "yyyy-MM-dd HH:mm:ss");
        }
        return addTimeDesc;
    }

    @ApiModelProperty("文章分类对象")
    @TableField(exist = false)
    private ArticleCat articleCat;

}