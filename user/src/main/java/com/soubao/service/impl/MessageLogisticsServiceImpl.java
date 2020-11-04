package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.MessageLogisticsMapper;
import com.soubao.entity.MessageLogistics;
import com.soubao.entity.UserMessage;
import com.soubao.service.MessageLogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-26
 */
@Service
public class MessageLogisticsServiceImpl extends ServiceImpl<MessageLogisticsMapper, MessageLogistics> implements MessageLogisticsService {

    @Autowired
    private MessageLogisticsMapper messageLogisticsMapper;

    @Override
    public IPage<MessageLogistics> userMessagePage(Page<MessageLogistics> page, Integer userId) {
        UserMessage userMessage = new UserMessage();
        userMessage.setUserId(userId);
        userMessage.setDeleted(0);
        return messageLogisticsMapper.selectUserMessagePage(page, userMessage);
    }
}
