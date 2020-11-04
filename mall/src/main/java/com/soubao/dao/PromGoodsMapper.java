package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Goods;
import com.soubao.entity.PromGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-08-19
 */
public interface PromGoodsMapper extends BaseMapper<PromGoods> {

    IPage<Goods> goodsPage(Page<PromGoods> page, @Param(Constants.WRAPPER) QueryWrapper<PromGoods> wrapper);
}
