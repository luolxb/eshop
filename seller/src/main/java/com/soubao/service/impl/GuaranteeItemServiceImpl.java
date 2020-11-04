package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.GuaranteeItemMapper;
import com.soubao.entity.Guarantee;
import com.soubao.entity.GuaranteeItem;
import com.soubao.service.GuaranteeItemService;
import com.soubao.service.GuaranteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 消费者保障服务项目表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-03-11
 */
@Service
public class GuaranteeItemServiceImpl extends ServiceImpl<GuaranteeItemMapper, GuaranteeItem> implements GuaranteeItemService {
    @Autowired
    private GuaranteeService guaranteeService;

    @Override
    public List<GuaranteeItem> listStoreGuaranteeInfo(Integer storeId) {
        List<GuaranteeItem> guaranteeItemList = this.list(
                new QueryWrapper<GuaranteeItem>().eq("grt_state", 1).orderByAsc("grt_sort"));
        Map<Integer, Guarantee> guaranteeMap = guaranteeService.list(new QueryWrapper<Guarantee>().eq("store_id", storeId))
                .stream().collect(Collectors.toMap(Guarantee::getGrtId, guarantee -> guarantee));
        for (GuaranteeItem guaranteeItem : guaranteeItemList) {
            Guarantee guarantee = guaranteeMap.get(guaranteeItem.getGrtId());
            if (guarantee == null) {
                guaranteeItem.setJoinState(0);
                guaranteeItem.setAuditState(0);
                guaranteeItem.setQuitState(0);
            } else {
                guaranteeItem.setJoinState(guarantee.getJoinstate());
                guaranteeItem.setAuditState(guarantee.getAuditstate());
                guaranteeItem.setQuitState(guarantee.getQuitstate());
            }
        }
        return guaranteeItemList;
    }
}
