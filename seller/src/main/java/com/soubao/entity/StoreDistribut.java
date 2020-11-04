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
@TableName("store_distribut")
@ApiModel("店铺分销设置对象")
public class StoreDistribut implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("分销开关")
    @TableField("`switch`")
    private Integer switchs;

    @ApiModelProperty("成为分销商条件 0 直接成为分销商,1成功购买商品后成为分销商")
    @TableField("`condition`")
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
