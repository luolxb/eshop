package com.soubao.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("店铺分销设置对象")
public class StoreDistribut {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("分销开关")
    private Integer switchs;

    @ApiModelProperty("成为分销商条件 0 直接成为分销商,1成功购买商品后成为分销商")
    private Integer condition;

    @ApiModelProperty("返佣级数0一级1两级2三级")
    private Integer regrade;

    @ApiModelProperty("一级分销商比例")
    private Integer firstRate;

    @ApiModelProperty("二级分销商名称")
    private Integer secondRate;

    @ApiModelProperty("三级分销商名称")
    private Integer thirdRate;

    @ApiModelProperty("订单收货确认后多少天可以分成")
    private Integer date;

    @ApiModelProperty("店铺id")
    private Integer storeId;
    
}
