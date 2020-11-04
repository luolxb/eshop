package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

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


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("help")
@ApiModel(value = "帮助对象", description = "help表")
public class Help implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("帮助ID")
    @TableId(value = "help_id", type = IdType.AUTO)
    private Integer helpId;

    @ApiModelProperty("排序")
    private Integer helpSort;

    @ApiModelProperty("标题")
    @NotBlank(message = "标题不能为空")
    private String helpTitle;

    @ApiModelProperty("帮助内容")
    @NotBlank(message = "帮助内容不能为空")
    private String helpInfo;

    public String getHelpInfo(){
        if (!StringUtils.isEmpty(helpInfo)) {
            return HtmlUtils.htmlUnescape(helpInfo);
        }
        return null;
    }

    @ApiModelProperty("跳转链接")
    private String helpUrl;

    @ApiModelProperty("更新时间")
    private Long addTime;

    @ApiModelProperty("帮助类型")
    @NotNull(message = "所属分类必须选择")
    private Integer typeId;

    @ApiModelProperty("页面类型:1为店铺,2为会员,默认为1")
    private Integer pageShow;

    @ApiModelProperty("是否显示")
    private Integer isShow;

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty("更新时间描述")
    @TableField(exist = false)
    private String addTimeDesc;
    public String getAddTimeDesc(){
        return TimeUtil.transForDateStr(addTime, "yyyy-MM-dd HH:mm");
    }

    @ApiModelProperty("帮助类型对象")
    @TableField(exist = false)
    private HelpType helpType;


}
