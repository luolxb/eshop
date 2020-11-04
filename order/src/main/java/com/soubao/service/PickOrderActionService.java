package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.PickOrder;
import com.soubao.entity.PickOrderAction;

public interface PickOrderActionService extends IService<PickOrderAction> {

    void addActionLog(PickOrder pickOrder, String statusDesc, String actionNote, Integer sellerId, int userType, Integer storeId);
}
