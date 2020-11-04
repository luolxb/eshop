package com.soubao.service;

import com.soubao.entity.Store;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * 服务类
 *
 * @author dyr
 * @since 2019-08-15
 */
@FeignClient("seller")
public interface SellerService {

    @GetMapping("/store")
    Store getStoreById(@RequestParam("store_id") Integer storeId);

    @GetMapping("/store/list")
    List<Store> getStoresById(@RequestParam(value = "store_id") Set<Integer> storeId);

    @PutMapping("/store")
    String updateStoreById(@RequestBody Store store);

    /**
     * 根据分类id获取店铺
     * @param scId
     * @return
     */
    @GetMapping("/store/list")
    List<Store> getStoresByScId(@RequestParam(value = "sc_id") Integer scId);

    /**
     * 根据 storeId  获取userId
     * @param storeId
     * @return
     */
    @GetMapping("/user/storeId")
    Integer getUserByStoreId(@RequestParam(value = "store_id") Integer storeId);
}
