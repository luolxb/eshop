package com.soubao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.MessageLogistics;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-26
 */
public interface MessageLogisticsService extends IService<MessageLogistics> {

    IPage<MessageLogistics> userMessagePage(Page<MessageLogistics> page, Integer userId);
}
