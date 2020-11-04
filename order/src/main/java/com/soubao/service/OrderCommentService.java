package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Order;
import com.soubao.entity.OrderComment;
import com.soubao.entity.Store;

import java.util.List;

/**
 * <p>
 * 订单评分表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-12
 */
public interface OrderCommentService extends IService<OrderComment> {
    //订单添加评论
    void orderAddComment(Order order, OrderComment orderComment);

    //获取店铺所有订单评论平均分
    Store getStoreScore(Store store);

    void withUser(List<OrderComment> records);

    void withStore(List<OrderComment> records);
}
