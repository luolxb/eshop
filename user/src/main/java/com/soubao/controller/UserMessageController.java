package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.*;
import com.soubao.service.*;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import com.soubao.vo.UserMessageTab;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-04-08
 */
@RestController
@RequestMapping("/message")
public class UserMessageController {
    @Autowired
    private MessageNoticeService messageNoticeService;
    @Autowired
    private MessageActivityService messageActivityService;
    @Autowired
    private MessageLogisticsService messageLogisticsService;
    @Autowired
    private MessagePrivateService messagePrivateService;
    @Autowired
    private UserMessageService userMessageService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @ApiOperation(value = "用户消息统计数")
    @GetMapping("count")
    public Integer count(@ApiParam("状态:1已读") @RequestParam(value = "is_see", defaultValue = "0") Integer isSee) {
        User user = authenticationFacade.getPrincipal(User.class);
        return userMessageService.count(new QueryWrapper<UserMessage>().eq("is_see", isSee).eq("user_id", user.getUserId()));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("设置消息已读")
    @PutMapping("/is_see")
    public SBApi deleteMessage(@ApiParam("消息id") @RequestParam(value = "rec_id") Integer recId) {
        User user = authenticationFacade.getPrincipal(User.class);
        userMessageService.update(new UpdateWrapper<UserMessage>().set("is_see", 1).eq("user_id", user.getUserId()).eq("rec_id", recId));
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("清空消息")
    @DeleteMapping
    public SBApi deleteMessage(@RequestBody UserMessage userMessage) {
        User user = authenticationFacade.getPrincipal(User.class);
        UpdateWrapper<UserMessage> userMessageUpdateWrapper = new UpdateWrapper<>();
        userMessageUpdateWrapper.set("deleted", 1).eq("user_id", user.getUserId());
        if (userMessage.getRecId() != null && userMessage.getRecId() > 0) {
            userMessageUpdateWrapper.eq("rec_id", userMessage.getRecId());
        }
        if (userMessage.getCategory() != null) {
            userMessageUpdateWrapper.eq("category", userMessage.getCategory());
        }
        userMessageService.update(userMessageUpdateWrapper);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("获取消息条目(总数与最新一条消息)")
    @GetMapping("/tab")
    public UserMessageTab userMessageTab(
            @ApiParam("消息类型：0通知消息,1活动消息,2物流,3私信") @RequestParam(value = "category")
                    Set<Integer> category) {
        User user = authenticationFacade.getPrincipal(User.class);
        UserMessageTab userMessageTab = new UserMessageTab();
        QueryWrapper<UserMessage> userMessageCountQueryWrapper = new QueryWrapper<>();
        userMessageCountQueryWrapper
                .eq("user_id", user.getUserId())
                .eq("is_see", 0)
                .eq("deleted", 0)
                .in("category", category);
        userMessageTab.setCount(userMessageService.count(userMessageCountQueryWrapper));
        if (userMessageTab.getCount() != 0) {
            UserMessage userMessage =
                    userMessageService.getOne(
                            (new QueryWrapper<UserMessage>())
                                    .eq("user_id", user.getUserId())
                                    .eq("deleted", 0)
                                    .in("category", category)
                                    .orderByDesc("rec_id")
                                    .last("limit 1"));
            MessageBase messageBase = userMessageService.getMessageBase(userMessage);
            userMessageTab.setMsg(messageBase.getMessageContent());
            userMessageTab.setTime(messageBase.getSendTime());
        }
        return userMessageTab;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("通知消息分页")
    @GetMapping("/notice/page")
    public Page<MessageNotice> messageNoticePage(
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "16") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);;
        return (Page<MessageNotice>)
                messageNoticeService.userMessagePage(new Page<>(page, size), user.getUserId());
    }

    @ApiOperation("通知消息")
    @GetMapping("/notice")
    public MessageNotice messageNotice(@RequestParam(value = "rec_id") Integer recId) {
        UserMessage userMessage = userMessageService.getById(recId);
        return userMessage != null
                ? messageNoticeService.getById(userMessage.getMessageId())
                : (new MessageNotice());
    }

    @ApiOperation("活动消息")
    @GetMapping("/activity")
    public MessageActivity messageActivity(@RequestParam(value = "rec_id") Integer recId) {
        UserMessage userMessage = userMessageService.getById(recId);
        return userMessage != null
                ? messageActivityService.getById(userMessage.getMessageId())
                : (new MessageActivity());
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("物流消息分页")
    @GetMapping("/message/logistics/page")
    public Page<MessageLogistics> messageLogisticsPage(
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "16") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        return (Page<MessageLogistics>)
                messageLogisticsService.userMessagePage(new Page<>(page, size), user.getUserId());
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("活动消息分页")
    @GetMapping("/activity/page")
    public Page<MessageActivity> messageActivityPage(
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "16") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        return (Page<MessageActivity>)
                messageActivityService.userMessagePage(new Page<>(page, size), user.getUserId());
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("私信消息分页")
    @GetMapping("/message/private/page")
    public Page<MessagePrivate> messagePrivatePage(
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "16") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        return (Page<MessagePrivate>)
                messagePrivateService.userMessagePage(new Page<>(page, size), user.getUserId());
    }

}
