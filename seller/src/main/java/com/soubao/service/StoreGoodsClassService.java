package com.soubao.service;

import com.soubao.entity.StoreGoodsClass;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 店铺商品分类表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-12-11
 */
public interface StoreGoodsClassService extends IService<StoreGoodsClass> {

    List<StoreGoodsClass> listToTree(List<StoreGoodsClass> list);

    //更新店铺分类
    void updateStoreGoodsClass(StoreGoodsClass storeGoodsClass);
}
