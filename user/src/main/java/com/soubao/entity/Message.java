package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author dyr
 * @since 2019-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 消息ID */
    @TableId(value = "message_id", type = IdType.AUTO)
    private Integer messageId;

    /** 管理员id */
    private Integer adminId;

    /** 商家管理员id */
    private Integer sellerId;

    /** 站内信内容 */
    private String message;

    /** 个体消息：0，全体消息：1 */
    private Integer type;

    /** 0系统消息，1物流通知，2优惠促销，3商品提醒，4我的资产，5商城好店 */
    private Integer category;

    /** 发送时间 */
    private Integer sendTime;

    /** 消息序列化内容 */
    private String data;
}
