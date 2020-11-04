package com.soubao.service;

import com.soubao.entity.Seller;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 卖家用户表 服务类
 * </p>
 *
 * @author dyr
 * @since 2020-03-16
 */
@FeignClient("seller")
public interface SellerService {

    @GetMapping("/credential")
    Seller getOneBySellerName(@RequestParam("seller_name") String sellerName);

    @GetMapping("/credential")
    Seller getOneByUserId(@RequestParam("user_id") Integer userId);
}
