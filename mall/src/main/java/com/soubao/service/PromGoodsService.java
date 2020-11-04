package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Goods;
import com.soubao.entity.PromGoods;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-19
 */
public interface PromGoodsService extends IService<PromGoods> {
    //删除优惠促销活动
    void removePromGoods(Integer promId);

    //关闭优惠促销活动
    void closeProm(Integer promId);

    void schedule();

    PromGoods getPromGoods(Integer promGoodsId);

    void addPromGoods(PromGoods promGoods);

    void updatePromGoods(PromGoods promGoods);

    IPage<Goods> goodsPage(Page<PromGoods> page, QueryWrapper<PromGoods> wrapper);
}
