package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.Goods;
import com.soubao.entity.GoodsImages;
import com.soubao.service.GoodsImagesService;
import com.soubao.dao.GoodsImagesMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-21
 */
@Service("goodsImagesService")
public class GoodsImagesServiceImpl extends ServiceImpl<GoodsImagesMapper, GoodsImages> implements GoodsImagesService {

    @Override
    public void updateGoodsImage(Goods goods, List<GoodsImages> goodsImagesList) {
        if(goodsImagesList != null){
            remove((new QueryWrapper<GoodsImages>()).eq("goods_id", goods.getGoodsId()));
            for(GoodsImages goodsImages : goodsImagesList){
                goodsImages.setGoodsId(goods.getGoodsId());
            }
            saveBatch(goodsImagesList);
        }
    }

    @Override
    public void addGoodsImage(Goods goods, List<GoodsImages> goodsImageList) {
        if(goodsImageList != null && goodsImageList.size() > 0){
            for(GoodsImages goodsImages : goodsImageList){
                goodsImages.setGoodsId(goods.getGoodsId());
            }
            saveBatch(goods.getGoodsImages());
        }
    }
}
