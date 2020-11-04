package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.GoodsConsult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soubao.entity.Order;
import com.soubao.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-11-27
 */
public interface GoodsConsultMapper extends BaseMapper<GoodsConsult> {
    IPage<GoodsConsult> selectConsultPage(Page<GoodsConsult> page, @Param(Constants.WRAPPER) QueryWrapper<GoodsConsult> wrapper);

    IPage<GoodsConsult> selectUserConsultPage(Page<GoodsConsult> goodsConsultPage, @Param("user") User user);
}
