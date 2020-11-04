package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.DateVoMapper;
import com.soubao.entity.Goods;
import com.soubao.entity.GoodsVisit;
import com.soubao.entity.User;
import com.soubao.service.GoodsVisitService;
import com.soubao.dao.GoodsVisitMapper;
import com.soubao.vo.DateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品浏览历史表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-15
 */
@Service("goodsVisitService")
public class GoodsVisitServiceImpl extends ServiceImpl<GoodsVisitMapper, GoodsVisit> implements GoodsVisitService {
    @Autowired
    private DateVoMapper dateVoMapper;
    @Autowired
    private GoodsVisitMapper goodsVisitMapper;

    @Override
    public void loggingGoods(User user, Goods goods) {
        GoodsVisit exGoodsVisit = getOne((new QueryWrapper<GoodsVisit>()).
                eq("user_id", user.getUserId()).
                eq("goods_id", goods.getGoodsId()).last("LIMIT 1"));
        if (exGoodsVisit == null) {
            GoodsVisit goodsVisit = new GoodsVisit();
            goodsVisit.setCatId1(goods.getCatId1());
            goodsVisit.setCatId2(goods.getCatId2());
            goodsVisit.setCatId3(goods.getCatId3());
            goodsVisit.setGoodsId(goods.getGoodsId());
            goodsVisit.setUserId(user.getUserId());
            goodsVisit.setVisitTime(System.currentTimeMillis() / 1000);
            save(goodsVisit);
        } else {
            exGoodsVisit.setVisitTime(System.currentTimeMillis() / 1000);
            updateById(exGoodsVisit);
        }
    }

    @Override
    public Page<DateVo> getVisitByDateGroup(Page<DateVo> page, QueryWrapper<GoodsVisit> wrapper) {
        return dateVoMapper.selectVisitByDateGroup(page, wrapper);
    }

    @Override
    public List<GoodsVisit> getVisitCatCount(User user) {
        return goodsVisitMapper.selectVisitCatCount(user);
    }
}
