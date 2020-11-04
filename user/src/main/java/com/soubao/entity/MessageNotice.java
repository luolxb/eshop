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
 * @since 2019-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("message_notice")
@ApiModel("消息通知")
public class MessageNotice extends MessageBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "message_id", type = IdType.AUTO)
    private Integer messageId;
    @ApiModelProperty("个体消息：0，全体消息:1")
    private Integer messageType;
    @ApiModelProperty("消息标题")
    private String messageTitle;
    @ApiModelProperty("消息内容")
    private String messageContent;
    @ApiModelProperty("发送时间")
    private Long sendTime;
    @ApiModelProperty("用户消息模板编号")
    private String mmtCode;
    @ApiModelProperty("0系统公告1降价通知2优惠券到账提醒3优惠券使用提醒4优惠券即将过期提醒5预售订单尾款支付提醒6提现到账提醒")
    private Integer type;
    @ApiModelProperty("活动id")
    private Integer promId;
    @ApiModelProperty("店铺ID")
    private Integer storeId;

    @TableField(exist = false)
    @ApiModelProperty("用户消息id")
    private Integer recId;

    @TableField(exist = false)
    @ApiModelProperty("是否查看：0未查看, 1已查看")
    private Integer isSee;

}
