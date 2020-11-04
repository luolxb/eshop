package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Goods;
import com.soubao.entity.SpecGoodsPrice;

import java.util.List;
import java.util.Set;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-19
 */

public interface SpecGoodsPriceService extends IService<SpecGoodsPrice> {

    /**
     * 获取商品规格id组
     * @param specGoodsPrices
     * @return
     */
    Set<Integer> getSpecItemIds(List<SpecGoodsPrice> specGoodsPrices);

    /**
     * 商家更新规格
     * @param goods
     * @param specGoodsPriceList
     */
    void updateSpecGoodsPriceList(Goods goods, List<SpecGoodsPrice> specGoodsPriceList);

    /**
     * 商家添加规格
     * @param goods
     * @param specGoodsPriceList
     */
    void addSpecGoodsPriceList(Goods goods, List<SpecGoodsPrice> specGoodsPriceList);
}
