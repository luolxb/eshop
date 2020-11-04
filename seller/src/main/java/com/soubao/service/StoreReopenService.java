package com.soubao.service;

import com.soubao.entity.StoreReopen;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 续签内容表 服务类
 * </p>
 *
 * @author dyr
 * @since 2020-03-06
 */
public interface StoreReopenService extends IService<StoreReopen> {
    //申请续签
    void saveStoreReopen(StoreReopen storeReopen);
}
