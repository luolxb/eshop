package com.soubao.vo;

import com.soubao.common.utils.RandomUtil;
import lombok.Data;

@Data
public class MailVo {
    private String host;
    private Integer port = 25;
    private String protocol = "smtp";
    private String sender;
    private String senderPwd;
    private String receiver;
    private String subject = "后台测试";
    private String text = "测试发送验证码：" + RandomUtil.createRandomString(4);
}
