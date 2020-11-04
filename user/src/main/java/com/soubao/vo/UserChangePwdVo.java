package com.soubao.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@ApiModel("用户修改密码请求数据")
public class UserChangePwdVo {
    @ApiModelProperty("新密码")
    @NotEmpty
    private String newPassWord;
    @ApiModelProperty("旧密码")
    @NotEmpty
    private String oldPassWord;
}
