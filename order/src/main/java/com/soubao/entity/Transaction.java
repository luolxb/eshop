package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("`transaction`")
public class Transaction {

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商品ID")
    private Integer goodsId;

    @ApiModelProperty(value = "买家用户者ID")
    private Integer buyerId;

    @ApiModelProperty(value = "卖家用户者ID")
    private Integer sellerId;

    @ApiModelProperty(value = "订单ID")
    private Integer orderId;

    @ApiModelProperty(value = "购买时间")
    private long purchaseTime;

    @ApiModelProperty(value = "状态：0代表未支付，1代表已支付，2代表取消")
    private byte status;
}
