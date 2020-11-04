package com.soubao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.OrderGoods;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderGoodsService")
public interface OrderGoodsService extends IService<OrderGoods> {
    //获取用户订单商品 评论数/未评论数 status 0:未评 1:已评
    int getCommentCountByUserAndStatusByUserId(Integer userId, int status);
    //获取用户订单商品评论列表
    IPage<OrderGoods> getUserOrderGoodsCommentPage(Page<OrderGoods> page, Integer userId, Integer status);

    void withStore(List<OrderGoods> records);
}
