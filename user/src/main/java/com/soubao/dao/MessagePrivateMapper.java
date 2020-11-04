package com.soubao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.MessagePrivate;
import com.soubao.entity.UserMessage;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-08-27
 */
public interface MessagePrivateMapper extends BaseMapper<MessagePrivate> {

    IPage<MessagePrivate> selectUserMessagePage(Page<MessagePrivate> page, UserMessage userMessage);
}
