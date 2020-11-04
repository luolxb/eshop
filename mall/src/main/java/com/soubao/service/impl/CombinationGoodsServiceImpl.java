package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.*;
import com.soubao.dao.CombinationGoodsMapper;
import com.soubao.entity.CombinationGoods;
import com.soubao.service.CombinationGoodsService;
import com.soubao.service.GoodsService;
import com.soubao.service.SpecGoodsPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 组合促销商品映射关系表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-05-19
 */
@Service
public class CombinationGoodsServiceImpl extends ServiceImpl<CombinationGoodsMapper, CombinationGoods> implements CombinationGoodsService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SpecGoodsPriceService specGoodsPriceService;

    @Override
    public CombinationGoods getMaster(List<CombinationGoods> combinationGoodsList) {
        for (CombinationGoods combinationGoods : combinationGoodsList) {
            if (combinationGoods.getIsMaster()) return combinationGoods;
        }
        return null;
    }

    @Override
    public Cart changeToCart(CombinationGoods combinationGoods) {
        Cart cart = new Cart();
        cart.setGoodsId(combinationGoods.getGoodsId());
        cart.setGoodsSn(combinationGoods.getGoodsSku().getGoodsSn());
        cart.setGoodsName(combinationGoods.getGoodsName());
        cart.setMarketPrice(combinationGoods.getGoodsSku().getMarketPrice());
        cart.setGoodsPrice(combinationGoods.getOriginalPrice());
        cart.setMemberGoodsPrice(combinationGoods.getPrice());
        cart.setSku(combinationGoods.getGoodsSku().getSku());
        cart.setItemId(combinationGoods.getGoodsSku().getItemId());
        cart.setPromType(7);
        cart.setPromId(combinationGoods.getCombinationId());
        cart.setSpecKey(combinationGoods.getGoodsSku().getSpecKey());
        cart.setSpecKeyName(combinationGoods.getGoodsSku().getSpecKeyName());
        cart.setStoreId(combinationGoods.getGoodsSku().getStoreId());
        return cart;
    }

    @Override
    public void withGoodsSku(List<CombinationGoods> combinationGoodsList) {
        Set<Integer> goodsIds = new HashSet<>();
        Set<Long> itemIds = new HashSet<>();
        for (CombinationGoods combinationGoods : combinationGoodsList) {
            if (combinationGoods.getItemId() > 0) {
                itemIds.add(combinationGoods.getItemId());
            } else {
                goodsIds.add(combinationGoods.getGoodsId());
            }
        }
        Map<Integer, Goods> goodsMap = new HashMap<>();
        Map<Long, SpecGoodsPrice> itemMap = new HashMap<>();
        if (goodsIds.size() > 0) {
            goodsMap = goodsService.list(new QueryWrapper<Goods>()
                    .in("goods_id", goodsIds)).stream().collect(Collectors.toMap(Goods::getGoodsId, goods -> goods));
        }
        if (itemIds.size() > 0) {
            itemMap = specGoodsPriceService.list(new QueryWrapper<SpecGoodsPrice>()
                    .in("item_id", itemIds)).stream().collect(Collectors.toMap(SpecGoodsPrice::getItemId, spec -> spec));
        }
        for (CombinationGoods combinationGoods : combinationGoodsList) {
            GoodsSku goodsSku = new GoodsSku(goodsMap.get(combinationGoods.getGoodsId()),
                    itemMap.getOrDefault(combinationGoods.getItemId(), null));
            combinationGoods.setGoodsSku(goodsSku);
        }
    }

    //将商品恢复成普通商品
    @Override
    public void recoveryPromTypes(List<CombinationGoods> combinationGoods) {
        Set<Integer> specGoodsIds = new HashSet<>(); //有商品规格的goodsId
        Set<Integer> goodsIds = new HashSet<>(); //没商品规格的goodsId
        Set<Integer> itemIds = new HashSet<>();
        for (CombinationGoods cg : combinationGoods) {
            if (cg.getItemId() > 0) {
                itemIds.add(cg.getItemId().intValue());
                specGoodsIds.add(cg.getGoodsId());
            } else {
                goodsIds.add(cg.getGoodsId());
            }
        }
        goodsService.recoveryPromTypes(goodsIds,itemIds,specGoodsIds);
    }

    @Override
    public void setGoodsPromType(List<CombinationGoods> combinationGoods) {
        Integer combinationId = combinationGoods.get(0).getCombinationId();
        Set<Integer> goodsIds = combinationGoods
                .stream()
                .map(CombinationGoods::getGoodsId)
                .collect(Collectors.toSet());
        Set<Long> itemIds = combinationGoods
                .stream()
                .filter(cg -> cg.getItemId() > 0)
                .map(CombinationGoods::getItemId)
                .collect(Collectors.toSet());
        if (goodsIds.size() > 0) {
            goodsService.update(new UpdateWrapper<Goods>()
                    .set("prom_id", combinationId)
                    .set("prom_type", 7)
                    .in("goods_id", goodsIds));
            if (itemIds.size() > 0) {
                specGoodsPriceService.update(new UpdateWrapper<SpecGoodsPrice>()
                        .set("prom_id", combinationId)
                        .set("prom_type", 7)
                        .in("item_id", itemIds));
                goodsService.update(new UpdateWrapper<Goods>()
                        .set("prom_type", 7)
                        .in("goods_id", goodsIds));
            }
        }
    }
}
