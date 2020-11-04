package com.soubao.service;

import com.soubao.entity.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 服务类
 *
 * @author dyr
 * @since 2019-08-15
 */
@FeignClient("mall")
public interface MallService {
    @GetMapping("config")
    Map<Object, Object> config();

    @GetMapping("goods/{id}")
    Goods goods(@PathVariable("id") Integer goodsId);

    @GetMapping("store/{id}")
    Store store(@PathVariable("id") Integer storeId);

    @GetMapping("store_collects")
    List<StoreCollect> storeCollectList(@RequestParam(value = "store_id") Integer storeId);

    @GetMapping("flash_sale/{prom_id}")
    FlashSale flashSale(@PathVariable("prom_id") Integer promId);

    @GetMapping("group_buy/{prom_id}")
    GroupBuy groupBuy(@PathVariable("prom_id") Integer promId);

    @GetMapping("team/activity/{team_id}")
    TeamActivity teamActivity(@PathVariable(value = "team_id") Integer teamId);

    @GetMapping("pre_sell/{id}")
    PreSell preSell(@PathVariable(value = "id") Integer id);

    @GetMapping("sms_template")
    SmsTemplate smsTemplate(
            @RequestParam(value = "tpl_id", required = false) Integer tplId,
            @RequestParam(value = "send_scene", required = false) String sendScene);

    @GetMapping("miniapp_config")
    MiniappConfig miniappConfig();

    @GetMapping("check_code")
    void checkCode(
            @RequestParam(value = "mobile") String mobile,
            @RequestParam(value = "code") String code);

    @GetMapping("region/list")
    List<Region> regionList(@RequestParam(value = "ids") Set<Integer> regionIds);

    @GetMapping("store/{id}")
    Store getStore(@PathVariable("id") Integer storeId);

    @GetMapping("user/coupon/list/seller")
    List<CouponList> getCouponList(@RequestParam(value = "cid", required = false) Integer cid);
}
