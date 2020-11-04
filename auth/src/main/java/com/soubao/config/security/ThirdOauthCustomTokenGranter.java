package com.soubao.config.security;

import com.soubao.service.CustomUserDetailsService;
import com.soubao.entity.User;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.Map;

public class ThirdOauthCustomTokenGranter extends AbstractCustomTokenGranter {

    protected CustomUserDetailsService userDetailsService;

    public ThirdOauthCustomTokenGranter(CustomUserDetailsService userDetailsService, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, "third_oauth");
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected User getUser(Map<String, String> parameters) {
        String oauth = parameters.get("oauth");
        String openid = parameters.get("openid");
        return userDetailsService.loadUserByThirdOauth(oauth, openid);
    }

}