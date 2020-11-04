package com.soubao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.MessagePrivate;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-27
 */
public interface MessagePrivateService extends IService<MessagePrivate> {

    IPage<MessagePrivate> userMessagePage(Page<MessagePrivate> page, Integer userId);
}
