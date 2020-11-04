package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.*;
import com.soubao.vo.GoodsListFilter;
import com.soubao.vo.Stock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GoodsService extends IService<Goods> {

    //商品是否在地区配送
    boolean isNotShippingInRegion(Goods goods, Integer regionId);

    //总件数商品在地区配送的运费
    BigDecimal getFreightPriceInRegion(Goods goods, Integer regionId, Integer goodsNum);

    //商品列表页的导航栏链接接组
    Set<GoodsListFilter> getGoodsListFilterForMenu(String url);

    //商品列表页的排序链接组
    Map<String, Object> getGoodsListFilterForOrderBy(String url);

    //商品列表页的价格区间链接组
    List<GoodsListFilter> getGoodsListFilterForPrice(String url, GoodsCategory goodsCategory);

    //获取分销商未上架商品数量
    int getNotAddGoodsCount(User user);

    /**
     * 商品减库存
     */
    void stock(Goods goods);

    //获取商品缩略图
    String getGoodsThumbnail(Goods goods, Integer width, Integer height);

    /**
     * 获取商品活动
     *
     * @return
     */

    GoodsProm getGoodsProm(Integer promType, Integer promId, Long itemId);

    /**
     * 获取商品sku
     *
     * @param goodsId
     * @param itemId
     * @return
     */
    GoodsSku getGoodsSku(Integer goodsId, Integer itemId);

    /**
     * 删除商品
     *
     * @param storeId
     * @param goodsId
     */
    void deleteById(Integer storeId, Integer goodsId);

    //查询分销商微店分销商品分页
    IPage<Goods> pageMyShopGoods(Page<Goods> goodsPage, QueryWrapper<Goods> queryWrapper);

    //将商品恢复成普通商品
    void recoveryPromType(Integer goodsId, Integer itemId);

    /**
     * 获取店铺商品库存列表
     *
     * @param page
     * @param stockQueryWrapper
     * @return
     */
    IPage<Stock> getStockPage(Page<Stock> page, QueryWrapper<Stock> stockQueryWrapper);

    /**
     * 修改店铺商品库存
     *
     * @param stock
     */
    void updateStock(Stock stock);

    void withStore(List<Goods> records);

    void withSpecGoodsPrice(List<Goods> records);

    void recoveryPromTypes(Set<Integer> goodsIds, Set<Integer> itemIds, Set<Integer> specGoodsIds);

    //校验店铺可发布商品数
    void checkGoodsLimit(Integer gradeId, Integer storeId);

    //商品原始图处理
    void handleOriginalImg(Goods goods);

    IPage<Goods> changeSPageToIPage(org.springframework.data.domain.Page<Goods> goodsSPage);

    IPage<Goods> iPage(GoodsQueryWrapper goodsQueryWrapper, Page<Goods> page);

    void withGoodsCategory(List<Goods> records);

    void reSell(Integer goodsId, BigDecimal price);

    void downSellGood(Integer goodsId);
}
