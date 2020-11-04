package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.GuaranteeApplyMapper;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 消费者保障服务申请表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-03-11
 */
@Service
public class GuaranteeApplyServiceImpl extends ServiceImpl<GuaranteeApplyMapper, GuaranteeApply> implements GuaranteeApplyService {
    @Autowired
    private GuaranteeItemService guaranteeItemService;
    @Autowired
    private GuaranteeLogService guaranteeLogService;
    @Autowired
    private GuaranteeService guaranteeService;
    @Autowired
    private GuaranteeCostlogService guaranteeCostlogService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private StoreService storeService;


    @Override
    public void withGuaranteeItem(List<GuaranteeApply> guaranteeApplyList) {
        Set<Integer> grtIds = guaranteeApplyList.stream().map(GuaranteeApply::getGrtId).collect(Collectors.toSet());
        if (!grtIds.isEmpty()) {
            Map<Integer, GuaranteeItem> guaranteeItemMap = guaranteeItemService.listByIds(grtIds).stream()
                    .collect(Collectors.toMap(GuaranteeItem::getGrtId, guaranteeItem -> guaranteeItem));
            for (GuaranteeApply guaranteeApply : guaranteeApplyList) {
                guaranteeApply.setGrtName(guaranteeItemMap.get(guaranteeApply.getGrtId()).getGrtName());
            }
        }
    }

    @Override
    public void updateAuditstate(GuaranteeApply guaranteeApply, String logMsg) {
        updateById(guaranteeApply);
        Guarantee guarantee = new Guarantee();
        guarantee.setAuditstate(guaranteeApply.getAuditstate());
        GuaranteeLog guaranteeLog = new GuaranteeLog();
        //TODO 暂时无法获取登录的admin
        Integer adminId = 1;
        String adminName = "admin";
        switch (guaranteeApply.getAuditstate()) {
            case 0:
            case 3:
                throw new ShopException(ResultEnum.CAN_NOT_REPEATED_CHANGES_STATE);
            case 1:
                if (guaranteeApply.getApplyType() == 1) {
                    guaranteeLog.setLogMsg("审核通过，待支付保证金");
                } else {
                    guaranteeLog.setLogMsg("管理员审核通过店铺退出保障服务的申请");
                    guarantee.setJoinstate(0);
                    guarantee.setQuitstate(0);
                    guarantee.setAuditstate(0);
                }
                break;
            case 2:
                if (guaranteeApply.getApplyType() == 0) {
                    guaranteeLog.setLogMsg("管理员拒绝店铺退出保障服务的申请，原因：" + logMsg);
                    guarantee.setQuitstate(2);
                    guarantee.setAuditstate(4);
                } else {
                    guaranteeLog.setLogMsg("审核未通过，原因：" + logMsg);
                    guarantee.setJoinstate(0);
                }
                break;
            case 4:
                guaranteeLog.setLogMsg("保证金审核通过");
                guarantee.setJoinstate(2);
                guarantee.setQuitstate(0);
                if (guaranteeApply.getCost().compareTo(BigDecimal.ZERO) > 0) {
                    GuaranteeCostlog guaranteeCostlog = new GuaranteeCostlog();
                    guaranteeCostlog.setGrtId(guaranteeApply.getGrtId());
                    guaranteeCostlog.setGrtName(guaranteeApply.getGrtName());
                    guaranteeCostlog.setStoreId(guaranteeApply.getStoreId());
                    guaranteeCostlog.setStoreName(guaranteeApply.getStoreName());
                    guaranteeCostlog.setAdminId(adminId);
                    guaranteeCostlog.setAdminName(adminName);
                    guaranteeCostlog.setPrice(guaranteeApply.getCost());
                    guaranteeCostlog.setAddTime(System.currentTimeMillis() / 1000);
                    guaranteeCostlog.setDesc("申请加入保障服务，支付保证金");
                    guaranteeCostlogService.save(guaranteeCostlog);
                }
                break;
            case 5:
                guaranteeLog.setLogMsg("保证金审核失败，原因：" + logMsg);
                break;
        }
        guaranteeService.update(guarantee, new UpdateWrapper<Guarantee>()
                .eq("grt_id", guaranteeApply.getGrtId()).eq("store_id", guaranteeApply.getStoreId()));
        guaranteeLog.setLogStoreid(guaranteeApply.getStoreId());
        guaranteeLog.setLogStorename(guaranteeApply.getStoreName());
        guaranteeLog.setLogGrtid(guaranteeApply.getGrtId());
        guaranteeLog.setLogGrtname(guaranteeApply.getGrtName());
        guaranteeLog.setLogAddtime(System.currentTimeMillis() / 1000);
        guaranteeLog.setLogRole("管理员");
        guaranteeLog.setLogUserid(adminId);
        guaranteeLog.setLogUsername(adminName);
        guaranteeLogService.save(guaranteeLog);
    }

    @Override
    public void costPay(Integer grtId, Integer storeId, String costimg, Integer sellerId) {
        GuaranteeApply guaranteeApply = getOne(new QueryWrapper<GuaranteeApply>().eq("grt_id", grtId)
                .eq("store_id", storeId).orderByDesc("id"), false);
        guaranteeApply.setCostimg(costimg);
        guaranteeApply.setAuditstate(3);
        updateById(guaranteeApply);
        guaranteeService.update(new UpdateWrapper<Guarantee>().set("costimg", costimg).set("auditstate", 3)
                .eq("grt_id", grtId).eq("store_id", storeId));
        Seller seller = sellerService.getById(sellerId);
        Store store = storeService.getById(storeId);
        GuaranteeItem guaranteeItem = guaranteeItemService.getOne(new QueryWrapper<GuaranteeItem>()
                .eq("grt_id", grtId).eq("grt_state", 1));
        GuaranteeLog guaranteeLog = new GuaranteeLog();
        guaranteeLog.setLogStoreid(storeId);
        guaranteeLog.setLogStorename(store.getStoreName());
        guaranteeLog.setLogGrtid(grtId);
        guaranteeLog.setLogGrtname(guaranteeItem.getGrtName());
        guaranteeLog.setLogMsg("店铺支付保证金");
        guaranteeLog.setLogAddtime(System.currentTimeMillis() / 1000);
        guaranteeLog.setLogRole("商家");
        guaranteeLog.setLogUserid(seller.getUserId());
        guaranteeLog.setLogUsername(seller.getSellerName());
        guaranteeLogService.save(guaranteeLog);
    }
}
