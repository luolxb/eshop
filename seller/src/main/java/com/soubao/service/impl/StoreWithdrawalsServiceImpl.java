package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.dao.StoreWithdrawalsMapper;
import com.soubao.entity.StoreWithdrawals;
import com.soubao.service.StoreWithdrawalsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.vo.StoreWithdrawalsExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-12-18
 */
@Service
public class StoreWithdrawalsServiceImpl extends ServiceImpl<StoreWithdrawalsMapper, StoreWithdrawals> implements StoreWithdrawalsService {
    @Autowired
    private StoreWithdrawalsMapper storeWithdrawalsMapper;

    @Override
    public IPage<StoreWithdrawals> getStoreWithdrawalsPage(Page<StoreWithdrawals> page, Integer storeId,Integer startTime,Integer endTime,Integer status,String bankCard,String realname ) {
        return storeWithdrawalsMapper.selectStoreWithdrawalsPage(page,  storeId, startTime, endTime, status, bankCard, realname );
    }

    @Override
    public StoreWithdrawals getStoreWithdrawalsById(Integer id) {
        return storeWithdrawalsMapper.selectStoreWithdrawalsById(id);
    }

    @Override
    public List<StoreWithdrawalsExcel> getStoreWithdrawalsExportData(Set<Integer> ids, Integer storeId, Integer startTime, Integer endTime, Integer status, String bankCard, String realname) {
        return storeWithdrawalsMapper.selectStoreWithdrawalsExportData(ids,  storeId, startTime, endTime, status, bankCard, realname );
    }
}
