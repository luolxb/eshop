package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.soubao.dao.UserAddressMapper;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.entity.Region;
import com.soubao.entity.User;
import com.soubao.entity.UserAddress;
import com.soubao.service.MallService;
import com.soubao.service.UserAddressService;
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
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-11-28
 */
@Service("userAddressService")
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Autowired
    private MallService mallService;

    @Override
    public void withRegionName(List<UserAddress> userAddressList) {
        if (userAddressList.size() == 0) {
            return;
        }
        Set<Integer> regionIds = new HashSet<>();
        for (UserAddress userAddress : userAddressList) {
            regionIds.add(userAddress.getProvince());
            regionIds.add(userAddress.getCity());
            regionIds.add(userAddress.getDistrict());
            regionIds.add(userAddress.getTwon());
        }
        List<Region> regions = mallService.regionList(regionIds);
        Map<Integer, String> regionIdToName = regions.stream().collect(Collectors.toMap(Region::getId, Region::getName));
        for (UserAddress userAddress : userAddressList) {
            withRegionName(userAddress, regionIdToName);
        }
    }

    @Override
    public void withRegionName(UserAddress userAddress) {
        Set<Integer> ids = new HashSet<>();
        ids.add(userAddress.getProvince());
        ids.add(userAddress.getCity());
        ids.add(userAddress.getDistrict());
        ids.add(userAddress.getTwon());
        List<Region> regions = mallService.regionList(ids);
        Map<Integer, String> regionIdToName = regions.stream().collect(Collectors.toMap(Region::getId, Region::getName));
        withRegionName(userAddress, regionIdToName);
    }

    @Override
    public void addUserAddress(User user, UserAddress userAddress) {
        int userAddressCount = count(new QueryWrapper<UserAddress>().eq("user_id", user.getUserId()));
        if (userAddressCount >= 20) {
            throw new ShopException(ResultEnum.TOO_MANY_ADDRESS);
        }
        userAddress.setUserId(user.getUserId());
        if (userAddressCount == 0) {
            userAddress.setIsDefault(1);
        }
        if (save(userAddress)) {
            //当已存在收货地址并设置了当前添加收货地址为默认地址时，取消其他地址的默认状态
            if (userAddressCount > 0 && userAddress.getIsDefault() == 1) {
                if (!update(new UpdateWrapper<UserAddress>()
                        .set("is_default", 0)
                        .eq("user_id", userAddress.getUserId())
                        .ne("address_id", userAddress.getAddressId()))) {
                    throw new ShopException(ResultEnum.FAIL);
                }
            }
        } else {
            throw new ShopException(ResultEnum.FAIL);
        }
    }

    @Override
    public void updateUserAddress(User user, UserAddress userAddress) {
        update(userAddress, new UpdateWrapper<UserAddress>()
                .eq("user_id", user.getUserId()).eq("address_id", userAddress.getAddressId()));
        //更新地址时设置当前地址为默认地址时，取消其他地址的默认状态
        if (userAddress.getIsDefault() == 1) {
            update(new UpdateWrapper<UserAddress>()
                    .set("is_default", 0).eq("user_id", user.getUserId()).ne("address_id", userAddress.getAddressId()));
        }
    }

    @Override
    public void removeUserAddress(User user, Integer addressId) {
        UserAddress userAddress = getOne(new QueryWrapper<UserAddress>()
                .eq("address_id", addressId)
                .eq("user_id", user.getUserId()));
        if (userAddress != null) {
            if (remove(new QueryWrapper<UserAddress>()
                    .eq("address_id", addressId)
                    .eq("user_id", user.getUserId()))) {
                if (userAddress.getIsDefault() == 1) {
                    //如果删除的是默认地址则修改最新一条地址为默认地址
                    UserAddress ua = getOne(new QueryWrapper<UserAddress>()
                            .eq("user_id", user.getUserId()).orderByDesc("address_id").last("limit 1"));
                    if (ua != null) {
                        update(new UpdateWrapper<UserAddress>().set("is_default", 1).eq("address_id", ua.getAddressId()));
                    }
                }
            } else {
                throw new ShopException(ResultEnum.FAIL);
            }
        }
    }

    private void withRegionName(UserAddress userAddress,  Map<Integer, String> regionIdToName){
        userAddress.setProvinceName(regionIdToName.get(userAddress.getProvince()));
        userAddress.setCityName(regionIdToName.get(userAddress.getCity()));
        userAddress.setDistrictName(regionIdToName.get(userAddress.getDistrict()));
        userAddress.setTwonName(regionIdToName.get(userAddress.getTwon()));
    }
}
