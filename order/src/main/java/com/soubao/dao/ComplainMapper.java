package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Complain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 投诉表 Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2020-02-28
 */
public interface ComplainMapper extends BaseMapper<Complain> {

    IPage<Complain> getComplainPage(Page<Complain> page, @Param(Constants.WRAPPER) QueryWrapper<Complain> queryWrapper);
}
