package com.soubao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Order;
import com.soubao.entity.TeamActivity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 拼团活动表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-10-30
 */
public interface TeamActivityService extends IService<TeamActivity> {
    IPage<TeamActivity> getGoodsPage(Page page, TeamActivity teamActivity);

    /**
     * 拼团列表页
     * @param page
     * @param teamActivityQuery
     * @return
     */
    IPage<TeamActivity> selectPageWithStore(Page page, TeamActivity teamActivityQuery);

    /**
     * 恢复活动下的商品或者商品规格为普通商品
     * @param teamActivity
     */
    void restoreGoodsSku(TeamActivity teamActivity);

    void withTeamActivity(List<Order> records);

    /**
     * 抽奖
     * @param teamActivity
     */
    void lottery(Integer sellerId, TeamActivity teamActivity);

    void withStore(List<TeamActivity> records);
}
