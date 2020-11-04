package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.MessageNoticeMapper;
import com.soubao.dto.MessageDto;
import com.soubao.entity.*;
import com.soubao.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 服务实现类
 *
 * @author dyr
 * @since 2019-08-26
 */
@Service
public class MessageNoticeServiceImpl extends ServiceImpl<MessageNoticeMapper, MessageNotice>
        implements MessageNoticeService {
    @Autowired private MessageNoticeMapper messageNoticeMapper;
    @Autowired private MemberMsgTplService memberMsgTplService;
    @Autowired private UserService userService;
    @Autowired private MallService mallService;
    @Autowired private UserMessageService userMessageService;
    @Autowired private StoreCollectService storeCollectService;

    @Override
    public IPage<MessageNotice> userMessagePage(Page<MessageNotice> page, Integer userId) {
        UserMessage userMessage = new UserMessage();
        userMessage.setUserId(userId);
        userMessage.setDeleted(0);
        return messageNoticeMapper.selectUserMessagePage(page, userMessage);
    }

    @Override
    public void sendNoticeMsg(MessageDto messageDto) {
        MemberMsgTpl memberMsgTpl = memberMsgTplService.getOne(new QueryWrapper<MemberMsgTpl>()
                .eq("mmt_code", "message_notice"));
        if (memberMsgTpl.getMmtMessageSwitch() == 1) {
            List<User> userList;
            String name2;
            if (messageDto.getStoreId() == 0) {
                name2 = "管理员";
            } else {
                name2 = "店铺：<" + mallService.getStore(messageDto.getStoreId()).getStoreName() + ">";
            }
            if (messageDto.getType() == 0) { // 全体会员消息
                // 判断是商家发还是总平台
                if (messageDto.getStoreId() == 0) {
                    userList = userService.list();
                } else {
                    // 获取所有关注本店铺的会员
                    Set<Integer> userIds = storeCollectService.storeCollectList(messageDto.getStoreId())
                            .stream().map(StoreCollect::getUserId).collect(Collectors.toSet());
                    userList = userService.list(new QueryWrapper<User>().in("user_id", userIds));
                }
            } else {
                userList = userService.list(new QueryWrapper<User>().in("user_id", messageDto.getUserIds()));
            }
            ArrayList<MessageNotice> messageNoticeList = new ArrayList<>();
            if (StringUtils.isEmpty(messageDto.getContent())) { // 发送内容为空时，自动使用模板消息
                String mmtMessageContent = memberMsgTpl.getMmtMessageContent(); // 获取内容模板
                replaceTemplateToList(userList, messageDto, mmtMessageContent, name2,
                        messageNoticeList, memberMsgTpl.getMmtName());
            } else {
                replaceTemplateToList( userList, messageDto, messageDto.getContent(), name2,
                        messageNoticeList,
                        memberMsgTpl.getMmtName());
            }
            // 批量插入通知消息
            saveBatch(messageNoticeList);
            // 批量插入到用户消息表
            List<UserMessage> userMessageList = new ArrayList<>();
            for (int i = 0; i < userList.size(); i++) {
                UserMessage userMessage = new UserMessage();
                userMessage.setUserId(userList.get(i).getUserId());
                userMessage.setMessageId(messageNoticeList.get(i).getMessageId());
                userMessage.setCategory(0);
                userMessageList.add(userMessage);
            }
            userMessageService.saveBatch(userMessageList);
        }
    }

    // 替换消息模板内容并添加到集合
    private void replaceTemplateToList(
            List<User> userList,
            MessageDto messageDto,
            String contentTemp,
            String name2,
            List<MessageNotice> messageNoticeList,
            String titleTemplate) {
        Integer type = messageDto.getType();
        long sendTime = System.currentTimeMillis() / 1000;
        for (User user : userList) {
            MessageNotice messageNotice = new MessageNotice();
            // 标题无占位符无需替换
            if (StringUtils.isEmpty(messageDto.getTitle())) {
                messageNotice.setMessageTitle(titleTemplate);
            } else {
                messageNotice.setMessageTitle(messageDto.getTitle());
            }
            String content =
                    contentTemp.replace("{$name}", user.getNickname()).replace("{$name2}", name2);
            messageNotice.setMessageContent(content);
            messageNotice.setMessageType(type);
            messageNotice.setType(0);
            messageNotice.setMmtCode("message_notice");
            messageNotice.setStoreId(messageDto.getStoreId());
            messageNotice.setSendTime(sendTime);
            messageNoticeList.add(messageNotice);
        }
    }
}
