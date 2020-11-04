package com.soubao.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "用户分销商品统计概况")
public class UserDistributionSurveyVo {
    @ApiModelProperty("已添加的分销商品数")
    private Integer hadAddGoodsNum;
    @ApiModelProperty("未添加的分销商品数")
    private Integer noAddGoodsNum;
}
