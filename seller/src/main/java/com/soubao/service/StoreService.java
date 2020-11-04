package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Seller;
import com.soubao.entity.Store;
import com.soubao.vo.StoreAddVo;
import com.soubao.vo.StoreSlideVO;

import java.util.List;

/**
 * <p>
 * 店铺数据表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-11-19
 */
public interface StoreService extends IService<Store> {

    //添加店铺
    Integer addStore(StoreAddVo storeAddVo);

    //更新店铺详细信息-总后台
    void updateStoreDetail(Store store);

    //更新店铺信息
    void updateStore(Store store);

    //删除店铺
    void removeStore(Integer storeId);

    //设置默认同步平台
    void setDefaultStore(Integer storeId);

    //获取店铺幻灯片设置
    StoreSlideVO getSlide(Seller seller);

    //获取手机店铺幻灯片
    StoreSlideVO getMobileSlide(Seller seller);

    void withInviteUser(List<Store> records);

    void withStoreOrderSum(List<Store> records);

    void withStoreMemberCount(List<Store> records);

    void withSeller(List<Store> records);

    void withStoreClass(List<Store> records);

    void withStoreGrade(List<Store> records);

    void withUserInfo(List<Store> records);

}
