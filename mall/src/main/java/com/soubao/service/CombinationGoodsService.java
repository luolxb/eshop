package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Cart;
import com.soubao.entity.CombinationGoods;

import java.util.List;

/**
 * <p>
 * 组合促销商品映射关系表 服务类
 * </p>
 *
 * @author dyr
 * @since 2020-05-19
 */
public interface CombinationGoodsService extends IService<CombinationGoods> {
    CombinationGoods getMaster(List<CombinationGoods> combinationGoodsList);

    Cart changeToCart(CombinationGoods combinationGoods);

    void withGoodsSku(List<CombinationGoods> combinationGoodsList);

    void setGoodsPromType(List<CombinationGoods> combinationGoods);

    void recoveryPromTypes(List<CombinationGoods> combinationGoodsList);
}
