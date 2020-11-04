package com.soubao.service;

import com.soubao.entity.*;
import com.soubao.common.vo.SBApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "seller")
public interface SellerService {

    @GetMapping("/list")
    List<Seller> sellers(@RequestParam(value = "seller_ids", required = false) Set<Integer> sellerIds);

    @GetMapping("/store/default")
    List<Store> defaultStores();

    @GetMapping("/store_address")
    StoreAddress storeAddress(@RequestParam("store_address_id") Integer storeAddressId);

    @GetMapping("/store_address/default")
    StoreAddress storeAddressDefault(@RequestParam("store_id") Integer storeId, @RequestParam("type") Integer type);

    @PutMapping("/store")
    SBApi updateById(@RequestBody Store store);

    @GetMapping("/store")
    Store store(@RequestParam("store_id") Integer storeId);

    @GetMapping("/store/sellerId")
    Store storeSellerId(@RequestParam("seller_id") Integer sellerId);

    @GetMapping("/store/list")
    List<Store> getStoresById(@RequestParam(value = "store_id") Set<Integer> storeId);

    @GetMapping("/userSellerStore/getOne")
    UserSellerStore userSellerStore(@RequestParam("nickname") String nickname,
                                    @RequestParam("userId") Integer userId,
                                    @RequestParam("mobile") String mobile);

    @GetMapping("/store/ids")
    Set<Integer> storeIdsByStoreName(
            @RequestParam(value = "store_name") String storeName);

    @GetMapping("/store/ids")
    Set<Integer> storeIdsByScId(
            @RequestParam(value = "sc_id") Integer scId);

    @GetMapping("/store/distribution/list")
    List<StoreDistribut> storeDistributionsByStoreIds(
            @RequestParam(value = "store_id") Set<Integer> storeIds);

    @GetMapping("/update_depositCertificate")
    SBApi updateDepositCertificateStatus(@RequestParam(value = "user_id", required = true) Integer userId,
                                         @RequestParam(value = "dc_id", required = true) Integer dcId,
                                         @RequestParam(value = "sale_status", required = false) Integer saleStatus,
                                         @RequestParam(value = "send_status", required = false) Integer sendStatus);


    /**
     * 根据存证的商家id 获取存证信息
     *
     * @param sellerId
     * @return
     */
    @GetMapping("/depositCertificate/list/on")
    List<DepositCertificate> getDepositCertificateBySellerId(@RequestParam(value = "seller_id") Integer sellerId);

    @GetMapping("/userSellerStore/storeId")
    UserSellerStore userSellerStoreByStoreId(@RequestParam("store_id") Integer storeId);

    @GetMapping("/userSellerStore/getUserStore")
    Store getUserSellerStore(@RequestParam(value = "userId") Integer userId);

    /**
     * 修改商店的交易数量
     *
     * @param storeId
     * @param userId
     */
    @PutMapping("/store/store_transaction_num")
    void updateStoreTransactionNum(@RequestParam("type") Integer type,
                                   @RequestParam("store_id") Integer storeId,
                                   @RequestParam("user_id") Integer userId);

    /**
     * 根据userId获取商家商铺
     *
     * @param userId
     * @return
     */
    @GetMapping("/stores/userId")
    Store getStoresByUserId(@RequestParam("user_id") Integer userId);

    @GetMapping("seller/storeId")
    Seller getSellerByStoreId(@RequestParam("store_id") Integer storeId);
}
