package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Goods;
import com.soubao.entity.GoodsCollect;
import com.soubao.entity.User;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-22
 */
public interface GoodsCollectService extends IService<GoodsCollect> {
    //收藏商品
    void addGoodsCollection(User user, Goods goods);

    IPage<GoodsCollect> getCollectPage(Page<GoodsCollect> collectPage, QueryWrapper<GoodsCollect> wrapper);

    //取消收藏商品
    void removeGoodsCollections(User user, List<Goods> goodsList);
}
