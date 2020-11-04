package com.soubao.config.security;

import com.soubao.vo.SBApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

@Slf4j
@FrameworkEndpoint
public class RevokeTokenEndpoint {

    @Autowired
    @Qualifier("consumerTokenServices")
    ConsumerTokenServices consumerTokenServices;

    @DeleteMapping("/token/revoke")
    @ResponseBody
    public SBApi revokeToken(@RequestParam(value = "access_token") String accessToken) {
        SBApi sbApi = new SBApi();
        if (consumerTokenServices.revokeToken(accessToken)){
            sbApi.setStatus(1);
            sbApi.setMsg("退出成功");
        }else{
            sbApi.setStatus(0);
            sbApi.setMsg("退出失败");
        }
        return sbApi;
    }
}
