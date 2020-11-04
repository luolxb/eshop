package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.Store;
import com.soubao.entity.User;
import com.soubao.entity.UserSellerStore;
import com.soubao.service.StoreService;
import com.soubao.service.UserSellerStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/userSellerStore")
public class UserSellerStoreController {

    @Autowired
    private UserSellerStoreService userSellerStoreService;

    @Autowired
    private StoreService storeService;

    @GetMapping("getOne")
    UserSellerStore getOne(@RequestParam("nickname") String nickName,
                           @RequestParam("userId") Integer userId,
                           @RequestParam("mobile") String mobile) {
        QueryWrapper<UserSellerStore> userSellerStoreQueryWrapper = new QueryWrapper<>();
        userSellerStoreQueryWrapper.eq("user_id", userId);
        UserSellerStore one = userSellerStoreService.getOne(userSellerStoreQueryWrapper);
        if (one == null) {
            Store store = new Store();
            store.setInviteUserId(0);
            store.setStoreName(nickName);
            store.setStorePhone(mobile);
            store.setGradeId(0);
            store.setUserId(userId);
            store.setUserName(nickName);
            store.setScId(0);
            store.setCityId(0);
            store.setDistrict(0);
            store.setStoreAddress("0");
            store.setStoreZip("0");
            store.setStoreState(1);
            store.setStoreSort(1);
            store.setStoreRebatePaytime("0");
            store.setStoreRecommend(0);
            store.setStoreTheme("0");
            store.setStoreCredit(0);
            store.setStoreDesccredit(BigDecimal.ZERO);
            store.setStoreServicecredit(BigDecimal.ZERO);
            store.setStoreCollect(0);
            store.setStoreSales(0);
            store.setStorePresales("0");
            store.setStoreAftersales("0");
            store.setStoreFreePrice(BigDecimal.ZERO);
            store.setStoreDecorationSwitch(0);
            store.setStoreDecorationOnly(0);
            store.setIsOwnShop(0);
            store.setDeleted(0);
            store.setDomainEnable(0);
            store.setStoreType(0);
            store.setDefaultStore(0);
            store.setStoreNotice("0");
            store.setProvinceId(0);
            storeService.save(store);

            UserSellerStore userSellerStore = new UserSellerStore();
            userSellerStore.setStoreId(store.getStoreId());
            userSellerStore.setUserId(userId);
            userSellerStoreService.save(userSellerStore);
            return userSellerStore;
        }
        return one;
    }

    @GetMapping("getUserStore")
    Store getOne(@RequestParam("userId") Integer userId) {
        QueryWrapper<UserSellerStore> userSellerStoreQueryWrapper = new QueryWrapper<>();
        userSellerStoreQueryWrapper.eq("user_id", userId);
        UserSellerStore one = userSellerStoreService.getOne(userSellerStoreQueryWrapper);
        if (null == one) {
            return null;
        }
        return storeService.getById(one.getStoreId());
    }

    /**
     * 获取散户的商店list
     *
     * @return
     */
    @GetMapping("/list")
    List<Store> getUserSellerStoreList() {
        List<UserSellerStore> list = userSellerStoreService.list();
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return storeService.list(new QueryWrapper<Store>().in("store_id", list.stream().map(UserSellerStore::getStoreId).collect(Collectors.toList())));
    }


    @GetMapping("/storeId")
    UserSellerStore userSellerStoreByStoreId(@RequestParam("store_id") Integer storeId) {
        QueryWrapper<UserSellerStore> userSellerStoreQueryWrapper = new QueryWrapper<>();
        userSellerStoreQueryWrapper.eq("store_id", storeId);
        return userSellerStoreService.getOne(userSellerStoreQueryWrapper);
    }
}
