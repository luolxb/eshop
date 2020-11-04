package com.soubao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soubao.entity.Comment;
import com.soubao.entity.GoodsCommentStatistic;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface CommentMapper extends BaseMapper<Comment> {

    List<GoodsCommentStatistic> selectGoodsCommentStatistic(
            @Param("goodsIds") Set<Integer> goodsIds);
}
