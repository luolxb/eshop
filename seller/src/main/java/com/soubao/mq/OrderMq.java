package com.soubao.mq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.AccountLogStore;
import com.soubao.entity.OrderStatis;
import com.soubao.entity.Store;
import com.soubao.service.AccountLogStoreService;
import com.soubao.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderMq {

    @Autowired
    private AccountLogStoreService accountLogStoreService;

    @Autowired
    private StoreService storeService;

    @RabbitListener(queues = "settle_order.seller")
    public void settleOrdersAfter(List<OrderStatis> orderStatisList) {
        List<AccountLogStore> accountLogStores = new ArrayList<>();
        Set<Integer> storeIds = new HashSet<>();
        for(OrderStatis orderStatis : orderStatisList){
            AccountLogStore accountLogStore = new AccountLogStore();
            accountLogStore.setStoreId(orderStatis.getStoreId());
            accountLogStore.setStoreMoney(orderStatis.getResultTotals());
            accountLogStore.setPendingMoney(orderStatis.getResultTotals().negate());
            accountLogStore.setDesc("平台订单结算");
            accountLogStore.setChangeTime(System.currentTimeMillis() / 1000);
            accountLogStores.add(accountLogStore);
            storeIds.add(orderStatis.getStoreId());
        }
        accountLogStoreService.saveBatch(accountLogStores);
        Map<Integer, Store> storeMap = storeService.list(new QueryWrapper<Store>().select("store_id,store_name,store_money,pending_money")
                .in("store_id", storeIds)).stream().collect(Collectors.toMap(Store::getStoreId, item->item));
        for(AccountLogStore accountLogStore : accountLogStores){
            if(storeMap.containsKey(accountLogStore.getStoreId())){
                Store store = storeMap.get(accountLogStore.getStoreId());
                store.setStoreMoney(store.getStoreMoney().add(accountLogStore.getStoreMoney()));
                store.setPendingMoney(store.getPendingMoney().add(accountLogStore.getPendingMoney()));
            }
        }
        storeService.updateBatchById(storeMap.values());
    }

}
