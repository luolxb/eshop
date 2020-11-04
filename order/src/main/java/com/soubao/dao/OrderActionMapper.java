package com.soubao.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.OrderAction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-09-25
 */
public interface OrderActionMapper extends BaseMapper<OrderAction> {
    //查询订单日志分页
    IPage<OrderAction> selectOrderActionPage(Page<OrderAction> page, @Param("startTime") Long startTime,
                                             @Param("endTime") Long endTime, @Param("adminId") Integer adminId);
}
