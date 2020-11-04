package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.StoreClass;
import com.soubao.service.StoreClassService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 店铺分类表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2019-12-06
 */
@RestController
@RequestMapping("/store_class")
public class StoreClassController {

    @Autowired
    private StoreClassService storeClassService;

    @GetMapping("list")
    @ApiOperation(value = "店铺分类列表")
    public List<StoreClass> list(){
        return storeClassService.list();
    }

    @GetMapping("page")
    public IPage<StoreClass> page(@RequestParam(value = "p", defaultValue = "1")Integer page,
                                  @RequestParam(value = "size", defaultValue = "10")Integer size){
        return storeClassService.page(new Page<>(page, size), new QueryWrapper<StoreClass>().orderByDesc("sc_sort"));
    }

    @GetMapping
    public StoreClass storeClass(@RequestParam("sc_id")Integer scId){
        return storeClassService.getById(scId);
    }

    @PostMapping
    public SBApi add(@Valid @RequestBody StoreClass storeClass, SBApi sbApi){
        storeClassService.save(storeClass);
        return sbApi;
    }

    @PutMapping
    public SBApi update(@Valid @RequestBody StoreClass storeClass, SBApi sbApi){
        storeClassService.updateById(storeClass);
        return sbApi;
    }

    @DeleteMapping
    public SBApi remove(@RequestParam("sc_id")Integer scId, SBApi sbApi){
        storeClassService.removeStoreClass(scId);
        return sbApi;
    }

}
