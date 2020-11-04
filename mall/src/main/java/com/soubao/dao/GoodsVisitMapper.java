package com.soubao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soubao.entity.GoodsVisit;
import com.soubao.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品浏览历史表 Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-08-15
 */
public interface GoodsVisitMapper extends BaseMapper<GoodsVisit> {
    List<GoodsVisit> selectVisitCatCount(@Param("user") User user);
}
