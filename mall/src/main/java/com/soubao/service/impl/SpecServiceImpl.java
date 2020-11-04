package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.Spec;
import com.soubao.entity.SpecGoodsPrice;
import com.soubao.entity.SpecItem;
import com.soubao.service.SpecGoodsPriceService;
import com.soubao.service.SpecService;
import com.soubao.dao.SpecMapper;
import com.soubao.common.utils.ShopStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-22
 */
@Service("specService")
public class SpecServiceImpl extends ServiceImpl<SpecMapper, Spec> implements SpecService {
    @Autowired
    private SpecGoodsPriceService specGoodsPriceService;
    @Autowired
    private SpecMapper specMapper;
    @Override
    public List<Spec> selectSpecsWithItem(List<SpecGoodsPrice> specGoodsPriceList) {
        List<Spec> specs = new ArrayList<>();
        Set<Integer> specItemIds = specGoodsPriceService.getSpecItemIds(specGoodsPriceList);
        if (specItemIds.size() > 0) {
            specs = specMapper.selectSpecWithItem(specItemIds);
        }
        return specs;
    }

    @Override
    public List<Spec> selectSpecsWithItem(Set<Integer> specItemIds) {
        return specMapper.selectSpecWithItem(specItemIds);
    }

    @Override
    public List<Spec> getStoreBindSpecAndItem(Integer storeId, Integer typeId) {
        return specMapper.selectStoreBindSpecAndItem(storeId, typeId);
    }

    @Override
    public List<Spec> selectSpecByTypeId(Integer typeId) {
        return specMapper.selectSpecByTypeId(typeId);
    }

    @Override
    public List<SpecGoodsPrice> getSpecGoodsPriceListBySpecItemIds(Set<Integer> specItemIds, Integer goodsId) {
        List<Spec> specList = selectSpecsWithItem(specItemIds);
        List<List<Integer>> specItemIdList = new ArrayList<>();
        Map<Integer, String> specItemIdToSpecNameMap = new HashMap<>();
        Map<Integer, String> specItemIdToSpecItemNameMap = new HashMap<>();
        for(Spec spec : specList){
            List<Integer> listSub = new ArrayList<>();
            for(SpecItem specItem : spec.getSpecItemList()){
                listSub.add(specItem.getId());
                specItemIdToSpecNameMap.put(specItem.getId(), spec.getName());
                specItemIdToSpecItemNameMap.put(specItem.getId(), specItem.getItem());
            }
            specItemIdList.add(listSub);
        }
        List<List<Integer>> specKeyList = new ArrayList<>();
        ShopStringUtil.descartes(specItemIdList, specKeyList, 0, new ArrayList<>());
        List<SpecGoodsPrice> specGoodsPriceList = new ArrayList<>();
        for (List<Integer> specItemIdArr : specKeyList) {
            Collections.sort(specItemIdArr);
            List<String> keyNameArr =  new ArrayList<>();
            for(Integer specItemId : specItemIdArr){
                keyNameArr.add(specItemIdToSpecNameMap.get(specItemId) + ":" + specItemIdToSpecItemNameMap.get(specItemId));
            }
            String key = org.apache.commons.lang3.StringUtils.join(specItemIdArr, "_");
            String keyName = org.apache.commons.lang3.StringUtils.join(keyNameArr, " ");
            SpecGoodsPrice specGoodsPrice = new SpecGoodsPrice();
            specGoodsPrice.setKey(key);
            specGoodsPrice.setKeyName(keyName);
            specGoodsPrice.setPrice(BigDecimal.ZERO);
            specGoodsPrice.setStoreCount(0);
            specGoodsPrice.setSku("");
            specGoodsPrice.setCost(BigDecimal.ZERO);
            specGoodsPriceList.add(specGoodsPrice);
        }
        if(!StringUtils.isEmpty(goodsId)){
            List<SpecGoodsPrice> exSpecGoodsPriceList = specGoodsPriceService.list((new QueryWrapper<SpecGoodsPrice>()).eq("goods_id", goodsId));
            for(SpecGoodsPrice exSpecGoodsPrice : exSpecGoodsPriceList){
                for(int index = 0; index < specGoodsPriceList.size();index ++){
                    if(specGoodsPriceList.get(index).getKey().equals(exSpecGoodsPrice.getKey())){
                        specGoodsPriceList.set(index, exSpecGoodsPrice);
                    }
                }
            }
        }
        return specGoodsPriceList;
    }

}
