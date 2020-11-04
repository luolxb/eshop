package com.soubao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Order;
import com.soubao.entity.RebateLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.vo.UserLowerVo;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyr
 * @since 2019-10-28
 */
public interface RebateLogService extends IService<RebateLog> {
    IPage<UserLowerVo> selectLowerPage(Page page, Integer userId, Integer level);

    //获取用户分销订单商品列表
    IPage<RebateLog> getRebateOrderGoodsPage(Page<RebateLog> page, Integer userId, Set<Integer> statusSet);

    void withUserByUserLowerVo(List<UserLowerVo> records);

    void withUser(List<RebateLog> records);

    void createRebateLog(List<Order> orders);
}
