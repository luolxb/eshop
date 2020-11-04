package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.StoreAddress;
import com.soubao.service.StoreAddressService;
import com.soubao.validation.group.Insert;
import com.soubao.validation.group.Update;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 店铺地址表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2019-11-25
 */
@RestController
@RequestMapping("store_address")
public class StoreAddressController {
    @Autowired
    private StoreAddressService storeAddressService;

    @GetMapping("page")
    public IPage<StoreAddress> page(@RequestParam("store_id")Integer storeId,
                                    @RequestParam(value = "p", defaultValue = "1")Integer page,
                                    @RequestParam(value = "size", defaultValue = "10")Integer size){
        QueryWrapper<StoreAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("store_id", storeId);
        queryWrapper.orderByDesc("store_address_id");
        IPage<StoreAddress> storeAddressPage = storeAddressService.page(new Page<>(page, size), queryWrapper);
        storeAddressService.withRegionsName(storeAddressPage.getRecords());
        return storeAddressPage;
    }

    @GetMapping("list")
    public List<StoreAddress> list(@RequestParam("store_id")Integer storeId){
        List<StoreAddress> storeAddressList = storeAddressService.list(new QueryWrapper<StoreAddress>()
                .eq("store_id", storeId)
                .eq("type", 0)
                .orderByDesc("is_default"));
        storeAddressService.withRegionsName(storeAddressList);
        return storeAddressList;
    }

    @GetMapping
    public StoreAddress storeAddress(@RequestParam("store_address_id")Integer storeAddressId){
        return storeAddressService.getById(storeAddressId);
    }

    @GetMapping("default")
    public StoreAddress storeAddressDefault(@RequestParam("store_id")Integer storeId,@RequestParam("type")Integer type){
        return storeAddressService.getOne(new QueryWrapper<StoreAddress>()
                .eq("store_id",storeId).eq("type", type).eq("is_default",1));
    }

    @PostMapping
    public SBApi add(@Validated({Insert.class}) @RequestBody StoreAddress storeAddress, SBApi sbApi){
        storeAddressService.addStoreAddress(storeAddress);
        return sbApi;
    }

    @PutMapping
    public SBApi update(@Validated({Update.class}) @RequestBody StoreAddress storeAddress, SBApi sbApi){
        storeAddressService.updateStoreAddress(storeAddress);
        return sbApi;
    }

    @DeleteMapping
    public SBApi delete(@RequestParam("store_address_id")Integer storeAddressId, SBApi sbApi){
        storeAddressService.removeById(storeAddressId);
        return sbApi;
    }

}
