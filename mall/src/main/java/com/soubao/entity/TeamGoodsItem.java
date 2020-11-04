package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("team_goods_item")
@ApiModel("拼团商品对象")
public class TeamGoodsItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("活动id")
    private Integer teamId;

    @ApiModelProperty("商品id")
    private Integer goodsId;

    @ApiModelProperty("规格id")
    private Integer itemId;

    @ApiModelProperty("拼团价")
    private BigDecimal teamPrice;

    @ApiModelProperty("已拼数量")
    private Integer salesSum;

    @ApiModelProperty("是否已删除0否，1删除")
    private Boolean deleted;

    @ApiModelProperty("购买数量")
    @TableField(exist = false)
    private Integer goodsNum;

    @ApiModelProperty("团长id")
    @TableField(exist = false)
    private Integer foundId;

    @ApiModelProperty("商品sku名称")
    @TableField(exist = false)
    private String keyName;

    @ApiModelProperty("商品sku库存")
    @TableField(exist = false)
    private Integer storeCount;

    @ApiModelProperty("商品sku价格")
    @TableField(exist = false)
    private BigDecimal shopPrice;
}
