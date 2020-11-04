package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.SpecItem;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-22
 */
public interface SpecItemService extends IService<SpecItem> {
    /**
     * 店铺删除规格子项
     */
    void deleteSpecItem(SpecItem specItem);

    /**
     * 批量删除规格子项
     */
    void deleteSpecItemList(List<SpecItem> specItemList);
}
