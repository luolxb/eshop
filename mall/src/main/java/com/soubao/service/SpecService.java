package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Spec;
import com.soubao.entity.SpecGoodsPrice;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-22
 */
public interface SpecService extends IService<Spec> {
    //根据商品规格价格列表获取商品规格项
    List<Spec> selectSpecsWithItem(List<SpecGoodsPrice> specGoodsPriceList);

    /**
     * 根据规格项id组获取规格及其子项
     * @param specItemIds
     * @return
     */
    List<Spec> selectSpecsWithItem(Set<Integer> specItemIds);

    /**
     * 获取商家绑定的商品规格及其子项
     * @param storeId
     * @param typeId
     * @return
     */
    List<Spec> getStoreBindSpecAndItem(Integer storeId, Integer typeId);

    /**
     * 查找某商品模型下的规格列表
     * @param typeId
     * @return
     */
    List<Spec> selectSpecByTypeId(Integer typeId);

    /**
     * 根据规格子项id获取spec_goods_price列表
     * @return
     */
    List<SpecGoodsPrice> getSpecGoodsPriceListBySpecItemIds(Set<Integer> specItemIds, Integer goodsId);
}
