package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.GoodsCategory;
import com.soubao.entity.Seller;
import com.soubao.entity.Store;
import com.soubao.entity.StoreBindClass;
import com.soubao.service.MallService;
import com.soubao.service.StoreBindClassService;
import com.soubao.service.StoreService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 店铺可发布商品类目表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2019-11-25
 */
@RestController
@RequestMapping("/store_bind_class")
public class StoreBindClassController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreBindClassService storeBindClassService;
    @Autowired
    private MallService mallService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping("page")
    public IPage<StoreBindClass> page(@RequestParam(value = "store_id", required = false) Integer storeId,
                                      @RequestParam(value = "state", required = false) Integer state,
                                      @RequestParam(value = "store_name", required = false) String storeName,
                                      @RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<StoreBindClass> storeBindClassIPage = storeBindClassService.page(new Page<>(page, size), storeId, state, storeName);
        storeBindClassService.withGoodsCategoryInfo(storeBindClassIPage.getRecords());
        storeBindClassService.withStoreInfo(storeBindClassIPage.getRecords());
        return storeBindClassIPage;
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("/goods_category")
    public String getStoreGoodsCategory() {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        Store store = storeService.getById(seller.getStoreId());
        if (store.getBindAllGc() == 1) {
            return mallService.getGoodsCategoryTree(null);
        } else {
            List<StoreBindClass> storeBindClasses = storeBindClassService.list((new QueryWrapper<StoreBindClass>())
                    .eq("store_id", store.getStoreId()));
            Set<Integer> ids = new HashSet<>();
            for (StoreBindClass storeBindClass : storeBindClasses) {
                ids.add(storeBindClass.getClass1());
                ids.add(storeBindClass.getClass2());
                ids.add(storeBindClass.getClass3());
            }
            if(ids.size() > 0){
                return mallService.getGoodsCategoryTree(ids);
            }
            return null;
        }
    }

    @GetMapping("list")
    public List<StoreBindClass> storeBindClasses(@RequestParam("store_id") Integer storeId) {
        List<StoreBindClass> storeBindClassList = storeBindClassService.list(new QueryWrapper<StoreBindClass>()
                .eq("store_id", storeId));
        storeBindClassService.withGoodsCategoryName(storeBindClassList);
        return storeBindClassList;
    }

    @PostMapping
    public SBApi save(@RequestBody StoreBindClass storeBindClass) {
        storeBindClassService.addStoreBindClass(storeBindClass);
        return new SBApi();
    }

    @PutMapping("check")
    public boolean check(@RequestParam("bid") Integer bid, @RequestParam("state") Integer state) {
        return storeBindClassService.update(new UpdateWrapper<StoreBindClass>().set("state", state).eq("bid", bid));
    }

    @DeleteMapping
    public SBApi remove(@RequestParam("bid") Integer bid) {
        storeBindClassService.removeById(bid);
        return new SBApi();
    }

    @GetMapping("count")
    public Integer getCount(@RequestParam(value = "state", required = false) Set<Integer> state) {
        QueryWrapper<StoreBindClass> storeBindClassQueryWrapper = new QueryWrapper<>();
        if(state != null){
            storeBindClassQueryWrapper.in("state", state);
        }
        return storeBindClassService.count(storeBindClassQueryWrapper);
    }

    @GetMapping
    public StoreBindClass getOne(@RequestParam("store_id")Integer storeId,
                                @RequestParam("class1")Integer class1,
                                @RequestParam("class2")Integer class2,
                                @RequestParam("class3")Integer class3){
        return storeBindClassService.getOne(new QueryWrapper<StoreBindClass>()
                        .eq("class_1", class1)
                        .eq("class_2", class2)
                        .eq("class_3", class3)
                        .eq("store_id", storeId)
                        .last("limit 1")
        );
    }
}
