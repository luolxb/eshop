package com.soubao.service;

import com.soubao.entity.StoreCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyr
 * @since 2020-05-09
 */
public interface StoreCollectService extends IService<StoreCollect> {
    //用户收藏店铺
    void addStoreCollect(User user, Integer storeId);

    //取消店铺收藏
    void removeStoreCollect(User user, Integer storeId);

    //获取某店铺的所有关注
    List<StoreCollect> storeCollectList(Integer storeId);

    void withStore(List<StoreCollect> records);
}
