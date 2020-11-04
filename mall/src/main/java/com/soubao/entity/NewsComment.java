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


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("news_comment")
@ApiModel(value = "新闻评论对象", description = "news_comment表")
public class NewsComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;

    @ApiModelProperty("新闻ID")
    private Integer articleId;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("审核状态 0未审核  1通过 2拒绝")
    private Integer checkType;

    @ApiModelProperty("审核理由描述")
    private String checkDescribe;

    @ApiModelProperty("0正常显示，1虚拟删除不显示")
    private Integer isDelete;

    @ApiModelProperty("评论时间")
    private Long addTime;

    @ApiModelProperty("点赞量")
    private Integer likeNum;

    @ApiModelProperty("评论者名称")
    @TableField(exist = false)
    private String commenter;

    @ApiModelProperty("评论时间描述")
    @TableField(exist = false)
    private String addTimeDesc;
    public String getAddTimeDesc(){
        if (addTime != null) {
            return TimeUtil.transForDateStr(addTime, "yyyy-MM-dd HH:mm:ss");
        }
        return addTimeDesc;
    }

}
