package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.StoreWithdrawals;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.vo.StoreWithdrawalsExcel;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyr
 * @since 2019-12-18
 */
public interface StoreWithdrawalsService extends IService<StoreWithdrawals> {
    //获取商家提现申请列表/转款列表
    IPage<StoreWithdrawals> getStoreWithdrawalsPage(Page<StoreWithdrawals> page,Integer storeId,Integer startTime,Integer endTime,Integer status,String bankCard,String realname );

    //根据id获取商家提现申请/转款记录
    StoreWithdrawals getStoreWithdrawalsById(Integer id);

    //获取商家转款导出数据
    List<StoreWithdrawalsExcel> getStoreWithdrawalsExportData(Set<Integer> ids, Integer storeId, Integer startTime, Integer endTime, Integer status, String bankCard, String realname);
}
