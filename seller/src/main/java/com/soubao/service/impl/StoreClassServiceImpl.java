package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.StoreClassMapper;
import com.soubao.entity.Store;
import com.soubao.entity.StoreClass;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.StoreClassService;
import com.soubao.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 店铺分类表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-12-06
 */
@Service
public class StoreClassServiceImpl extends ServiceImpl<StoreClassMapper, StoreClass> implements StoreClassService {
    @Autowired
    private StoreService storeService;

    @Override
    public void removeStoreClass(Integer scId) {
        if (storeService.count(new QueryWrapper<Store>().eq("sc_id", scId)) > 0) {
            throw new ShopException(ResultEnum.CANNOT_REMOVE_STORECLASS);
        }
        removeById(scId);
    }
}
