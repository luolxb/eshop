package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.soubao.entity.Admin;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.AdminService;
import com.soubao.service.AuthService;
import com.soubao.common.utils.ShopStringUtil;
import com.soubao.vo.LoginAdminVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class IndexController {
    @Value("${security.oauth2.client.scope}")
    private String scope;
    @Value("${security.oauth2.client.id}")
    private String clientId;
    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;
    @Autowired
    private AuthService authService;
    @Autowired
    private AdminService adminService;


    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/index")
    public String index() {
       return "你好~，叼大的管理员";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginAdminVo loginAdminVo, HttpServletRequest request) {
        if(!authService.verify(loginAdminVo.getVerification())){
            throw new ShopException(ResultEnum.VERIFY_ERROR);
        }
//        log.info("password|"+scope+"|"+clientId+"|"+clientSecret+"|"+"{admin}" + loginAdminVo.getUserName() +"|" + loginAdminVo.getPassword());
        String resultString = authService.getToken("password", scope, clientId,
                clientSecret, "{admin}" + loginAdminVo.getUserName(), loginAdminVo.getPassword());
        adminService.update((new UpdateWrapper<Admin>()).set("last_login", System.currentTimeMillis() / 1000)
                .set("last_ip", ShopStringUtil.getIpAddr(request))
                .eq("user_name", loginAdminVo.getUserName()));
        return resultString;
    }
}
