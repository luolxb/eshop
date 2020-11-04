package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.CouponList;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-09-02
 */
public interface CouponListMapper extends BaseMapper<CouponList> {

    IPage<CouponList> getCouponListPage(Page<CouponList> page, @Param(Constants.WRAPPER) QueryWrapper<CouponList> queryWrapper);
}
