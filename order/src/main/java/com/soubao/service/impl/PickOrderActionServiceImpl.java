package com.soubao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.PickOrderActionMapper;
import com.soubao.entity.PickOrder;
import com.soubao.entity.PickOrderAction;
import com.soubao.service.PickOrderActionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 提货订单的操作记录服务层
 */
@Service
@Slf4j
public class PickOrderActionServiceImpl extends ServiceImpl<PickOrderActionMapper, PickOrderAction> implements PickOrderActionService {

    /**
     * 保存提货订单操作记录
     *
     * @param pickOrder
     * @param statusDesc
     * @param actionNote
     * @param sellerId
     * @param userType
     * @param storeId
     */
    @Override
    public void addActionLog(PickOrder pickOrder, String statusDesc, String actionNote, Integer sellerId, int userType, Integer storeId) {
        PickOrderAction pickOrderAction = new PickOrderAction();
        pickOrderAction.setPrickOrderId(pickOrder.getPickOrderId());
        pickOrderAction.setActionUser(sellerId);
        pickOrderAction.setStoreId(storeId);
        pickOrderAction.setUserType(userType);
        pickOrderAction.setActionNote(actionNote);
        pickOrderAction.setPickOrderStatus(pickOrder.getPickOrderStatus());
        pickOrderAction.setLogTime(System.currentTimeMillis() / 1000);
        pickOrderAction.setStatusDesc(statusDesc);
        this.save(pickOrderAction);
    }
}
