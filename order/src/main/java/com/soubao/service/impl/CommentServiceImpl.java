package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.common.constant.OrderConstant;
import com.soubao.dao.CommentMapper;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {
    @Autowired private CommentMapper commentMapper;
    @Autowired private OrderService orderService;
    @Autowired private OrderGoodsService orderGoodsService;
    @Autowired private AmqpTemplate rabbitTemplate;
    @Autowired private UserService userService;
    @Autowired private MallService mallService;

    @Override
    public void orderGoodsAddComment(Comment comment) {
        OrderGoods orderGoods = orderGoodsService.getById(comment.getRecId());
        if (orderGoods == null) {
            throw new ShopException(ResultEnum.NO_FIND_ORDER);
        }
        Order order = orderService.getById(orderGoods.getOrderId());
        if (1 == orderGoods.getIsComment()) {
            throw new ShopException(ResultEnum.ORDER_HAVE_COMMENT);
        }
        if (!order.getOrderStatus().equals(OrderConstant.DELIVERY)) {
            throw new ShopException(ResultEnum.ORDER_NOT_FINISH);
        }
        comment.setGoodsId(orderGoods.getGoodsId());
        comment.setOrderId(orderGoods.getOrderId());
        comment.setStoreId(orderGoods.getStoreId());
        comment.setShopId(orderGoods.getShopId());
        comment.setAddTime(System.currentTimeMillis() / 1000);
        comment.setSpecKeyName(orderGoods.getSpecKeyName());
        comment.setOrderSn(order.getOrderSn());
        // 开始插入
        save(comment);
        /// 更新订单商品表状态
        orderGoods.setIsComment(1);
        orderGoodsService.updateById(orderGoods);
        int orderGoodsNoCommentCount =
                orderGoodsService.count(
                        (new QueryWrapper<OrderGoods>())
                                .eq("order_id", comment.getOrderId())
                                .eq("is_comment", 0));
        if (orderGoodsNoCommentCount == 0) {
            // 如果所有的商品都已经评价了,订单状态改成已评价
            order.setOrderStatus(OrderConstant.COMMENTED);
            orderService.updateById(order);
        }
        rabbitTemplate.convertAndSend("comment_order", "", orderGoods);
    }

    @Override
    public List<GoodsCommentStatistic> getGoodsListCommentStatisticByGoodsIds(
            Set<Integer> goodsIds) {
        return commentMapper.selectGoodsCommentStatistic(goodsIds);
    }

    @Override
    public GoodsCommentStatistic getGoodsCommentStatistic(Goods goods) {
        Set<Integer> goodsIds = new HashSet<>();
        goodsIds.add(goods.getGoodsId());
        List<GoodsCommentStatistic> goodsCommentStatisticList =
                getGoodsListCommentStatisticByGoodsIds(goodsIds);
        if (goodsCommentStatisticList.size() > 0) {
            return goodsCommentStatisticList.get(0);
        } else {
            return new GoodsCommentStatistic(goods.getGoodsId());
        }
    }

    @Override
    public Comment getComment(Integer commentId) {
        Comment comment = this.getById(commentId);
        User user = userService.getUserInfoById(comment.getUserId());
        comment.setNickname(user.getNickname());
        comment.setHeadPic(user.getHeadPic());
        return comment;
    }

    @Override
    public void withUser(List<Comment> records) {
        if(records.size() > 0){
            Set<Integer> userIds = records.stream().map(Comment::getUserId).collect(Collectors.toSet());
            Map<Integer, User> userMap = userService.usersByIds(userIds).stream().collect(Collectors.toMap(User::getUserId, user -> user));
            User tmpUser;
            for(Comment record : records){
                if(userMap.containsKey(record.getUserId())){
                    tmpUser = userMap.get(record.getUserId());
                    record.setNickname(tmpUser.getNickname());
                    record.setHeadPic(tmpUser.getHeadPic());
                }
            }
        }
    }

    @Override
    public void withGoodsName(List<Comment> records) {
        if (records.size() > 0) {
            Set<Integer> recId = records.stream().map(Comment::getRecId).collect(Collectors.toSet());
            Map<Integer, String> goodsNameMap = orderGoodsService.list(new QueryWrapper<OrderGoods>().select("rec_id,goods_name").in("rec_id", recId))
                    .stream()
                    .collect(Collectors.toMap(OrderGoods::getRecId, OrderGoods::getGoodsName));
            records.forEach(comment -> {
                if (goodsNameMap.containsKey(comment.getRecId())) {
                    comment.setGoodsName(goodsNameMap.get(comment.getRecId()));
                }
            });
        }
    }
}
