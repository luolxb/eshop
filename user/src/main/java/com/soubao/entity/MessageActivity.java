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
@TableName("message_activity")
@ApiModel("活动消息")
public class MessageActivity extends MessageBase implements Serializable {

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
    @ApiModelProperty("活动结束时间")
    private Long endTime;
    @ApiModelProperty("用户消息模板编号")
    private String mmtCode;
    @ApiModelProperty("1抢购2团购3优惠促销4预售5虚拟6拼团7搭配购8自定义图文消息9订单促销")
    private Integer promType;
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

    @TableField(exist = false)
    @ApiModelProperty("商品id")
    private Integer goodsId;

    @TableField(exist = false)
    @ApiModelProperty("活动开始时间")
    private Long startTime;

    @TableField(exist = false)
    @ApiModelProperty("活动是否结束")
    private Integer isFinish;
}
