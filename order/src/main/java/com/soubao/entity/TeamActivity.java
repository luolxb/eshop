package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("拼团活动对象")
public class TeamActivity {

    @ApiModelProperty("拼团活动id")
    @TableId(value = "team_id", type = IdType.AUTO)
    private Integer teamId;

    @ApiModelProperty("拼团活动类型,0分享团1佣金团2抽奖团")
    private Integer teamType;
}
