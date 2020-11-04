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

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-08-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("message_private")
@ApiModel("私信消息")
public class MessagePrivate extends MessageBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "message_id", type = IdType.AUTO)
    private Integer messageId;
    @ApiModelProperty("消息内容")
    private String messageContent;
    @ApiModelProperty("发送时间")
    private Long sendTime;
    @ApiModelProperty("发送者")
    private Integer sendUserId;

    @TableField(exist = false)
    @ApiModelProperty("用户消息id")
    private Integer recId;

    @TableField(exist = false)
    @ApiModelProperty("是否查看：0未查看, 1已查看")
    private Integer isSee;
}
