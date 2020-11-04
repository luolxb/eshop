package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.Goods;
import com.soubao.entity.GoodsCollect;
import com.soubao.entity.User;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.GoodsCollectService;
import com.soubao.service.GoodsService;
import com.soubao.dao.GoodsCollectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-22
 */
@Service("goodsCollectService")
public class GoodsCollectServiceImpl extends ServiceImpl<GoodsCollectMapper, GoodsCollect> implements GoodsCollectService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsCollectMapper goodsCollectMapper;

    @Override
    public void addGoodsCollection(User user, Goods goods) {

        GoodsCollect goodsCollect = getOne(new QueryWrapper<GoodsCollect>()
                .eq("user_id", user.getUserId())
                .eq("goods_id", goods.getGoodsId()).last("limit 1"));
        if (goodsCollect == null) {
            GoodsCollect gc = new GoodsCollect();
            gc.setGoodsId(goods.getGoodsId());
            gc.setUserId(user.getUserId());
            gc.setAddTime(System.currentTimeMillis() / 1000);
            save(gc);
            goodsService.update(new UpdateWrapper<Goods>().setSql("comment_count=comment_count+1").eq("goods_id", goods.getGoodsId()));
        }
    }

    @Override
    public IPage<GoodsCollect> getCollectPage(Page<GoodsCollect> collectPage, QueryWrapper<GoodsCollect> wrapper) {
        return goodsCollectMapper.selectCollectPage(collectPage, wrapper);
    }

    @Override
    public void removeGoodsCollections(User user, List<Goods> goodsList) {
        if (goodsList.size() > 0) {
            Set<Integer> ids = goodsList.stream().map(Goods::getGoodsId).collect(Collectors.toSet());
            Set<Integer> collectIds = list(new QueryWrapper<GoodsCollect>().eq("user_id", user.getUserId()).in("goods_id", ids))
                    .stream().map(GoodsCollect::getCollectId).collect(Collectors.toSet());
            if (removeByIds(collectIds)) {
                goodsService.update(new UpdateWrapper<Goods>().setSql("comment_count=comment_count-1").in("goods_id", ids));
            } else {
                throw new ShopException(ResultEnum.FAIL);
            }
        }
    }
}
