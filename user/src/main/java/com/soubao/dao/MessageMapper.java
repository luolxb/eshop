package com.soubao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soubao.entity.Message;

/**
 * Mapper 接口
 *
 * @author dyr
 * @since 2019-08-26
 */
public interface MessageMapper extends BaseMapper<Message> {
    // 获取消息列表
    //    IPage<Message> selectMessageList(Page Page, @Param("category") Integer category,
    // @Param("userId") Integer userId);
}
