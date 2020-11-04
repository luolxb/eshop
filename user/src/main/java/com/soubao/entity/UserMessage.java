package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_message")
@ApiModel("用户消息对象")
public class UserMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户消息id")
    @TableId(value = "rec_id", type = IdType.AUTO)
    private Integer recId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("消息id")
    private Integer messageId;

    @ApiModelProperty("通知消息：0, 活动消息：1, 物流:2, 私信:3")
    private Integer category;

    @ApiModelProperty("是否查看：0未查看, 1已查看")
    private Integer isSee;

    @ApiModelProperty("用户假删除标识,1:删除,0未删除")
    private Integer deleted;

    @ApiModelProperty("查看状态：0未查看，1已查看")
    private Integer status;

}
