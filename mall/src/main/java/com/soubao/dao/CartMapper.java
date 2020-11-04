package com.soubao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soubao.entity.Cart;
import com.soubao.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-08-29
 */
public interface CartMapper extends BaseMapper<Cart> {
    //查询用户购物车商品数量
    int selectCartGoodsCountByUser(@Param("user") User user);
}
