package com.soubao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.dto.MessageDto;
import com.soubao.entity.MessageNotice;

/**
 * 服务类
 *
 * @author dyr
 * @since 2019-08-26
 */
public interface MessageNoticeService extends IService<MessageNotice> {
    IPage<MessageNotice> userMessagePage(Page<MessageNotice> page, Integer userId);

    // 商家发送站内信
    void sendNoticeMsg(MessageDto messageDto);
}
