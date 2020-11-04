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
@TableName("message_logistics")
@ApiModel("物流消息")
public class MessageLogistics extends MessageBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "message_id", type = IdType.AUTO)
    private Integer messageId;

    @ApiModelProperty("消息标题")
    private String messageTitle;
    @ApiModelProperty("消息内容")
    private String messageContent;
    @ApiModelProperty("图片地址")
    private String imgUri;
    @ApiModelProperty("发送时间")
    private Long sendTime;
    @ApiModelProperty("单号")
    private String orderSn;
    @ApiModelProperty("物流订单id")
    private Integer orderId;
    @ApiModelProperty("用户消息模板编号")
    private String mmtCode;
    @ApiModelProperty("1到货通知2发货提醒3签收提醒4评价提醒5退货提醒6退款提醒")
    private Integer type;
    @ApiModelProperty("店铺ID")
    private Integer storeId;

    @TableField(exist = false)
    @ApiModelProperty("用户消息id")
    private Integer recId;

    @TableField(exist = false)
    @ApiModelProperty("是否查看：0未查看, 1已查看")
    private Integer isSee;
}
