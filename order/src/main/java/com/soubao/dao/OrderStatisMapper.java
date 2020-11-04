package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.OrderStatis;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商家订单结算表 Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-12-19
 */
public interface OrderStatisMapper extends BaseMapper<OrderStatis> {
    //分页查询商家结算记录
    IPage<OrderStatis> selectStatisPage(Page<OrderStatis> page, @Param(Constants.WRAPPER) QueryWrapper<OrderStatis> wrapper);
}
