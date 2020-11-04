package com.soubao.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel("用户提现申请对象")
@Data
public class UserWithdrawalDto {
    @ApiModelProperty("提现金额")
    @NotNull(message = "提现金额不能为空")
    @Min(value = 1, message = "提现金额不能小于1")
    private BigDecimal money;

    @ApiModelProperty("银行账号或支付宝账号")
    @NotBlank(message = "银行账号不能为空")
    private String bankCard;

    @ApiModelProperty("银行名称 如支付宝 微信 中国银行 农业银行等")
    @NotBlank(message = "银行名称不能为空")
    private String bankName;

    @ApiModelProperty("提现类型 : 0余额提现,1佣金提现")
    @Min(value = 0, message = "提现类型错误")
    @Max(value = 1, message = "提现类型错误")
    private Integer type;

    @ApiModelProperty("支付密码")
    @NotBlank(message = "支付密码不能为空")
    private String payPwd;

    @ApiModelProperty("提款账号真实姓名")
    @NotBlank(message = "提款账号真实姓名不能为空")
    private String realname;
}
