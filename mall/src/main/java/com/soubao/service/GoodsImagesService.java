package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Goods;
import com.soubao.entity.GoodsImages;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-21
 */
public interface GoodsImagesService extends IService<GoodsImages> {
    /**
     * 更新商品相册
     * @param goods
     * @param goodsImagesList
     */
    void updateGoodsImage(Goods goods, List<GoodsImages> goodsImagesList);

    /**
     * 添加商品相册
     * @param goods
     * @param goodsImageList
     */
    void addGoodsImage(Goods goods, List<GoodsImages> goodsImageList);
}
