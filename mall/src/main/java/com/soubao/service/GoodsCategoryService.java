package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.GoodsCategory;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-13
 */
public interface GoodsCategoryService extends IService<GoodsCategory> {
    //根据一级分类id查找二级和三级分类，树状结构
    List<GoodsCategory> getSecAndThirdCategoryListByFirstId(Integer id);

    GoodsCategory getGoodsCategoryByGoodsListFilterUrl(String url);

    List<GoodsCategory> listToTree(List<GoodsCategory> list);

    /**
     * 获取商品列表带上商品模型
     * @return
     */
    List<GoodsCategory> getGoodsCategoryWithGoodsType();
    /**
     * 获取商品列表带上商品模型
     * @return
     */
    List<GoodsCategory> getGoodsCategoryWithGoodsType(Integer parentId);
    /**
     * 获取商品列表带上商品品牌
     * @return
     */
    List<GoodsCategory> getCategoryWithBrand();

    /**
     * 根据商品分类id删除
     * @param id
     */
    void deleteById(Integer id);

    //更新商品分类
    void updateGoodsCategory(GoodsCategory goodsCategory);
}
