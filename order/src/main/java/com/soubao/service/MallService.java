package com.soubao.service;

import com.soubao.entity.*;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient("mall")
public interface MallService {
    @GetMapping("/miniapp_config")
    MiniappConfig getMiniappConfig();

    @GetMapping("config")
    Map<Object, Object> config();

    @GetMapping("/region/list")
    List<Region> regions(@RequestParam(value = "ids", required = false) Set<Integer> ids);

    @GetMapping("/goods/{id}")
    Goods goods(@PathVariable("id") Integer goodsId);

    @PutMapping("/goods")
    String updateGoods(@RequestBody Goods goods);

    @GetMapping("sell-state/update")
    SBApi updateSellState(@RequestParam(value = "goodsId", required = true) Integer goodsId,
                          @RequestParam(value = "userId", required = false) Integer userId,
                          @RequestParam(value = "store_count", required = true) Integer store_count,
                          @RequestParam(value = "price", required = false) BigDecimal price);

    @GetMapping("sell-state/update/goods")
    SBApi updateSellStateGoods(@RequestParam(value = "goodsId", required = true) Integer goodsId,
                               @RequestParam(value = "userId", required = false) Integer userId,
                               @RequestParam(value = "store_id") Integer storeId,
                               @RequestParam(value = "store_count", required = true) Integer store_count,
                               @RequestParam(value = "price", required = false) BigDecimal price);

    @GetMapping("team/order_ids")
    Set<Integer> getOrderIdsByTeamIdOrFoundId(@RequestParam(value = "team_id", required = false) Integer teamId,
                                              @RequestParam(value = "found_id", required = false) Integer foundId);

    @RequestMapping(value = "/coupon/{id}", method = RequestMethod.GET)
    Coupon getCouponById(@PathVariable("id") Integer id);

    @GetMapping("/user/coupon")
    CouponList getUserCoupon(@RequestParam(value = "uid", required = false) Integer uid,
                             @RequestParam(value = "get_order_id", required = false) Integer getOrderId);

    @DeleteMapping("/user/coupon")
    SBApi cancelUserCoupon(@RequestParam(value = "id") Integer id);

    @PutMapping("/user/coupon/return")
    SBApi returnUserCoupon(@RequestParam(value = "user_id") Integer userId,
                           @RequestParam(value = "order_id") Integer orderId);

    @GetMapping("/team/activity")
    TeamActivity teamActivity(@RequestParam(value = "team_id") Integer teamId);

    @GetMapping("/team/found")
    TeamFound teamFound(@RequestParam(value = "order_id") Integer orderId);

    @GetMapping("/team/found")
    List<TeamFollow> teamFollows(@RequestParam(value = "found_id") Integer orderId,
                                 @RequestParam(value = "status") Integer status);

    @GetMapping("/goods/ids")
    Set<Integer> goodsIdsByGoodsName(
            @RequestParam(value = "goods_name") String goodsName);

    @GetMapping("/goods/ids")
    Set<Integer> goodsIdsByCatId(
            @RequestParam(value = "cat_id") Integer catId);

    @GetMapping("/goods/ids")
    Set<Integer> goodsIdsByCatIdAndBrandId(
            @RequestParam(value = "cat_id", required = false) Integer catId,
            @RequestParam(value = "brand_id", required = false) Integer brandId
    );

    @GetMapping("/goods/image/list")
    List<GoodsImages> images(@RequestParam("goods_id") Integer goodsId);

    /**
     * 根据dc_id ,store_id 获取商家多次转卖的商品
     *
     * @param collect
     * @param storeId
     * @return
     */
    @GetMapping("/goods/dc_id")
    List<Goods> getGoodsByDcid(@RequestParam("dc_ids") List<Long> collect,
                               @RequestParam("store_id") Integer storeId);

    /**
     * 获取商家发布的商品
     * @param userId
     * @return
     */
    @GetMapping("/goods/seller")
    List<Goods> getgoodsBySellerUserId(@RequestParam("user_id") Integer userId);

    @GetMapping("/goods/storeId")
    List<Goods> getGoodsByStoreId(@RequestParam("store_id") Integer storeId );

    @GetMapping("/region/address")
    String getAddress(@RequestParam(value = "country", required = false) Integer country,
                      @RequestParam(value = "province", required = false) Integer province,
                      @RequestParam(value = "city", required = false) Integer city,
                      @RequestParam(value = "district", required = false) Integer district,
                      @RequestParam(value = "twon", required = false) Integer twon) ;
}
