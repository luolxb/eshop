package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-08-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("account_log")
@ApiModel(value = "资金变动日志对象", description = "accountLog表")
public class AccountLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "日志表id")
    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "用户金额")
    @NotNull(message = "金额不能为空")
    private BigDecimal userMoney;

    @ApiModelProperty(value = "冻结金额")
    @NotNull(message = "冻结资金不能为空")
    private BigDecimal frozenMoney;

    @ApiModelProperty(value = "支付积分")
    @NotNull(message = "积分不能为空")
    private Integer payPoints;

    @ApiModelProperty(value = "变动时间")
    private Long changeTime;

    @NotBlank(message = "请填写操作备注")
    @ApiModelProperty(value = "描述")
    @TableField("`desc`")
    private String desc;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "订单id")
    private Integer orderId;

}
