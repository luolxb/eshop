package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.StoreClass;

/**
 * <p>
 * 店铺分类表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-12-06
 */
public interface StoreClassService extends IService<StoreClass> {
    //删除店铺分类
    void removeStoreClass(Integer scId);
}
