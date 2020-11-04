package com.soubao.controller;

import com.soubao.vo.MailVo;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/mail")
@Api(value = "邮件控制器", tags = {"邮件相关控制器接口"})
public class MailController {

    @PreAuthorize("hasAnyRole('ADMIN')")
    @ApiOperation("发送邮件")
    @PostMapping
    public SBApi sendSimpleMail(@RequestBody MailVo mailVo) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();//直接生产一个实例
        mailSender.setHost(mailVo.getHost());//动态添加配置
        mailSender.setPassword(mailVo.getSenderPwd());
        mailSender.setPort(mailVo.getPort());
        mailSender.setProtocol(mailVo.getProtocol());
        mailSender.setUsername(mailVo.getSender());
        //正常为25 smtp端口的smtp服务器设置为465端口时需要此配置
//        Properties prop = new Properties();
//        prop.put("mail.smtp.ssl.enable", true);
//        mailSender.setJavaMailProperties(prop);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailVo.getSender());
        message.setTo(mailVo.getReceiver()); // 使用数组的形式还可以群发
        message.setSubject(mailVo.getSubject());
        message.setText(mailVo.getText());
        mailSender.send(message);
        return new SBApi();
    }

}
