package com.soubao.config.security;

import com.alibaba.fastjson.JSON;
import com.soubao.service.AuthService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    @Autowired
    private AuthService authService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 配置order访问控制，必须认证后才可以访问
        http.authorizeRequests().antMatchers("/order/**").authenticated().
                and().logout().logoutUrl("/logout").logoutSuccessHandler(
                ((request, response, authentication) -> {
                    String accessToken = request.getHeader("Authorization");
                    response.setContentType("application/json;charset=UTF-8");
                    String resultJson;
                    if (!StringUtils.isEmpty(accessToken)) {
                        resultJson = authService.revokeToken(accessToken.replace("Bearer", "").trim());
                    } else {
                        resultJson = JSON.toJSONString(new SBApi());
                    }
                    response.getWriter().write(resultJson);
                })
        ).deleteCookies("JSESSIONID", "Authorization");
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 定义异常转换类生效
        AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        ((OAuth2AuthenticationEntryPoint) authenticationEntryPoint).setExceptionTranslator(new Auth2ResponseExceptionTranslator());
        resources.authenticationEntryPoint(authenticationEntryPoint);
    }
}
