package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.dto.DeliveryPickOrderRq;
import com.soubao.entity.PickOrder;
import com.soubao.entity.vo.PickOrderRq;

public interface PickOrderService extends IService<PickOrder> {
    void pickOrderReq(PickOrderRq pickOrderRq);

    void deliveryPickOrder(DeliveryPickOrderRq deliveryPickOrderRq);

    void confirmReceipt(Integer pickOrderId);
}
