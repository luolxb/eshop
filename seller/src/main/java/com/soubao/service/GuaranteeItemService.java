package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.GuaranteeItem;

import java.util.List;

/**
 * <p>
 * 消费者保障服务项目表 服务类
 * </p>
 *
 * @author dyr
 * @since 2020-03-11
 */
public interface GuaranteeItemService extends IService<GuaranteeItem> {

    //店铺保障服务详情列表
    List<GuaranteeItem> listStoreGuaranteeInfo(Integer storeId);
}
