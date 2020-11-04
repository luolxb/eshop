package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.StoreDistribut;
import com.soubao.service.StoreDistributService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-05-06
 */
@RestController
@RequestMapping("/store/distribution")
@Api(value = "店铺分销", tags = {"店铺入驻相关接口"})
public class StoreDistributController {
    @Autowired
    private StoreDistributService storeDistributService;

    @GetMapping
    public StoreDistribut getStoreSet(@RequestParam("store_id") Integer storeId) {
        return storeDistributService.getOne(new QueryWrapper<StoreDistribut>().eq("store_id", storeId));
    }

    @GetMapping("/list")
    public List<StoreDistribut> getList(@RequestParam("store_id") Set<Integer> storeIds) {
        return storeDistributService.list(new QueryWrapper<StoreDistribut>().in("store_id", storeIds));
    }

    @PostMapping
    public SBApi saveStoreDistribut(@RequestBody StoreDistribut storeDistribut) {
        storeDistributService.save(storeDistribut);
        return new SBApi();
    }

    @PutMapping
    public SBApi updateStoreDistribut(@RequestBody StoreDistribut storeDistribut) {
        storeDistributService.updateById(storeDistribut);
        return new SBApi();
    }
}
