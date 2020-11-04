package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.OrderGoodsMapper;
import com.soubao.dao.OrderMapper;
import com.soubao.entity.Order;
import com.soubao.entity.OrderGoods;
import com.soubao.entity.Store;
import com.soubao.service.OrderGoodsService;
import com.soubao.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service("orderGoodsService")
public class OrderGoodsServiceImpl extends ServiceImpl<OrderGoodsMapper, OrderGoods> implements OrderGoodsService {

    @Autowired
    private SellerService sellerService;

    @Override
    public int getCommentCountByUserAndStatusByUserId(Integer userId, int status) {
        return ((OrderGoodsMapper)baseMapper).selectCommentCountByUserAndStatusByUserId(userId, status);
    }

    @Override
    public IPage<OrderGoods> getUserOrderGoodsCommentPage(Page<OrderGoods> page, Integer userId, Integer isComment) {
        return ((OrderGoodsMapper)baseMapper).selectOrderGoodsCommentPage(page, userId, isComment);
    }

    @Override
    public void withStore(List<OrderGoods> records) {
        if(records.size() > 0){
            Set<Integer> storeIds = records.stream().map(OrderGoods::getStoreId).collect(Collectors.toSet());
            Map<Integer, Store> storeMap = sellerService.getStoresById(storeIds).stream().collect(Collectors.toMap(Store::getStoreId, store -> store));
            records.forEach(orderGoods -> {
                if (storeMap.containsKey(orderGoods.getStoreId())) {
                    orderGoods.setStoreName(storeMap.get(orderGoods.getStoreId()).getStoreName());
                }
            });
        }
    }
}
