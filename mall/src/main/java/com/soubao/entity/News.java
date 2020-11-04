package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.utils.TimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("news")
@ApiModel(value = "新闻对象", description = "news表")
public class News implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "新闻主键")
    @TableId(value = "article_id", type = IdType.AUTO)
    private Integer articleId;

    @ApiModelProperty("类别ID")
    @NotNull(message = "所属分类必须选择")
    private Integer catId;

    @ApiModelProperty("文章标题")
    @NotBlank(message = "新闻标题不能为空")
    private String title;

    @ApiModelProperty("审核状态 0未审核  1通过 2拒绝")
    private Integer checkType;

    @ApiModelProperty("审核理由描述")
    private String checkDescribe;

    @ApiModelProperty("新闻标签")
    @NotBlank(message = "新闻标签不能为空")
    private String tags;

    @ApiModelProperty("新闻内容")
    @NotBlank(message = "文章内容不能为空")
    private String content;

    public String getContent(){
        if (!StringUtils.isEmpty(content)) {
            return HtmlUtils.htmlUnescape(content);
        }
        return null;
    }

    @ApiModelProperty("作者id（用户id）")
    private Integer userId;

    @ApiModelProperty("文章作者")
    private String author;

    @ApiModelProperty("作者邮箱")
    private String authorEmail;

    @ApiModelProperty("关键字")
    private String keywords;

    private Integer articleType;

    @ApiModelProperty("是否显示")
    private Integer isOpen;

    @ApiModelProperty("添加时间")
    private Long addTime;

    @ApiModelProperty("附件地址")
    private String fileUrl;

    private Integer openType;

    @ApiModelProperty("链接地址")
    private String link;

    @ApiModelProperty("文章摘要")
    private String description;

    @ApiModelProperty("浏览量")
    private Integer click;

    @ApiModelProperty("文章预告发布时间")
    private Long publishTime;

    @ApiModelProperty("文章缩略图")
    private String thumb;

    @ApiModelProperty("文章缩略图2")
    private String thumb2;

    @ApiModelProperty("文章缩略图3")
    private String thumb3;

    @ApiModelProperty("新闻来源  发布新闻的管理员角色id")
    private Integer pickupId;

    @ApiModelProperty("新闻列表显示样式 0不显示图片 1左显示，2右显示 3上显示 4下显示")
    @NotNull(message = "请选择导图展现样式")
    private Integer type;

    @ApiModelProperty("新闻来源 、0=总平台 1=商家 2=用户")
    private Integer source;


    @ApiModelProperty("新闻类别对象")
    @TableField(exist = false)
    private NewsCat newsCat;

    @ApiModelProperty("新闻来源描述")
    @TableField(exist = false)
    private String sourceDesc;

    @ApiModelProperty("审核状态描述")
    @TableField(exist = false)
    private String checkTypeDesc;

    public String getCheckTypeDesc() {
        if (checkType != null) {
            switch (checkType) {
                case 0:
                    return "未审核";
                case 1:
                    return "通过";
                case 2:
                    return "拒绝";
            }
        }
        return checkTypeDesc;
    }

    @ApiModelProperty("新闻发布时间描述")
    @TableField(exist = false)
    private String publishTimeDesc;
    public String getPublishTimeDesc(){
        return TimeUtil.transForDateStr(publishTime, "yyyy-MM-dd");
    }

    @ApiModelProperty("新闻标签描述")
    @TableField(exist = false)
    private String tagsDesc;
    public String getTagsDesc(){
        Map<String, String>tagsMap = new HashMap<>();
        tagsMap.put("0", "最新");
        tagsMap.put("1", "热门");
        tagsMap.put("2", "推荐");
        tagsMap.put("3", "精品");
        if (!StringUtils.isEmpty(tags)){
            List<String> tagsList = Arrays.asList(tags.split(","));
            StringBuilder sb = new StringBuilder();
            for (int i = 0;i<tagsList.size();i++) {
                if (i != 0){
                    sb.append(" ");
                }
                sb.append("[");
                sb.append(tagsMap.get(tagsList.get(i)));
                sb.append("]");
            }
            return sb.toString();
        }
        return tags;
    }

}
