package com.soubao.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2020-03-25
 */
@Setter
@Getter
public class OauthUser {
    private Integer tuId;
    private Integer userId;
    private String openid;
    private String oauth;
    private String unionid;
    private String oauthChild;
}
