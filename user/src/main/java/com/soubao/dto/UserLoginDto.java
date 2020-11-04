package com.soubao.dto;

import com.soubao.validation.annotation.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import javax.validation.constraints.NotEmpty;

@ApiModel("用户登录")
@Getter
@Setter
public class UserLoginDto {
    @Phone
    @ApiModelProperty("手机")
    private String mobile;
    @NotEmpty
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("图形验证码")
    private String verification;
    @ApiModelProperty("短信验证码")
    private String code;
}
