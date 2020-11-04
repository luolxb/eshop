package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Goods;
import com.soubao.vo.Stock;
import org.apache.ibatis.annotations.Param;


public interface GoodsMapper extends BaseMapper<Goods> {

    //获取用户未上架商品数量
    int selectNotAddGoodsCount(@Param("userId") Integer userId);

    void stock(@Param("goods") Goods goods);

    //查询分销商微店分销商品分页
    IPage<Goods> selectPageMyShopGoods(Page<Goods> page, @Param(Constants.WRAPPER) QueryWrapper<Goods> queryWrapper);

    IPage<Stock> selectStockPage(Page<Stock> page, @Param(Constants.WRAPPER) QueryWrapper<Stock> stockQueryWrapper);

}