package com.soubao.entity;

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
@TableName("team_lottery")
@ApiModel("拼团抽奖对象")
public class TeamLottery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("幸运儿手机")
    private Integer userId;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("订单编号")
    private String orderSn;

    @ApiModelProperty("幸运儿手机")
    private String mobile;

    @ApiModelProperty("拼团活动ID")
    private Integer teamId;

    @ApiModelProperty("会员昵称")
    private String nickname;

    @ApiModelProperty("幸运儿头像")
    private String headPic;

}
