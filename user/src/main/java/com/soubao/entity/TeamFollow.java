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

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("team_follow")
@ApiModel("参团对象")
public class TeamFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("参团id")
    @TableId(value = "follow_id", type = IdType.AUTO)
    private Integer followId;

    @ApiModelProperty("参团会员id")
    private Integer followUserId;

    @ApiModelProperty("参团会员昵称")
    private String followUserNickname;

    @ApiModelProperty("会员头像")
    private String followUserHeadPic;

    @ApiModelProperty("参团时间")
    private Long followTime;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("订单编号")
    private String orderSn;

    @ApiModelProperty("开团ID")
    private Integer foundId;

    @ApiModelProperty("开团人user_id")
    private Integer foundUserId;

    @ApiModelProperty("拼团活动id")
    private Integer teamId;

    @ApiModelProperty("参团状态0:待拼单(表示已下单但是未支付)1拼单成功(已支付)2成团成功3成团失败")
    private Integer status;

    @ApiModelProperty("抽奖团是否中奖")
    private Boolean isWin;

    @ApiModelProperty("订单节点")
    @TableField(exist = false)
    private Order order;

}
