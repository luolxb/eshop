package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.StoreBindClassMapper;
import com.soubao.entity.GoodsCategory;
import com.soubao.entity.Store;
import com.soubao.entity.StoreBindClass;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.MallService;
import com.soubao.service.MallService;
import com.soubao.service.StoreBindClassService;
import com.soubao.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 店铺可发布商品类目表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-11-25
 */
@Service
public class StoreBindClassServiceImpl extends ServiceImpl<StoreBindClassMapper, StoreBindClass> implements StoreBindClassService {
    @Autowired
    private StoreBindClassMapper storeBindClassMapper;

    @Autowired
    private StoreService storeService;

    @Autowired
    private MallService mallService;

    @Override
    public boolean addStoreBindClass(StoreBindClass storeBindClass) {

        QueryWrapper<StoreBindClass> storeBindClassQueryWrapper = new QueryWrapper<>();
        storeBindClassQueryWrapper.eq("store_id", storeBindClass.getStoreId());
        if (storeBindClass.getClass3() > 0){
            storeBindClassQueryWrapper.eq("class_3", storeBindClass.getClass3());
        }else if (storeBindClass.getClass2() > 0){
            storeBindClassQueryWrapper.eq("class_2", storeBindClass.getClass2());
        }else{
            storeBindClassQueryWrapper.eq("class_1", storeBindClass.getClass1());
        }
        if (this.count(storeBindClassQueryWrapper) > 0) {
            throw new ShopException(ResultEnum.STORE_CLASS_EXISTS);
        }
        Integer bid;
        if (storeBindClass.getClass3() > 0){
            bid = storeBindClass.getClass3();
        }else if (storeBindClass.getClass2() > 0){
            bid = storeBindClass.getClass2();
        }else{
            bid = storeBindClass.getClass1();
        }
        GoodsCategory goodsCategory = mallService.getGoodsCategoryById(bid);
        storeBindClass.setCommisRate(goodsCategory.getCommission());
        return this.save(storeBindClass);
    }

    @Override
    public IPage<StoreBindClass> page(Page<StoreBindClass> page, Integer storeId, Integer state, String storeName) {
        QueryWrapper<StoreBindClass> storeBindClassQueryWrapper = new QueryWrapper<>();
        if (storeId != null){
            storeBindClassQueryWrapper.eq("store_id", storeId);
        }
        if (state != null){
            storeBindClassQueryWrapper.eq("state", state);
        }
        if (!StringUtils.isEmpty(storeName)) {
            Set<Integer> storeIds = storeService.list(new QueryWrapper<Store>()
                    .like("store_name", storeName)
                    .select("store_id"))
                    .stream().map(Store::getStoreId).collect(Collectors.toSet());
            storeBindClassQueryWrapper.in("store_id", storeIds);
        }
        storeBindClassQueryWrapper.orderByDesc("bid");
        return this.page(page, storeBindClassQueryWrapper);
    }

    @Override
    public void withGoodsCategoryInfo(List<StoreBindClass> storeBindClassList) {
        Set<Integer> classIds = new HashSet<>();
        storeBindClassList.forEach(storeBindClass -> {
            classIds.add(storeBindClass.getClass1());
            classIds.add(storeBindClass.getClass2());
            classIds.add(storeBindClass.getClass3());
        });
        if (!classIds.isEmpty()) {
            Map<Integer, GoodsCategory> goodsCategoryMap = mallService.getGoodsCategoryListByIds(classIds)
                    .stream().collect(Collectors.toMap(GoodsCategory::getId, goodsCategory -> goodsCategory));
            storeBindClassList.forEach(storeBindClass -> {
                StringBuilder sb = new StringBuilder();
                GoodsCategory goodsCategory1 = goodsCategoryMap.get(storeBindClass.getClass1());
                GoodsCategory goodsCategory2 = goodsCategoryMap.get(storeBindClass.getClass2());
                GoodsCategory goodsCategory3 = goodsCategoryMap.get(storeBindClass.getClass3());
                if (goodsCategory1 != null){
                    sb.append(goodsCategory1.getName());
                }
                if (goodsCategory2 != null){
                    if (sb.length() > 0){
                        sb.append(" -> ");
                    }
                    sb.append(goodsCategory2.getName());
                }
                if (goodsCategory3 != null){
                    if (sb.length() > 0){
                        sb.append(" -> ");
                    }
                    sb.append(goodsCategory3.getName());
                }
                storeBindClass.setFullClassName(sb.toString());
            });
        }
    }

    @Override
    public void withStoreInfo(List<StoreBindClass> storeBindClassList) {
        Set<Integer> storeIds = storeBindClassList.stream().map(StoreBindClass::getStoreId).collect(Collectors.toSet());
        if (!storeIds.isEmpty()) {
            Map<Integer, Store> storeMap = storeService.listByIds(storeIds).stream()
                    .collect(Collectors.toMap(Store::getStoreId, store -> store));
            if (!storeMap.isEmpty()) {
                for (StoreBindClass storeBindClass : storeBindClassList) {
                    Store store = storeMap.get(storeBindClass.getStoreId());
                    if (store != null){
                        storeBindClass.setStoreName(store.getStoreName());
                        storeBindClass.setSellerName(store.getSellerName());
                    }
                }
            }
        }
    }

    @Override
    public void withGoodsCategoryName(List<StoreBindClass> storeBindClassList) {
        Set<Integer> classIds = new HashSet<>();
        storeBindClassList.forEach(storeBindClass -> {
            classIds.add(storeBindClass.getClass1());
            classIds.add(storeBindClass.getClass2());
            classIds.add(storeBindClass.getClass3());
        });
        if (!classIds.isEmpty()) {
            Map<Integer, GoodsCategory> goodsCategoryMap = mallService.getGoodsCategoryListByIds(classIds)
                    .stream().collect(Collectors.toMap(GoodsCategory::getId, goodsCategory -> goodsCategory));
            storeBindClassList.forEach(storeBindClass -> {
                GoodsCategory goodsCategory1 = goodsCategoryMap.get(storeBindClass.getClass1());
                GoodsCategory goodsCategory2 = goodsCategoryMap.get(storeBindClass.getClass2());
                GoodsCategory goodsCategory3 = goodsCategoryMap.get(storeBindClass.getClass3());
                if (goodsCategory1 != null){
                    storeBindClass.setClassOneName(goodsCategory1.getName());
                }
                if (goodsCategory2 != null){
                    storeBindClass.setClassTwoName(goodsCategory2.getName());
                }
                if (goodsCategory3 != null){
                    storeBindClass.setClassThreeName(goodsCategory3.getName());
                }
            });
        }
    }

}
