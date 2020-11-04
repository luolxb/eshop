package com.soubao.vo;

import com.soubao.entity.Goods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDistributionGoodsVo extends Goods {
    @ApiModelProperty("用户分销商品主键")
    private Integer id;
    @ApiModelProperty("分享次数")
    private Integer shareNum;
    @ApiModelProperty("分销销量")
    private Integer shareSalesNum;
    @ApiModelProperty("加入个人分销库时间")
    private Integer addTime;

}
