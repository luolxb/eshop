package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Comment;
import com.soubao.entity.Goods;
import com.soubao.entity.GoodsCommentStatistic;

import java.util.List;
import java.util.Set;

public interface CommentService extends IService<Comment> {
    // 给订单商品添加评论
    void orderGoodsAddComment(Comment comment);

    // 获取商品列表的评价信息
    List<GoodsCommentStatistic> getGoodsListCommentStatisticByGoodsIds(Set<Integer> goodsIds);
    // 获取商品的评价详情
    GoodsCommentStatistic getGoodsCommentStatistic(Goods goods);

    Comment getComment(Integer commentId);

    void withUser(List<Comment> records);

    void withGoodsName(List<Comment> records);
}
