package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.OrderComment;
import com.soubao.entity.Store;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单评分表 Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-08-12
 */
public interface OrderCommentMapper extends BaseMapper<OrderComment> {

    Store selectStoreScore(@Param("store") Store store);
}
