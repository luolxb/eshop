package com.soubao.service.impl;

import com.soubao.dao.UserMessageMapper;
import com.soubao.entity.MessageBase;
import com.soubao.entity.UserMessage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-04-08
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements UserMessageService {

    @Autowired
    private MessageNoticeService messageNoticeService;
    @Autowired
    private MessagePrivateService messagePrivateService;
    @Autowired
    private MessageActivityService messageActivityService;
    @Autowired
    private MessageLogisticsService messageLogisticsService;

    @Override
    public MessageBase getMessageBase(UserMessage userMessage) {
        MessageBase messageBase = null;
        if(userMessage.getCategory() == 0){
            messageBase = messageNoticeService.getById(userMessage.getMessageId());
        }
        if(userMessage.getCategory() == 1){
            messageBase = messageActivityService.getById(userMessage.getMessageId());
        }
        if(userMessage.getCategory() == 2){
            messageBase = messageLogisticsService.getById(userMessage.getMessageId());
        }
        if(userMessage.getCategory() == 3){
            messageBase = messagePrivateService.getById(userMessage.getMessageId());
        }
        return messageBase;
    }
}
