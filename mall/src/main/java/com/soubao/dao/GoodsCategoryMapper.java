package com.soubao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soubao.entity.GoodsCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsCategoryMapper extends BaseMapper<GoodsCategory> {
    /**
     * 获取根据一级分类获取对应的二三级分类
     */
    List<GoodsCategory> selectSecAndThirdCategoryListByFirstId(@Param("id") Integer id);

    /**
     * 根据品牌和商品分类内连接
     * @return
     */
    List<GoodsCategory> selectCategoryWithBrand();

    /**
     * 查询商品分类带上商品模型
     * @return
     */
    List<GoodsCategory> selectGoodsCategoryWithGoodsType(Integer parentId);
}