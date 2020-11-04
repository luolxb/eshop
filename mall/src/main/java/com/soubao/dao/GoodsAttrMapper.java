package com.soubao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soubao.entity.GoodsAttr;
import com.soubao.entity.GoodsCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-08-15
 */
public interface GoodsAttrMapper extends BaseMapper<GoodsAttr> {
    List<GoodsAttr> selectAttrByGoodsId(@Param("goods_id") Integer goodsId);

    List<GoodsAttr> selectAttrForGoodsList(@Param("goodsCategory") GoodsCategory goodsCategory, @Param("noInAttrIds") Set<Integer> noInAttrIds);
}
