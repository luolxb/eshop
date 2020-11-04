package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.FlashSale;
import org.apache.ibatis.annotations.Param;

public interface FlashSaleMapper extends BaseMapper<FlashSale> {
    //抢购活动分页
    IPage<FlashSale> selectFlashSaleGoodsPage(Page<FlashSale> page, @Param(Constants.WRAPPER) QueryWrapper<FlashSale> wrapper);
}