package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.soubao.dto.UserLoginDto;
import com.soubao.entity.OauthUser;
import com.soubao.entity.User;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.AuthService;
import com.soubao.service.OauthUserService;
import com.soubao.service.MallService;
import com.soubao.service.UserService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Api(value = "用户登录注册控制器", tags = {"用户登录注册相关"})
@Slf4j
@RestController
public class IndexController {
    @Autowired
    private MallService mallService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private OauthUserService oauthUserService;
    @Value("${security.oauth2.client.scope}")
    private String scope;
    @Value("${security.oauth2.client.id}")
    private String clientId;
    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @ApiOperation(value = "小程序登录接口")
    @PostMapping("login/{oauth}")
    public String thirdLogin(
            @PathVariable(value = "oauth") String oauth,//miniapp
            @NotNull @RequestParam(value = "js_code") String jsCode,
//                          @RequestParam(value = "iv", defaultValue = "") String iv,
//                          @RequestParam(value = "signature", defaultValue = "") String signature,//签名
//                          @RequestParam(value = "encryptedData", defaultValue = "") String encryptedData,
            @Validated User loginUser) {
        OauthUser oauthUser = oauthUserService.getOauthUserByCode(jsCode);
        User user = userService.loginAndGetUser(loginUser, oauthUser);
        String result = authService.getTokenByThirdAuth("third_oauth", scope, clientId,
                clientSecret, oauth, oauthUser.getOpenid());
        userService.update(new UpdateWrapper<User>().eq("user_id", user.getUserId())
                .set("last_login", System.currentTimeMillis() / 1000));
        return result;
    }

    @ApiOperation(value = "H5注册接口")
    @PostMapping("register")
    public SBApi register(@RequestBody User registerUser) {
        userService.register(registerUser);
        return new SBApi();
    }

    @ApiOperation(value = "H5登录接口")
    @PostMapping("login")
    public String login(@RequestBody UserLoginDto loginUser) {
        String result;
        if (!StringUtils.isEmpty(loginUser.getCode())){
            //短信验证码登录
            result = authService.getTokenBySms("sms", scope, clientId,
                    clientSecret, loginUser.getMobile(), loginUser.getCode());
        }else{
            //密码登录
            if (StringUtils.isEmpty(loginUser.getVerification()) || !authService.verify(loginUser.getVerification())) {
                throw new ShopException(ResultEnum.VERIFY_CODE_ERROR);
            }
            result = authService.getToken("password", scope, clientId,
                    clientSecret, "{mobile}" + loginUser.getMobile(), loginUser.getPassword());
        }
        userService.update(new UpdateWrapper<User>().set("last_login", System.currentTimeMillis() / 1000).eq("mobile", loginUser.getMobile()));
        return result;
    }

    @PostMapping("oauth_user")
    public SBApi addOauthUser(OauthUser oauthUser,
                              User user,
                              @RequestParam(value = "code", required = false) String code,//短信验证码,
                              SBApi sbApi) {
        mallService.checkCode(user.getMobile(), code);
        sbApi.setResult(userService.addOauthUserReturnUser(oauthUser, user));
        return sbApi;
    }

}
