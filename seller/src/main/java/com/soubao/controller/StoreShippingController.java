package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.StoreShipping;
import com.soubao.service.StoreShippingService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 店铺快递公司表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2019-12-10
 */
@RestController
@RequestMapping("/store_shipping")
public class StoreShippingController {

    @Autowired
    private StoreShippingService storeShippingService;

    @PostMapping("/{store_id}")
    public SBApi updateStoreShipping(@PathVariable("store_id") Integer storeId,
                                     @RequestBody Set<Integer> shippingIdList) {
        QueryWrapper<StoreShipping> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("store_id", storeId);
        storeShippingService.remove(queryWrapper);
        List<StoreShipping> storeShippingList = new ArrayList<>();
        for (Integer shippingId : shippingIdList) {
            StoreShipping storeShipping = new StoreShipping();
            storeShipping.setStoreId(storeId);
            storeShipping.setShippingId(shippingId);
            storeShippingList.add(storeShipping);
        }
        storeShippingService.saveBatch(storeShippingList);
        return new SBApi();
    }
}
