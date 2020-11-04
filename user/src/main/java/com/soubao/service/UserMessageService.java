package com.soubao.service;

import com.soubao.entity.MessageBase;
import com.soubao.entity.UserMessage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyr
 * @since 2020-04-08
 */
public interface UserMessageService extends IService<UserMessage> {

    MessageBase getMessageBase(UserMessage userMessage);
}
