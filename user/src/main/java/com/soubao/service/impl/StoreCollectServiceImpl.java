package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.StoreCollectMapper;
import com.soubao.entity.Store;
import com.soubao.entity.StoreCollect;
import com.soubao.entity.User;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.SellerService;
import com.soubao.service.StoreCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-05-09
 */
@Service
public class StoreCollectServiceImpl extends ServiceImpl<StoreCollectMapper, StoreCollect> implements StoreCollectService {
    @Autowired
    private SellerService sellerService;

    @Override
    public void addStoreCollect(User user, Integer storeId) {
        Store store = sellerService.getStoreById(storeId);
        if (store == null) {
            throw new ShopException(ResultEnum.STORE_NOT_EXISTS);
        }
        StoreCollect storeCollect = getOne(new QueryWrapper<StoreCollect>()
                .eq("store_id", storeId).eq("user_id", user.getUserId()));
        if (storeCollect != null) {
            throw new ShopException(ResultEnum.STORE_COLLECT_EXISTS);
        }
        StoreCollect sc = new StoreCollect();
        sc.setUserId(user.getUserId());
        sc.setStoreId(storeId);
        sc.setAddTime(System.currentTimeMillis() / 1000);
        sc.setStoreName(store.getStoreName());
        sc.setUserName(user.getNickname());
        if (save(sc)) {
            store.setStoreCollect(store.getStoreCollect() + 1);
            sellerService.updateStoreById(store);
        } else {
            throw new ShopException(ResultEnum.FAIL);
        }
    }

    @Override
    public void removeStoreCollect(User user, Integer storeId) {
        StoreCollect storeCollect = getOne(new QueryWrapper<StoreCollect>().eq("store_id", storeId)
                .eq("user_id", user.getUserId()));
        if (storeCollect != null) {
            if (removeById(storeCollect.getLogId())) {
                Store store = sellerService.getStoreById(storeId);
                store.setStoreCollect(store.getStoreCollect() - 1);
                sellerService.updateStoreById(store);
            } else {
                throw new ShopException(ResultEnum.FAIL);
            }
        }
    }

    @Override
    public List<StoreCollect> storeCollectList(Integer storeId) {
        return this.list(new QueryWrapper<StoreCollect>().eq("store_id", storeId));
    }

    @Override
    public void withStore(List<StoreCollect> records) {
        if (records.size() == 0) {
            return;
        }
        Set<Integer> storeIds = records.stream().map(StoreCollect::getStoreId).collect(Collectors.toSet());
        if (storeIds.size() > 0) {
            Map<Integer, Store> storeMap = sellerService.getStoresById(storeIds).stream().collect(Collectors.toMap(Store::getStoreId, store -> store));
            records.forEach(storeCollect -> {
                if (storeMap.containsKey(storeCollect.getStoreId())) {
                    storeCollect.setStore(storeMap.get(storeCollect.getStoreId()));
                }
            });
        }
    }
}
