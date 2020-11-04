package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.utils.TimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("system_article")
@ApiModel("系统文章对象")
public class SystemArticle implements Serializable {
    @ApiModelProperty("id")
    @TableId(value = "doc_id", type = IdType.AUTO)
    private Integer docId;

    @ApiModelProperty("调用标识码")
    @NotBlank(message = "标识码不能为空")
    private String docCode;

    @ApiModelProperty("标题")
    @NotBlank(message = "标题不能为空")
    private String docTitle;

    @ApiModelProperty("添加时间/修改时间")
    private Long docTime;

    @ApiModelProperty("内容")
    @NotBlank(message = "帮助内容不能为空")
    private String docContent;

    public String getDocContent() {
        if (StringUtils.isNotEmpty(docContent)) {
            return HtmlUtils.htmlUnescape(docContent);
        }
        return null;
    }

    @TableField(exist = false)
    private String docTimeDesc;
    public String getDocTimeDesc(){
        return TimeUtil.transForDateStr(docTime, "yyyy-MM-dd HH:mm:ss");
    }

    private static final long serialVersionUID = 1L;
}