package com.soubao.vo;

import com.soubao.validation.annotation.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@ApiModel("用户修改支付密码请求数据")
public class UserChangePayPwdVo {
    @ApiModelProperty("支付密码")
    @NotEmpty
    private String paypwd;
    @ApiModelProperty("手机号")
    @Phone
    private String mobile;
    @ApiModelProperty("短信验证码")
    @NotEmpty
    private String verification;
}
