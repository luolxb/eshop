package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Brand;
import com.soubao.entity.GoodsCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-08-20
 */
public interface BrandMapper extends BaseMapper<Brand> {

    List<Brand> selectAttrForGoodsList(@Param("goodsCategory") GoodsCategory goodsCategory);

    IPage<Brand> selectBrandPage(Page<Brand> page, @Param(Constants.WRAPPER) QueryWrapper<Brand> brandQueryWrapper);
}
