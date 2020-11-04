package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.GroupBuy;
import com.soubao.vo.GroupBuyGoodsVo;

/**
 * 团购商品表 服务类
 *
 * @author dyr
 * @since 2019-08-19
 */
public interface GroupBuyService extends IService<GroupBuy> {
    // 更新活动状态
    void updateGroupBuyStatus(GroupBuy groupBuy);

    // 删除团购活动
    void removeGroupBuy(Integer promId);

    // 取消商品的团购活动
    void closeProm(Integer promId);

    void schedule();

    void saveGroupBuy(GroupBuy groupBuy);

    void updateGroupBuy(GroupBuy groupBuy);

    IPage<GroupBuyGoodsVo> getGroupBuyGoodsPage(Page<GroupBuy> objectPage, QueryWrapper<GroupBuy> queryWrapper);
}
