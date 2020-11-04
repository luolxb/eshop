package com.soubao.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "用户本月签到统计", description = "用户本月签到统计")
public class UserSignStatisticsVo {
    @ApiModelProperty(value = "本月积分")
    private Integer monthPoint;
    @ApiModelProperty(value = "累计积分")
    private Integer totalPoint;
    @ApiModelProperty(value = "连续天数")
    private Integer consecutiveDays;
    @ApiModelProperty(value = "签到日期")
    private String signTime;
}
