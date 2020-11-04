package com.soubao.service;

import com.soubao.entity.DepositCertificate;
import com.soubao.entity.Goods;
import com.soubao.entity.Store;
import com.soubao.entity.StoreGrade;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-29
 */
@FeignClient(name = "seller")
public interface SellerService {

    @GetMapping("/store")
    Store getStoreById(@RequestParam("store_id") Integer storeId);

    @GetMapping("/store/list")
    List<Store> getStoreListByIds(@RequestParam(value = "store_id") Set<Integer> storeId);

    @GetMapping("/store/list")
    List<Store> getStoreListByName(@RequestParam(value = "store_name") String storeName);

    @GetMapping("/store/list")
    List<Store> getStoreListByUserIds(@RequestParam(value = "user_id") Set<Integer> userIds);

    @GetMapping("/depositCertificate/list/on")
    List<DepositCertificate> getDepositCertificateBySellerId(@RequestParam(value = "seller_id") Integer sellerId);

    @GetMapping("/store_grade")
    StoreGrade getStoreGrade(@RequestParam("sg_id")Integer gradeId);

    @GetMapping("/store/page1")
    List<Store> storePage(
            @RequestParam(value = "is_own_shop",required = false) Integer isOwnShop,
            @RequestParam(value = "grade_id", required = false) Integer gradeId,
            @RequestParam(value = "sc_id", required = false) Integer scId,
            @RequestParam(value = "store_state", required = false) Integer storeState,
            @RequestParam(value = "seller_name", required = false) String sellerName,
            @RequestParam(value = "store_name", required = false) String storeName,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size);

    @GetMapping("/userSellerStore/getUserStore")
    Store getUserSellerStore(@RequestParam(value = "userId") Integer userId);

    @GetMapping("/userSellerStore/list")
    List<Store> getUserSellerStoreList();

}
