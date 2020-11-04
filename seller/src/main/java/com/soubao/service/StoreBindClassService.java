package com.soubao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.StoreBindClass;

import java.util.List;

/**
 * <p>
 * 店铺可发布商品类目表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-11-25
 */
public interface StoreBindClassService extends IService<StoreBindClass> {

    //新增店铺经营类目
    boolean addStoreBindClass(StoreBindClass storeBindClass);

    //店铺经营类目审核分页
    IPage<StoreBindClass> page(Page<StoreBindClass> page, Integer storeId, Integer state, String storeName);

    void withGoodsCategoryInfo(List<StoreBindClass> storeBindClassList);

    void withStoreInfo(List<StoreBindClass> records);

    void withGoodsCategoryName(List<StoreBindClass> storeBindClassList);

}
