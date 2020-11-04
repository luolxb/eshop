package com.soubao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.OrderGoods;
import org.apache.ibatis.annotations.Param;

public interface OrderGoodsMapper extends BaseMapper<OrderGoods> {

    //查询用户订单商品 评论数/未评论数 status 0:未评 1:已评
    int selectCommentCountByUserAndStatusByUserId(@Param("user_id") Integer userId, int status);

    //查询用户订单商品的评论列表 status 0:未评 1:已评 2:全部
    IPage<OrderGoods> selectOrderGoodsCommentPage(Page<OrderGoods> page, @Param("userId")Integer userId, @Param("isComment") Integer isComment);
}