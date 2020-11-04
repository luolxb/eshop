package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.User;
import com.soubao.entity.UserStore;

/**
 * <p>
 * 用户店铺信息表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-27
 */
public interface UserStoreService extends IService<UserStore> {
    //保存用户店铺信息 添加/修改
    void saveUserStore(User user, UserStore userStore);
}
