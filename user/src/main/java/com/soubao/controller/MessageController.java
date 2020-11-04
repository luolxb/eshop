package com.soubao.controller;

import com.soubao.dto.MessageDto;
import com.soubao.service.MessageNoticeService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/message")
@Api(
        value = "消息控制器",
        tags = {"消息相关控制器接口"})
public class MessageController {
    @Autowired private MessageNoticeService messageNoticeService;

    @PreAuthorize("hasAnyRole('SELLER,ADMIN')")
    @PostMapping("send_notice_msg")
    public SBApi sellerSendMsg(@RequestBody MessageDto messageDto) {
        messageNoticeService.sendNoticeMsg(messageDto);
        return new SBApi();
    }

}
