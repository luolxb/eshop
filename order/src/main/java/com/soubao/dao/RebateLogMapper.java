package com.soubao.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.RebateLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soubao.vo.UserLowerVo;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-10-28
 */
public interface RebateLogMapper extends BaseMapper<RebateLog> {

    IPage<UserLowerVo> selectLowerPage(Page page, Integer userId, Integer level);

    //获取用户分销订单商品列表
    IPage<RebateLog> selectRebateOrderGoodsList(Page page, @Param("userId") Integer userId, @Param("status") Set<Integer> status);
}
