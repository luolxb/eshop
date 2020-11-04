package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.StoreReopenMapper;
import com.soubao.entity.Store;
import com.soubao.entity.StoreGrade;
import com.soubao.entity.StoreReopen;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.StoreGradeService;
import com.soubao.service.StoreReopenService;
import com.soubao.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * <p>
 * 续签内容表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-03-06
 */
@Service
public class StoreReopenServiceImpl extends ServiceImpl<StoreReopenMapper, StoreReopen> implements StoreReopenService {
    @Autowired
    private StoreGradeService storeGradeService;
    @Autowired
    private StoreService storeService;

    @Override
    public void saveStoreReopen(StoreReopen storeReopen) {
        StoreGrade storeGrade = storeGradeService.getById(storeReopen.getReGradeId());
        if (storeGrade == null){
            throw new ShopException(ResultEnum.PARAM_ERROR);
        }
        Store store = storeService.getById(storeReopen.getReStoreId());
        int count = this.count(new QueryWrapper<StoreReopen>().eq("re_store_id", storeReopen.getReStoreId()).notIn("re_state", -1, 2));
        if (count > 0){
            throw new ShopException(ResultEnum.APPLY_UNFINISHED);
        }
        storeReopen.setReGradeName(storeGrade.getSgName());
        storeReopen.setReStoreName(store.getStoreName());
        storeReopen.setReStartTime(System.currentTimeMillis() / 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, storeReopen.getReYear());
        storeReopen.setReEndTime(calendar.getTimeInMillis() / 1000);
        storeReopen.setReCreateTime(System.currentTimeMillis()/ 1000);
        storeReopen.setReState(1);
        this.save(storeReopen);
    }
}
