package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.StoreAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 店铺地址表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-11-25
 */
public interface StoreAddressService extends IService<StoreAddress> {

    //新增店铺地址
    void addStoreAddress(StoreAddress storeAddress);

    //修改店鋪地址
    void updateStoreAddress(StoreAddress storeAddress);

    void withRegionsName(List<StoreAddress> records);

}
