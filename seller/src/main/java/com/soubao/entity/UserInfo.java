package com.soubao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-08-15
 */
@Data
public class UserInfo {

    /**
     * 邮件
     */
    //@Email
    @JsonProperty(value = "email")
    private String email;

    /**
     * 密码
     */
    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(value = "regTime")
    private Long regTime;

    @JsonProperty(value = "mobile")
    private String mobile;


    @JsonProperty(value = "nickName")
    private String nickName;


}
