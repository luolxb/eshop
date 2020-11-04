package com.soubao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.MessageActivity;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-26
 */
public interface MessageActivityService extends IService<MessageActivity> {

    IPage<MessageActivity> userMessagePage(Page<MessageActivity> page, Integer userId);
}
