package com.soubao.service;

import com.soubao.entity.User;
import com.soubao.entity.UserAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyr
 * @since 2019-11-28
 */
public interface UserAddressService extends IService<UserAddress> {
    /**
     * 列表带上地址名称
     * @param userAddresses
     */
    void withRegionName(List<UserAddress> userAddresses);
    /**
     * 用户地址带上地址名称
     * @param userAddress
     */
    void withRegionName(UserAddress userAddress);

    //添加收货地址
    void addUserAddress(User user, UserAddress userAddress);

    //更新收货地址
    void updateUserAddress(User user, UserAddress userAddress);

    //删除收货地址
    void removeUserAddress(User user, Integer addressId);
}
