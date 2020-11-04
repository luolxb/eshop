package com.soubao.service;

import com.soubao.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyr
 * @since 2019-10-30
 */
public interface TeamGoodsItemService extends IService<TeamGoodsItem> {
    /**
     * 生成主订单
     * @return
     */
    Order getMasterOrder(User user, GoodsSku goodsSku, Integer goodsNum);

    /**
     * 根据拼团订单，请求数据生成主订单
     * @return
     */
    Order getMasterOrderByTeamOrder(User user, Order teamOrder, Order requestOrder);

    /**
     * 提交订单前校验
     */
    void checkPayOrder(Order teamOrder, Order requestOrder);

    /**
     * 添加团长团员日志
     * @param order
     */
    void log(Order order);

    /**
     * 带上商品详细
     * @param teamGoodsItems
     */
    void withGoodsSku(List<TeamGoodsItem> teamGoodsItems);

    /**
     * 添加拼团规格
     * @param teamGoodsItemList
     * @param teamActivity
     */
    void saveByTeam(List<TeamGoodsItem> teamGoodsItemList, TeamActivity teamActivity);
}
