package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.StoreGoodsClass;
import com.soubao.service.StoreGoodsClassService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 店铺商品分类表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2019-12-11
 */
@RestController
@RequestMapping("/store_goods_class")
public class StoreGoodsClassController {
    @Autowired
    private StoreGoodsClassService storeGoodsClassService;

    @GetMapping("list")
    public List<StoreGoodsClass> list(){
        return null;
    }

    @GetMapping("top_list")
    public List<StoreGoodsClass> topList(@RequestParam("store_id")Integer storeId){
        return storeGoodsClassService.list(new QueryWrapper<StoreGoodsClass>()
                .eq("store_id",storeId).eq("parent_id",0));
    }

    @GetMapping("seller/tree")
    public List<StoreGoodsClass> storeGoodsClassTree(@RequestParam("store_id")Integer storeId){
        return storeGoodsClassService.listToTree(storeGoodsClassService.list(new QueryWrapper<StoreGoodsClass>()
                .eq("store_id",storeId)));
    }

    @GetMapping
    public StoreGoodsClass storeGoodsClass(@RequestParam("cat_id")Integer catId){
        return storeGoodsClassService.getById(catId);
    }

    @PostMapping
    public SBApi add(@Valid @RequestBody StoreGoodsClass storeGoodsClass, SBApi sbApi){
        storeGoodsClassService.save(storeGoodsClass);
        return sbApi;
    }

    @PutMapping
    public SBApi update(@Valid @RequestBody StoreGoodsClass storeGoodsClass, SBApi sbApi){
        storeGoodsClassService.updateStoreGoodsClass(storeGoodsClass);
        return sbApi;
    }

    @DeleteMapping
    public SBApi remove(@RequestParam("cat_id")Integer catId, SBApi sbApi){
        storeGoodsClassService.remove(new QueryWrapper<StoreGoodsClass>().eq("cat_id",catId).or().eq("parent_id",catId));
        return sbApi;
    }

}
