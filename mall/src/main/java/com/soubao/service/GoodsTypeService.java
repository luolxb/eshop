package com.soubao.service;

import com.soubao.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyr
 * @since 2019-11-20
 */
public interface GoodsTypeService extends IService<GoodsType> {

    /**
     * 商品模型关联商品分类
     * @param goodsType
     * @param goodsCategoryList
     */
    void bindGoodsGoodsCategoryList(GoodsType goodsType, List<GoodsCategory> goodsCategoryList);

    /**
     * 商品模型关联商品属性
     * @param goodsType
     * @param goodsAttributes
     */
    void bindGoodsAttribute(GoodsType goodsType, List<GoodsAttribute> goodsAttributes);

    /**
     * 商品模型关联商品规格
     * @param goodsType
     * @param specList
     */
    void bindSpec(GoodsType goodsType, List<Spec> specList);

    /**
     * 商品模型绑定商品品牌
     * @param goodsType
     * @param brandTypeList
     */
    void bindBand(GoodsType goodsType, List<BrandType> brandTypeList);

    /**
     * 绑定
     * @param goodsType
     */
    void updateForBind(GoodsType goodsType);
}
