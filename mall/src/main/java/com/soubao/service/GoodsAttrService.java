package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Goods;
import com.soubao.entity.GoodsAttr;
import com.soubao.entity.GoodsCategory;
import com.soubao.vo.GoodsListAttrFilter;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-15
 */
public interface GoodsAttrService extends IService<GoodsAttr> {
    //根据商品id获取商品的属性和
    List<GoodsAttr> getAttrListByGoodsId(Integer goodsId);

    //根据前端传递的属性参数来获取要查询的商品id
    Set<Integer> getGoodsIdsByAttr(String attrGroupStr);

    //商品列表页的属性筛选链接组
    List<GoodsListAttrFilter> getGoodsListFilterForAttr(String url, GoodsCategory goodsCategory);

    /**
     * 商家更新商品属性
     * @param goods
     * @param goodsAttrs
     */
    void updateGoodsAttr(Goods goods, List<GoodsAttr> goodsAttrs);

    /**
     * 商家添加属性
     * @param goods
     * @param goodsAttrs
     */
    void addGoodsAttr(Goods goods, List<GoodsAttr> goodsAttrs);
}
