package com.soubao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Setter
@Getter
public class DepositCertificatePublisher {

    @JsonProperty(value = "user_id")
    private Integer userId;  //这里的通证发行者ID，就是seller端的店铺ID

    //@Email
    private String email;


    private String mobile;

    @JsonProperty(value = "nike_name")
    private String nikeName;


    private String password;

    @JsonProperty(value = "company_id")
    private Integer companyId;  //这里的公司ID，就是seller端的组ID
}
