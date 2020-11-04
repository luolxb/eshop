package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.dao.StoreAddressMapper;
import com.soubao.entity.Region;
import com.soubao.entity.StoreAddress;
import com.soubao.service.MallService;
import com.soubao.service.StoreAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 店铺地址表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-11-25
 */
@Service
public class StoreAddressServiceImpl extends ServiceImpl<StoreAddressMapper, StoreAddress> implements StoreAddressService {
    @Autowired
    private MallService mallService;

    @Override
    public void addStoreAddress(StoreAddress storeAddress) {
        int address_count = this.count(new QueryWrapper<StoreAddress>()
                .eq("is_default",1)
                .eq("type",storeAddress.getType())
                .eq("store_id", storeAddress.getStoreId()));
        if (address_count == 0){//没有默认收货/发货地址，设置当前地址为默认收货/发货地址
            storeAddress.setIsDefault(1);
        }
        if(storeAddress.getIsDefault() == 1 && address_count > 0){//设置当前收货/发货地址为默认地址时，取消其他收货/发货地址的默认状态
            this.update(new UpdateWrapper<StoreAddress>()
                    .set("is_default",0)
                    .eq("is_default",1)
                    .eq("type",storeAddress.getType())
                    .eq("store_id",storeAddress.getStoreId()));
        }
        this.save(storeAddress);
    }

    @Override
    public void updateStoreAddress(StoreAddress storeAddress) {
        int address_count = this.count(new QueryWrapper<StoreAddress>()
                .eq("is_default",1)
                .eq("type",storeAddress.getType())
                .eq("store_id", storeAddress.getStoreId()));
        if (address_count == 0){//没有默认收货/发货地址，设置当前地址为默认收货/发货地址
            storeAddress.setIsDefault(1);
        }
        if(storeAddress.getIsDefault() == 1 && address_count > 0){//设置当前收货/发货地址为默认地址时，取消其他收货/发货地址的默认状态
            this.update(new UpdateWrapper<StoreAddress>()
                    .set("is_default",0)
                    .eq("is_default",1)
                    .eq("type",storeAddress.getType())
                    .eq("store_id",storeAddress.getStoreId()));
        }
        this.updateById(storeAddress);
    }

    @Override
    public void withRegionsName(List<StoreAddress> records) {
        if (!records.isEmpty()){
            Set<Integer> regionIds = new HashSet<>();
            records.forEach(storeAddress -> {
                regionIds.add(storeAddress.getProvinceId());
                regionIds.add(storeAddress.getCityId());
                regionIds.add(storeAddress.getDistrictId());
            });
            Map<Integer, String> regionMap = mallService.listRegion(regionIds).stream().collect(Collectors.toMap(Region::getId, Region::getName));
            records.forEach(storeAddress -> {
                storeAddress.setProvinceName(regionMap.get(storeAddress.getProvinceId()));
                storeAddress.setCityName(regionMap.get(storeAddress.getCityId()));
                storeAddress.setDistrictName(regionMap.get(storeAddress.getDistrictId()));
            });
        }
    }
}
