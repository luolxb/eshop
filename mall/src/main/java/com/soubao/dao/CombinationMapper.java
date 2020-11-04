package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Combination;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 组合促销表 Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2020-05-19
 */
public interface CombinationMapper extends BaseMapper<Combination> {

    IPage<Combination> selectCombinationPage(Page<Combination> combinationPage, @Param(Constants.WRAPPER) QueryWrapper<Combination> wrapper);
}
