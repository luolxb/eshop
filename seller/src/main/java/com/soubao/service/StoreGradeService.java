package com.soubao.service;

import com.soubao.entity.StoreGrade;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 店铺等级表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-12-06
 */
public interface StoreGradeService extends IService<StoreGrade> {
    //删除店铺等级
    void remove(Integer gradeId);

    //新增店铺等级
    void insert(StoreGrade storeGrade);

    //更新店铺等级
    void update(StoreGrade storeGrade);
}
