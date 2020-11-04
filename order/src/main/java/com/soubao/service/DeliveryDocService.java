package com.soubao.service;

import com.soubao.entity.DeliveryDoc;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 发货单 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-10-14
 */
public interface DeliveryDocService extends IService<DeliveryDoc> {

    void withRegions(List<DeliveryDoc> deliveryDocs);

    void withRegions(DeliveryDoc deliveryDocs);

    void withSeller(List<DeliveryDoc> deliveryDocs);

    Object getExpress(DeliveryDoc deliveryDoc);
}
