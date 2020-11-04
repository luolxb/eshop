package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.GuaranteeMapper;
import com.soubao.dto.GuaranteeApplyDto;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 店铺消费者保障服务加入情况表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-03-11
 */
@Service
public class GuaranteeServiceImpl extends ServiceImpl<GuaranteeMapper, Guarantee> implements GuaranteeService {
    @Autowired
    private GuaranteeItemService guaranteeItemService;
    @Autowired
    private GuaranteeLogService guaranteeLogService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private GuaranteeApplyService guaranteeApplyService;
    @Autowired
    private SellerService sellerService;

    @Override
    public void withGuaranteeItem(List<Guarantee> guaranteeList) {
        Set<Integer> grtIds = guaranteeList.stream().map(Guarantee::getGrtId).collect(Collectors.toSet());
        if (!grtIds.isEmpty()) {
            Map<Integer, GuaranteeItem> guaranteeItemMap = guaranteeItemService.listByIds(grtIds)
                    .stream().collect(Collectors.toMap(GuaranteeItem::getGrtId, guaranteeItem -> guaranteeItem));
            for (Guarantee guarantee : guaranteeList) {
                guarantee.setGrtName(guaranteeItemMap.get(guarantee.getGrtId()).getGrtName());
            }
        }
    }

    @Override
    public void updateIsOpen(Guarantee guarantee, String logMsg) {
        Guarantee dbGuarantee = this.getById(guarantee.getId());
        if (Objects.equals(dbGuarantee.getIsopen(), guarantee.getIsopen())) {
            throw new ShopException(ResultEnum.CAN_NOT_REPEATED_CHANGES_SWITCH);
        }
        this.updateById(guarantee);
        //TODO 暂时无法获取登录的admin
        Integer adminId = 1;
        String adminName = "admin";
        GuaranteeLog guaranteeLog = new GuaranteeLog();
        if (guarantee.getIsopen() == 1) {
            guaranteeLog.setLogMsg("关闭状态修改为“允许使用”");
        } else {
            guaranteeLog.setLogMsg("关闭状态修改为“永久禁止使用”，原因：" + logMsg);
        }
        guaranteeLog.setLogStoreid(guarantee.getStoreId());
        guaranteeLog.setLogStorename(guarantee.getStoreName());
        guaranteeLog.setLogGrtid(guarantee.getGrtId());
        guaranteeLog.setLogGrtname(guarantee.getGrtName());
        guaranteeLog.setLogAddtime(System.currentTimeMillis() / 1000);
        guaranteeLog.setLogRole("管理员");
        guaranteeLog.setLogUserid(adminId);
        guaranteeLog.setLogUsername(adminName);
        guaranteeLogService.save(guaranteeLog);
    }

    @Override
    public void apply(GuaranteeApplyDto guaranteeApplyDto) {
        GuaranteeItem guaranteeItem = guaranteeItemService.getOne(
                new QueryWrapper<GuaranteeItem>().eq("grt_state", 1).eq("grt_id", guaranteeApplyDto.getGrtId()));
        Guarantee exGuarantee = this.getOne(new QueryWrapper<Guarantee>()
                .eq("store_id", guaranteeApplyDto.getStoreId()).eq("grt_id", guaranteeApplyDto.getGrtId()));
        if (exGuarantee != null && exGuarantee.getAuditstate() == 0 && (exGuarantee.getJoinstate() == 1 || exGuarantee.getQuitstate() == 1)) {
            throw new ShopException(ResultEnum.IN_REVIEW);
        }
        Store store = storeService.getById(guaranteeApplyDto.getStoreId());
        GuaranteeApply guaranteeApply = new GuaranteeApply();
        guaranteeApply.setGrtId(guaranteeApplyDto.getGrtId());
        guaranteeApply.setStoreId(guaranteeApplyDto.getStoreId());
        guaranteeApply.setAddTime(System.currentTimeMillis() / 1000);
        guaranteeApply.setStoreName(store.getStoreName());
        guaranteeApply.setCost(guaranteeItem.getGrtCost());
        guaranteeApply.setApplyType(guaranteeApplyDto.getApplyType());
        guaranteeApplyService.save(guaranteeApply);

        Guarantee guarantee = new Guarantee();
        GuaranteeLog guaranteeLog = new GuaranteeLog();

        if (guaranteeApplyDto.getApplyType() == 1) {
            guarantee.setJoinstate(1);
            guarantee.setAuditstate(0);
            guaranteeLog.setLogMsg("店铺申请加入保障服务");
        } else {
            guarantee.setQuitstate(1);
            guaranteeLog.setLogMsg("店铺申请退出保障服务");
        }

        guarantee.setGrtId(guaranteeApplyDto.getGrtId());
        guarantee.setStoreId(guaranteeApplyDto.getStoreId());
        guarantee.setStoreName(store.getStoreName());
        guarantee.setCost(guaranteeItem.getGrtCost());

        if (exGuarantee != null) {
            guarantee.setId(exGuarantee.getId());
            this.updateById(guarantee);
        } else {
            this.save(guarantee);
        }

        Seller seller = sellerService.getById(guaranteeApplyDto.getSellerId());
        guaranteeLog.setLogStoreid(guaranteeApplyDto.getStoreId());
        guaranteeLog.setLogStorename(store.getStoreName());
        guaranteeLog.setLogGrtid(guaranteeApplyDto.getGrtId());
        guaranteeLog.setLogGrtname(guaranteeItem.getGrtName());
        guaranteeLog.setLogAddtime(System.currentTimeMillis() / 1000);
        guaranteeLog.setLogRole("商家");
        guaranteeLog.setLogUserid(seller.getUserId());
        guaranteeLog.setLogUsername(seller.getSellerName());
        guaranteeLogService.save(guaranteeLog);
    }
}

