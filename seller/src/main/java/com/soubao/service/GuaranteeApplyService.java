package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.GuaranteeApply;

import java.util.List;

/**
 * <p>
 * 消费者保障服务申请表 服务类
 * </p>
 *
 * @author dyr
 * @since 2020-03-11
 */
public interface GuaranteeApplyService extends IService<GuaranteeApply> {

    void withGuaranteeItem(List<GuaranteeApply> guaranteeApplyList);

    //店铺保障服务审核
    void updateAuditstate(GuaranteeApply guaranteeApply, String logMsg);

    //保障服务支付保证金
    void costPay(Integer grtId, Integer storeId, String costimg, Integer sellerId);
}
