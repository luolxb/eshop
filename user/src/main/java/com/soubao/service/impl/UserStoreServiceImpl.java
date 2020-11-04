package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.UserStoreMapper;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.entity.User;
import com.soubao.entity.UserStore;
import com.soubao.service.UserStoreService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户店铺信息表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-27
 */
@Service("userStoreService")
public class UserStoreServiceImpl extends ServiceImpl<UserStoreMapper, UserStore> implements UserStoreService {
    @Override
    public void saveUserStore(User user, UserStore userStore) {
        //非分销商
        if (user.getIsDistribut() != 1) {
            throw new ShopException(ResultEnum.NOT_DISTRIBUTORS);
        }
        UserStore us = getOne(new QueryWrapper<UserStore>().eq("user_id", user.getUserId()), false);
        if (us == null) {
            //添加
            userStore.setUserId(user.getUserId());
            userStore.setStoreTime(System.currentTimeMillis() / 1000);
            if (!save(userStore)) {
                throw new ShopException(ResultEnum.FAIL);
            }
        } else {
            //修改
            userStore.setStoreTime(us.getStoreTime());
            if (!update(userStore, new QueryWrapper<UserStore>().eq("id", us.getId()))) {
                throw new ShopException(ResultEnum.FAIL);
            }
        }
    }
}
