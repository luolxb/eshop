package com.soubao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.MessageActivity;
import com.soubao.entity.UserMessage;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-08-26
 */
public interface MessageActivityMapper extends BaseMapper<MessageActivity> {

    IPage<MessageActivity> selectUserMessagePage(Page<MessageActivity> page, UserMessage userMessage);
}
