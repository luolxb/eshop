package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.GoodsCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-08-22
 */
public interface GoodsCollectMapper extends BaseMapper<GoodsCollect> {

    IPage<GoodsCollect> selectCollectPage(Page<GoodsCollect> collectPage, @Param(Constants.WRAPPER) QueryWrapper<GoodsCollect> wrapper);
}
