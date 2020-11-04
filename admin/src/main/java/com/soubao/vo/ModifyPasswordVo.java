package com.soubao.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class ModifyPasswordVo implements Serializable {
    @NotBlank(message = "不能为空")
    private String newPassword;
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
}
