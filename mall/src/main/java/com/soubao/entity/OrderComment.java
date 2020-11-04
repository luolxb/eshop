package com.soubao.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_comment")
@ApiModel(value = "订单评分对象", description = "order_comment表")
public class OrderComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单评分主键")
    @TableId(value = "order_commemt_id", type = IdType.AUTO)
    private Integer orderCommentId;
    @ApiModelProperty(value = "订单表主键")
    private Integer orderId;
    @ApiModelProperty(value = "店铺表主键")
    private Integer storeId;
    @ApiModelProperty(value = "用户表主键")
    private Integer userId;
    @ApiModelProperty(value = "描述相符分数（0~5）")
    private BigDecimal describeScore;
    @ApiModelProperty(value = "卖家服务分数（0~5）")
    private BigDecimal sellerScore;
    @ApiModelProperty(value = "物流服务分数（0~5）")
    private BigDecimal logisticsScore;
    @ApiModelProperty(value = "评分时间")
    @TableField("commemt_time")
    private Long commentTime;
    @ApiModelProperty(value = "不删除0；删除：1")
    private Integer deleted;
}
