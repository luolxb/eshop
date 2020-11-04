package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.StoreGradeMapper;
import com.soubao.entity.Store;
import com.soubao.entity.StoreGrade;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.StoreGradeService;
import com.soubao.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 店铺等级表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-12-06
 */
@Service
public class StoreGradeServiceImpl extends ServiceImpl<StoreGradeMapper, StoreGrade> implements StoreGradeService {
    @Autowired
    private StoreService storeService;

    @Override
    public void remove(Integer gradeId) {
        if (storeService.count(new QueryWrapper<Store>().eq("grade_id", gradeId)) > 0) {
            throw new ShopException(ResultEnum.GRADE_HAS_STORE);
        }
        this.removeById(gradeId);
    }

    @Override
    public void insert(StoreGrade storeGrade) {
        if (this.count(new QueryWrapper<StoreGrade>().eq("sg_name", storeGrade.getSgName())) > 0) {
            throw new ShopException(ResultEnum.STORE_GRADE_EXISTS);
        }
        storeGrade.setSgActLimits("all");
        this.save(storeGrade);
    }

    @Override
    public void update(StoreGrade storeGrade) {
        if (this.count(new QueryWrapper<StoreGrade>().eq("sg_name", storeGrade.getSgName())
                .ne("sg_id", storeGrade.getSgId())) > 0) {
            throw new ShopException(ResultEnum.STORE_GRADE_EXISTS);
        }
        this.updateById(storeGrade);
    }
}
