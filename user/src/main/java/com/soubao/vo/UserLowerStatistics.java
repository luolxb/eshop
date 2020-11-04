package com.soubao.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "用户下线人数统计", description = "一级,二级,三级")
@Data
public class UserLowerStatistics {
    @ApiModelProperty(value = "一级总数")
    private Integer firstLowerCount;
    @ApiModelProperty(value = "二级总数")
    private Integer secondLowerCount;
    @ApiModelProperty(value = "三级总数")
    private Integer thirdLowerCount;
}
