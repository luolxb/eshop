package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.Goods;
import com.soubao.entity.SpecGoodsPrice;
import com.soubao.service.SpecGoodsPriceService;
import com.soubao.dao.SpecGoodsPriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-19
 */
@Service("specGoodsPriceService")
public class SpecGoodsPriceServiceImpl extends ServiceImpl<SpecGoodsPriceMapper, SpecGoodsPrice> implements SpecGoodsPriceService {
    @Autowired
    private SpecGoodsPriceMapper specGoodsPriceMapper;

    @Override
    public Set<Integer> getSpecItemIds(List<SpecGoodsPrice> specGoodsPrices) {
        Set<Integer> specItemIds = new HashSet<>();
        for(SpecGoodsPrice specGoodsPrice : specGoodsPrices){
            for (String specIdStr : specGoodsPrice.getKey().split("_")) {
                specItemIds.add(Integer.parseInt(specIdStr));
            }
        }
        return specItemIds;
    }

    @Override
    public void updateSpecGoodsPriceList(Goods goods, List<SpecGoodsPrice> specGoodsPriceList) {
        if(specGoodsPriceList != null){
            if(specGoodsPriceList.size() == 0){
                remove((new QueryWrapper<SpecGoodsPrice>()).eq("goods_id", goods.getGoodsId()));
            }else{
                for(SpecGoodsPrice specGoodsPrice :specGoodsPriceList){
                    specGoodsPrice.setGoodsId(goods.getGoodsId());
                }
                List<SpecGoodsPrice> exSpecGoodsPriceList = list((new QueryWrapper<SpecGoodsPrice>()).eq("goods_id", goods.getGoodsId()));
                if(exSpecGoodsPriceList.size() > 0){
                    Map<String, SpecGoodsPrice> specGoodsPriceKey = specGoodsPriceList.stream().collect(Collectors.toMap(SpecGoodsPrice::getKey, specGoodsPrice -> specGoodsPrice));
                    ListIterator<SpecGoodsPrice> exSpecGoodsPriceIt = exSpecGoodsPriceList.listIterator();
                    Set<Long> deleteItemIds = new HashSet<>();
                    while(exSpecGoodsPriceIt.hasNext()){
                        SpecGoodsPrice specGoodsPrice = exSpecGoodsPriceIt.next();
                        if(!specGoodsPriceKey.containsKey(specGoodsPrice.getKey())){
                            deleteItemIds.add(specGoodsPrice.getItemId());
                        }else{
                            specGoodsPriceKey.get(specGoodsPrice.getKey()).setItemId(specGoodsPrice.getItemId());
                        }
                    }
                    if(deleteItemIds.size() > 0){
                        remove((new QueryWrapper<SpecGoodsPrice>()).in("item_id", deleteItemIds));
                    }
                }
                saveOrUpdateBatch(specGoodsPriceList);
            }
        }
    }

    @Override
    public void addSpecGoodsPriceList(Goods goods, List<SpecGoodsPrice> specGoodsPriceList) {
        if(specGoodsPriceList != null && specGoodsPriceList.size() > 0){
            for(SpecGoodsPrice specGoodsPrice : specGoodsPriceList){
                specGoodsPrice.setGoodsId(goods.getGoodsId());
                specGoodsPrice.setStoreId(goods.getStoreId());
            }
            saveBatch(specGoodsPriceList);
        }
    }

}
