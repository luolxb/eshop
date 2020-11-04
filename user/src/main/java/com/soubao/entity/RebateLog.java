package com.soubao.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author dyr
 * @since 2019-10-28
 */
@Setter
@Getter
public class RebateLog {

    @ApiModelProperty("自增id")
    private Integer id;

    @ApiModelProperty("获佣用户")
    private Integer userId;

    @ApiModelProperty("购买人id")
    private Integer buyUserId;

    @ApiModelProperty("购买人名称")
    private String nickname;

    @ApiModelProperty("订单编号")
    private String orderSn;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("订单商品总额")
    private BigDecimal goodsPrice;

    @ApiModelProperty("获佣金额")
    private BigDecimal money;

    @ApiModelProperty("获佣用户级别")
    private Integer level;

    @ApiModelProperty("分成记录生成时间")
    private Long createTime;

    @ApiModelProperty("确定收货时间")
    private Integer confirm;

    @ApiModelProperty("0未付款,1已付款, 2等待分成(已收货) 3已分成, 4已取消")
    private Integer status;

    @ApiModelProperty("确定分成或者取消时间")
    private Long confirmTime;

    @ApiModelProperty("如果是取消, 有取消备注")
    private String remark;

    @ApiModelProperty("店铺id")
    private Integer storeId;

}
