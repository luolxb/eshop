package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.FlashSale;

public interface FlashSaleService extends IService<FlashSale> {
    // 获取抢购商品
    IPage<FlashSale> selectFlashSaleGoodsPage(
            Page<FlashSale> page, QueryWrapper<FlashSale> wrapper);

    //// 更新活动状态
    void updateFlashSaleStatus(FlashSale flashSale);

    // 删除抢购活动
    void removeFlashSale(Integer promId);

    // 关闭活动
    void closeProm(Integer promId);

    /** 定时更新过期活动 */
    void schedule();

    // 删除3个月前的活动
    void deleteTask();

    void saveFlashSale(FlashSale flashSale);

    void updateFlashSale(FlashSale flashSale);
}
