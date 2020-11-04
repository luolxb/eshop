package com.soubao.service;

import com.soubao.common.vo.SBApi;
import com.soubao.entity.Goods;
import com.soubao.entity.GoodsCategory;
import com.soubao.entity.Region;
import com.soubao.entity.StoreGoodsCount;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@FeignClient(name = "mall")
public interface MallService {

    @GetMapping("/goods/category/tree")
    String getGoodsCategoryTree(@RequestParam(value = "ids", required = false) Set<Integer> ids);

    @GetMapping("/goods/category")
    GoodsCategory getGoodsCategoryById(@RequestParam(value = "id") Integer id);

    @GetMapping("/goods/category/list")
    List<GoodsCategory> getGoodsCategoryListByIds(@RequestParam(value = "id", required = false) Set<Integer> ids);

    @PutMapping("/on_sale")
    String updateIsOnSale(@RequestParam("store_id") Integer storeId);

    @GetMapping("/store/goods_count")
    int getStoreGoodsCount(@RequestParam("store_id") Integer storeId);

    @GetMapping("region/list")
    List<Region> listRegion(@RequestParam(value = "ids", required = false) Set<Integer> ids);

    @GetMapping("/stores/goods_count")
    List<StoreGoodsCount> getStoreGoodsCounts(@RequestParam(value = "store_id") Set<Integer> storeId);

    @GetMapping("/goods/list")
    List<Goods> getGoodsList(@RequestParam(value = "store_id", required = false) Integer storeId,
                             @RequestParam(value = "goods_state", defaultValue = "1") Integer goodsState,
                             @RequestParam(value = "is_on_sale", defaultValue = "1") Integer isOnSale,
                             @RequestParam(value = "limit", required = false) Integer limit);

    @PreAuthorize("hasAnyRole('SELLER')")
    @PostMapping("/goods")
    SBApi addGoods(@Valid @RequestBody Goods goods);

    @PreAuthorize("hasAnyRole('SELLER')")
    @PostMapping("/goods1")
    Integer addGoods1(@Valid @RequestBody Goods goods);

    @GetMapping("/seller/goods/dc_id")
    Goods goodsByDcId(@RequestParam("dc_id") Integer depositCertificateId);

//    @PreAuthorize("hasAnyRole('SELLER')")
//    @PutMapping("goods/list")
//    SBApi updateGoods(@RequestBody List<Goods> goodsList, SBApi sbApi);
//
//    @GetMapping("goods/{id}")
//    Goods getGoods(@PathVariable("id") Integer goodsId);


}
