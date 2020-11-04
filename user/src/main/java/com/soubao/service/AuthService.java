package com.soubao.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth")
public interface AuthService {

    @GetMapping("/current")
    String getAdmin();

    @PostMapping("/verification")
    Boolean verify(@RequestParam("verification") String verification);

    @PostMapping("/oauth/token")
    String getToken(@RequestParam("grant_type") String grantType,
                    @RequestParam("scope") String scope,
                    @RequestParam("client_id") String clientId,
                    @RequestParam("client_secret") String clientSecret,
                    @RequestParam("username") String username,
                    @RequestParam("password") String password
    );

    @PostMapping("/oauth/token")
    String getTokenBySms(@RequestParam("grant_type") String grantType,
                    @RequestParam("scope") String scope,
                    @RequestParam("client_id") String clientId,
                    @RequestParam("client_secret") String clientSecret,
                    @RequestParam("mobile") String mobile,
                    @RequestParam("smscode") String smscode
    );

    @PostMapping("/oauth/token")
    String getTokenByThirdAuth(@RequestParam("grant_type") String grantType,
                         @RequestParam("scope") String scope,
                         @RequestParam("client_id") String clientId,
                         @RequestParam("client_secret") String clientSecret,
                         @RequestParam("oauth") String oauth,
                         @RequestParam("openid") String openid
    );

    @DeleteMapping("/token/revoke")
    String revokeToken(@RequestParam("access_token") String accessToken);
}
