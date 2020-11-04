package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.common.vo.SBApi;
import com.soubao.entity.*;
import com.soubao.entity.vo.GoodsVo;
import com.soubao.service.*;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.vo.Stock;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(value = "商品控制器", tags = {"商品相关接口"})
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    @Autowired
    private GoodsAttrService goodsAttrService;
    @Autowired
    private GoodsImagesService goodsImagesService;
    @Autowired
    private SpecGoodsPriceService specGoodsPriceService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @ApiOperation(value = "商品接口", notes = "获取单个商品记录", httpMethod = "GET")
    @GetMapping("goods/{id}")
    public Goods goods(@ApiParam("商品id") @PathVariable("id") Integer goodsId) {
        return goodsService.getById(goodsId);
    }

    @ApiOperation(value = "修改商品可卖状态接口", notes = "库存数和所属人", httpMethod = "GET")
    @GetMapping("sell-state/update")
    SBApi updateSellState(@RequestParam(value = "goodsId", required = true) Integer goodsId,
                          @RequestParam(value = "userId", required = false) Integer userId,
                          @RequestParam(value = "store_count", required = true) Integer store_count,
                          @RequestParam(value = "price", required = false) BigDecimal price) {
        Goods goods = goodsService.getById(goodsId);
        goods.setStoreCount(store_count);
        if (null != userId) {
            goods.setUserId(userId);
        }
        // 商品上架时间
        goods.setOnTime(System.currentTimeMillis() / 1000);
        if (store_count == 0)
            goods.setIsOnSale(0);
        else
            goods.setIsOnSale(1);

        if (null != price) {
            goods.setMarketPrice(price);
            goods.setShopPrice(price);
        }
        goodsService.updateById(goods);
        return (new SBApi());
    }


    @ApiOperation(value = "修改商品可卖状态接口", notes = "库存数和所属人", httpMethod = "GET")
    @GetMapping("sell-state/update/goods")
    SBApi updateSellStateGoods(@RequestParam(value = "goodsId") Integer goodsId,
                               @RequestParam(value = "userId", required = false) Integer userId,
                               @RequestParam(value = "store_id") Integer storeId,
                               @RequestParam(value = "store_count") Integer store_count,
                               @RequestParam(value = "price", required = false) BigDecimal price) {
        Goods goods = goodsService.getById(goodsId);
        goods.setStoreCount(store_count);
        if (store_count == 0) {
            goods.setIsOnSale(0);
        } else {
            goods.setIsOnSale(1);
        }

        if (null != price) {
            goods.setMarketPrice(price);
            goods.setShopPrice(price);
        }
        goods.setStoreId(storeId);
        goods.setSubscriptionPrice(price);
        goods.setSubscriptionTime(new Date());
        goods.setOwnerId(userId);
        goodsService.updateById(goods);
        return (new SBApi());
    }


    @ApiOperation(value = "商品sku")
    @GetMapping("goods/sku")
    public GoodsSku goodsSku(@ApiParam(value = "商品主键") @RequestParam(value = "id", required = false) Integer goodsId,
                             @ApiParam(value = "规格方案ID") @RequestParam(value = "item_id", required = false) Integer itemId,
                             @ApiParam(value = "是否拼团") @RequestParam(value = "is_team", defaultValue = "false") Boolean isTeam) {
        GoodsSku goodsSku = goodsService.getGoodsSku(goodsId, itemId);
        goodsSku.setIsTeam(isTeam);
        goodsSku.useGoodsProm();
        return goodsSku;
    }

    @ApiOperation(value = "商品列表分页", notes = "获取分页记录", httpMethod = "GET")
    @GetMapping("goods/page")
    public IPage<Goods> goodsPage(
            @ApiParam("全文检索搜索词") @RequestParam(value = "words", required = false) String words,//有值使用全文检索
            @ApiParam("商品分类id") @RequestParam(value = "cat_id", required = false) Integer categoryId,
            @ApiParam("商品一级分类id") @RequestParam(value = "cat_id1", required = false) Integer catId1,
            @ApiParam("商品品牌id组") @RequestParam(value = "brand_ids", required = false) Set<Integer> brandIds,
            @ApiParam("店铺id") @RequestParam(value = "store_id", required = false) Integer storeId,
            @ApiParam("指定活动的商品") @RequestParam(value = "prom_id", required = false) Integer promId,
            @ApiParam("指定活动类型的商品：1秒杀2团购3优惠促销4预售") @RequestParam(value = "prom_type", required = false) Integer promType,
            @ApiParam("起始价格") @RequestParam(value = "start_price", required = false) Integer startPrice,
            @ApiParam("截止价格") @RequestParam(value = "end_price", required = false) Integer endPrice,
            @ApiParam("属性筛选字符串") @RequestParam(value = "attr", required = false) String attrGroupStr,
            @ApiParam("推荐") @RequestParam(value = "is_recommend", required = false) Integer isRecommend,
            @ApiParam("热销") @RequestParam(value = "is_hot", required = false) Integer isHot,
            @ApiParam("新品") @RequestParam(value = "is_new", required = false) Integer isNew,
            @ApiParam("是否积分商品") @RequestParam(value = "is_exchange_integral", required = false) boolean isExchangeIntegral,
            @ApiParam("是否查询品牌商品") @RequestParam(value = "is_brand", required = false) boolean isBrand,
            @ApiParam("是否查询分销商品") @RequestParam(value = "is_distribution", required = false) Integer isDistribution,
            @ApiParam("相似商品名称") @RequestParam(value = "goods_name", required = false) String goodsName,
            @ApiParam("审核商品:默认通过") @RequestParam(value = "goods_state", defaultValue = "1") Set<Integer> goodsState,
            @ApiParam("上架商品:默认上架") @RequestParam(value = "is_on_sale", defaultValue = "1") Integer isOnSale,
            @ApiParam("排序字段") @RequestParam(value = "order_by", defaultValue = "sort") String orderBy,
            @ApiParam("排序方式") @RequestParam(value = "sort", defaultValue = "desc") String sort,
            @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
            @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "20") Integer size) {
        GoodsQueryWrapper goodsQueryWrapper = new GoodsQueryWrapper();
        if (isBrand && page <= 1) {//查询品牌商品
            //goodsQueryWrapper.isBrand().orderBy("sort", "desc").orderBy("goods_id", "desc");
        }
        if (categoryId != null) {
            goodsQueryWrapper.goodsCategory(goodsCategoryService.getById(categoryId));
        }
        goodsQueryWrapper.isOnSale(1);
        if (attrGroupStr != null) {
            Set<Integer> goodsIds = goodsAttrService.getGoodsIdsByAttr(attrGroupStr);
            if (goodsIds == null || goodsIds.isEmpty()) {
                return new Page<>();
            }
            goodsQueryWrapper.inGoodsIds(goodsIds);
        }

        goodsQueryWrapper.search(words).catId1(catId1).goodsState(goodsState).isOnSale(isOnSale).storeId(storeId).goodsName(goodsName)
                .brandIds(brandIds).startPrice(startPrice).endPrice(endPrice).isExchangeIntegral(isExchangeIntegral).isDistribut(isDistribution)
                .isRecommend(isRecommend).isHot(isHot).isNew(isNew).promId(promId).promType(promType);
        IPage<Goods> goodsIPage = goodsService.iPage(goodsQueryWrapper, new Page<>(page, size));
        goodsService.withStore(goodsIPage.getRecords());
        return goodsIPage;
    }


    @ApiOperation(value = "通证商品列表分页（商家端）", notes = "获取分页记录", httpMethod = "GET")
    @GetMapping("goods/page/depositCertificate/seller")
    public IPage<Goods> goodsPage(
            @ApiParam("店铺id") @RequestParam(value = "store_id", required = true) Integer storeId,
            @ApiParam("是否在卖") @RequestParam(value = "is_on_sale", required = false) Integer isOnSale,
            @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
            @ApiParam("二级分类") @RequestParam(value = "cat_id2", required = false) Integer cat_id2,
            @ApiParam("商品名称") @RequestParam(value = "good_name", required = false) String good_name,
            @ApiParam("排序字段") @RequestParam(value = "sort_type", defaultValue = "1") Integer sortType,
            @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "20") Integer size) {
        Store store = sellerService.getStoreById(storeId);
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("store_id", storeId);
        if (null != isOnSale)
            wrapper.eq("is_on_sale", isOnSale);
        if (sortType == 1)
            wrapper.orderByAsc("shop_price");
        else
            wrapper.orderByDesc("shop_price");

        wrapper.eq("cat_id1", 188);//只显示通证的
        wrapper.eq("user_id", store.getUserId());
        if (null != cat_id2)
            wrapper.eq("cat_id2", cat_id2);
        if (null != good_name)
            wrapper.like("goods_name", good_name);

        wrapper.eq("delete_flag", 0);

        //goodsService.list(wrapper);
        //Page<Goods> pageGoods = new Page<>(page, size);
        IPage<Goods> goodsIPage = goodsService.page(new Page<>(page, size), wrapper);
        goodsService.withGoodsCategory(goodsIPage.getRecords());
        goodsService.withStore(goodsIPage.getRecords());
        return goodsIPage;
    }


    /**
     * APP 端存证  只显示商家的商品
     *
     * @param words
     * @param storeId
     * @param isOnSale
     * @param page
     * @param cat_id2
     * @param sortType
     * @param size
     * @return
     */
    @ApiOperation(value = "通证商品列表分页", notes = "获取分页记录", httpMethod = "GET")
    @GetMapping("goods/page/depositCertificate")
    public IPage<Goods> goodsPage(
            @ApiParam("全文检索搜索词") @RequestParam(value = "words", required = false) String words,//有值使用全文检索
            @ApiParam("店铺id") @RequestParam(value = "store_id", required = false) Integer storeId,
            @ApiParam("是否在卖") @RequestParam(value = "is_on_sale", required = false) Integer isOnSale,
            @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
            @ApiParam("二级分类") @RequestParam(value = "cat_id2", required = true) Integer cat_id2,
            @ApiParam("排序字段") @RequestParam(value = "sort_type", defaultValue = "1") Integer sortType,
            @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "20") Integer size) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        if (!"anonymousUser".equals(principal)) {
            User user = authenticationFacade.getPrincipal(User.class);
            wrapper.ne("owner_id", user.getUserId());
        }
        if (null != words) {
            wrapper.like("goods_name", words);
        }
        if (null != storeId) {
            wrapper.eq("store_id", storeId);
        }
        if (null != isOnSale) {
            wrapper.eq("is_on_sale", isOnSale);
        }
        if (sortType == 1) {
            wrapper.orderByAsc("shop_price");
        } else {
            wrapper.orderByDesc("shop_price");
        }

        wrapper.eq("cat_id1", 188);//只显示通证的
        wrapper.eq("cat_id2", cat_id2);//

        wrapper.eq("delete_flag", 0);

        // 获取散户的商店list
        List<Store> storeList = sellerService.getUserSellerStoreList();
        if(!CollectionUtils.isEmpty(storeList)) {
            // 查询所有商家的商店
            wrapper.notIn("store_id", storeList.stream().map(Store::getStoreId).collect(Collectors.toList()));
        }
        IPage<Goods> goodsPage = goodsService.page(new Page<>(page, size), wrapper);
        goodsService.withGoodsCategory(goodsPage.getRecords());
        goodsService.withStore(goodsPage.getRecords());
        return goodsPage;
    }


    @ApiOperation(value = "总平台商品分页", notes = "获取总平台商品分页", httpMethod = "GET")
    @GetMapping("/goods/page/admin")
    public IPage<Goods> adminGoodsPage(
            @RequestParam(value = "cat_id", required = false) Integer catId,
            @RequestParam(value = "brand_id", required = false) Integer brandId,
            @RequestParam(value = "goods_state", required = false) Integer goodsState,
            @RequestParam(value = "is_on_sale", required = false) Integer isOnSale,
            @RequestParam(value = "goods_sn", required = false) String goodsSn,//商品名称
            @RequestParam(value = "goods_name", required = false) String goodsName,//商品货号
            @ApiParam("是否查询分销商品") @RequestParam(value = "is_distribution", required = false) Integer isDistribution,
            @RequestParam(value = "order_by", defaultValue = "goods_id") String orderBy,    //排序字段
            @RequestParam(value = "sort", defaultValue = "desc") String sort,   //排序方式
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        if (catId != null) {
            queryWrapper.and(wrapper -> wrapper.eq("cat_id1", catId).or().eq("cat_id2", catId).or().eq("cat_id3", catId));
        }
        if (brandId != null) {
            queryWrapper.eq("brand_id", brandId);
        }
        if (goodsState != null) {
            queryWrapper.eq("goods_state", goodsState).lt("is_on_sale", 2);
        }
        if (isOnSale != null) {
            queryWrapper.eq("is_on_sale", isOnSale);
        }
        if (goodsName != null) {
            queryWrapper.like("goods_name", goodsName);
        }
        if (goodsSn != null) {
            queryWrapper.eq("goods_sn", goodsSn);
        }
        if (isDistribution != null) {
            queryWrapper.gt("distribut", 0);
        }
        if (!StringUtils.isEmpty(orderBy)) {
            if (!StringUtils.isEmpty(sort) && sort.toLowerCase().equals("asc")) {
                queryWrapper.orderByAsc(orderBy);
            } else {
                queryWrapper.orderByDesc(orderBy);
            }
        }
        IPage<Goods> goodsIPage = goodsService.page(new Page<>(page, size), queryWrapper);
        goodsService.withGoodsCategory(goodsIPage.getRecords());
        goodsService.withStore(goodsIPage.getRecords());
        return goodsIPage;
    }

    @ApiOperation(value = "总平台分销商微店分销商品分页", notes = "获取总平台分销商微店分销商品分页", httpMethod = "GET")
    @GetMapping("admin/my_shop/goods/page")
    public IPage<Goods> pageMyShopGoods(@RequestParam("user_id") Integer userId,
                                        @RequestParam(value = "cat_id", required = false) Integer catId,
                                        @RequestParam(value = "brand_id", required = false) Integer brandId,
                                        @RequestParam(value = "goods_name_or_sn", required = false) String goodsNameOrSn,//商品名称或货号
                                        @RequestParam(value = "p", defaultValue = "1") Integer page,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ud.user_id", userId);
        queryWrapper.eq("g.is_on_sale", 1);
        if (catId != null) {
            queryWrapper.and(wrapper -> wrapper.eq("g.cat_id1", catId)
                    .or().eq("g.cat_id2", catId)
                    .or().eq("g.cat_id3", catId));
        }
        if (brandId != null) {
            queryWrapper.eq("g.brand_id", brandId);
        }
        if (StringUtils.isNotEmpty(goodsNameOrSn)) {
            queryWrapper.and(wrapper -> wrapper.like("g.goods_name", goodsNameOrSn)
                    .or().like("g.goods_sn", goodsNameOrSn));
        }
        queryWrapper.orderByDesc("ud.sales_num");
        return goodsService.pageMyShopGoods(new Page<>(page, size), queryWrapper);
    }

    @ApiOperation(value = "商品列表", notes = "获取商品列表", httpMethod = "GET")
    @GetMapping("goods/list")
    public List<Goods> goodsList(
            @ApiParam("商品名称") @RequestParam(value = "goods_name", required = false) String goodsName,
            @ApiParam("店铺id") @RequestParam(value = "store_id", required = false) Set<Integer> storeId,
            @ApiParam("审核商品:默认通过") @RequestParam(value = "goods_state", defaultValue = "1") Integer goodsState,
            @ApiParam("上架商品:默认上架") @RequestParam(value = "is_on_sale", defaultValue = "1") Integer isOnSale,
            @ApiParam("排序字段") @RequestParam(value = "order_by", defaultValue = "sort") String orderBy,
            @ApiParam("排序方式") @RequestParam(value = "asc", defaultValue = "true") Boolean asc,
            @ApiParam("条数") @RequestParam(value = "limit", required = false) Integer limit) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_state", goodsState).eq("is_on_sale", isOnSale);
        if (null != orderBy) {
            if (asc) {
                queryWrapper.orderByAsc(orderBy);
            } else {
                queryWrapper.orderByDesc(orderBy);
            }
        }
        if (null != limit) {
            queryWrapper.last("limit " + limit);
        }
        if (null != storeId && storeId.size() > 0) {
            queryWrapper.in("store_id", storeId);
        }
        if (null != goodsName) {
            queryWrapper.like("goods_name", goodsName);
        }
        return goodsService.list(queryWrapper);
    }

    @ApiOperation(value = "商品主键集合", notes = "获取商品主键集合", httpMethod = "GET")
    @GetMapping("/goods/ids")
    public Set<Integer> goodsList(
            @ApiParam("商品名称") @RequestParam(value = "goods_name", required = false) String goodsName,
            @ApiParam("商品分类id") @RequestParam(value = "cat_id", required = false) Integer catId,
            @ApiParam("商品品牌id") @RequestParam(value = "brand_id", required = false) Integer brandId,
            @ApiParam("店铺id") @RequestParam(value = "store_id", required = false) Set<Integer> storeId) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        if (null != storeId && storeId.size() > 0) {
            queryWrapper.in("store_id", storeId);
        }
        if (null != goodsName) {
            queryWrapper.like("goods_name", goodsName);
        }
        if (null != catId) {
            queryWrapper.and(i -> i.eq("cat_id1", catId).or().eq("cat_id2", catId).or().eq("cat_id3", catId));
        }
        if (null != brandId) {
            queryWrapper.eq("brand_id", brandId);
        }
        return goodsService.list(queryWrapper).stream().map(Goods::getGoodsId).collect(Collectors.toSet());
    }

    @ApiOperation(value = "商品店铺分组统计数", notes = "商品店铺分组统计数", httpMethod = "GET")
    @GetMapping("/stores/goods_count")
    public List<Goods> goodsList(
            @ApiParam("店铺id") @RequestParam(value = "store_id") Set<Integer> storeId) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("count( goods_id ) AS goodsCount,store_id");
        if (null != storeId && storeId.size() > 0) {
            queryWrapper.in("store_id", storeId);
        }
        queryWrapper.groupBy("store_id");
        return goodsService.list(queryWrapper);
    }

    @ApiOperation(value = "店铺商品统计", notes = "获取单个店铺商品统计", httpMethod = "GET")
    @GetMapping("/store/goods_counts")
    public Map<String, Integer> goodsCounts(@ApiParam("店铺主键") @RequestParam("store_id") Integer storeId) {
        Map<String, Integer> storeGoodsCounts = new HashMap<>();
        storeGoodsCounts.put("hot_goods_count", goodsService.count((new QueryWrapper<Goods>().
                eq("store_id", storeId).eq("is_hot", 1).eq("is_on_sale", 1))));
        storeGoodsCounts.put("new_goods_count", goodsService.count((new QueryWrapper<Goods>().
                eq("store_id", storeId).eq("is_new", 1).eq("is_on_sale", 1))));
        storeGoodsCounts.put("total_goods_count", goodsService.count((new QueryWrapper<Goods>().
                eq("store_id", storeId).eq("is_on_sale", 1))));
        return storeGoodsCounts;
    }

    @ApiOperation(value = "店铺商品总数", notes = "获取单个店铺总数", httpMethod = "GET")
    @GetMapping("/store/goods_count")
    public int goodsCount(@ApiParam("店铺主键") @RequestParam("store_id") Integer storeId) {
        return goodsService.count((new QueryWrapper<Goods>().
                eq("store_id", storeId)));
    }

    @ApiOperation(value = "商品查询构造器", notes = "获取商品查询构造器", httpMethod = "GET")
    @GetMapping("goods/filter_urls")
    public Map<String, Object> filterUrls(@ApiParam("构造url") @RequestParam(value = "filter_url", defaultValue = "") String filter_url) {
        GoodsCategory goodsCategory = goodsCategoryService.getGoodsCategoryByGoodsListFilterUrl(filter_url);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("filter_attr", goodsAttrService.getGoodsListFilterForAttr(filter_url, goodsCategory));
        resultMap.put("filter_brand", brandService.getGoodsListFilterForBrand(filter_url, goodsCategory));
        resultMap.put("filter_price", goodsService.getGoodsListFilterForPrice(filter_url, goodsCategory));
        resultMap.putAll(goodsService.getGoodsListFilterForOrderBy(filter_url));
        return resultMap;
    }

    @ApiOperation(value = "查询商品和商品规格分页")
    @GetMapping("goods/spec_goods_price/page")
    public IPage<Goods> getGoodsWithSpecGoodsPricePage(
            @RequestParam(value = "store_id", required = false) Integer storeId,
            @RequestParam(value = "keywords", required = false) String keywords,
            @RequestParam(value = "brand_id", required = false) Integer brandId,
            @RequestParam(value = "cat_id", required = false) Integer catId,
            @RequestParam(value = "is_new", required = false) Integer isNew,
            @RequestParam(value = "is_hot", required = false) Integer isHot,
            @RequestParam(value = "prom_id", required = false) Integer promId,
            @RequestParam(value = "prom_type", required = false) Integer promType,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_on_sale", 1).eq("is_virtual", 0)
                .orderByDesc("goods_id");
        if (storeId != null) {
            queryWrapper.eq("store_id", storeId);
        }
        if (catId != null) {
            queryWrapper.and(wrapper -> wrapper.eq("cat_id1", catId)
                    .or().eq("cat_id2", catId).or().eq("cat_id3", catId));
        }
        if (StringUtils.isNotEmpty(keywords)) {
            queryWrapper.and(wrapper -> wrapper.like("goods_name", keywords).or().like("keywords", keywords));
        }
        if (brandId != null) {
            queryWrapper.eq("brand_id", brandId);
        }
        if (isNew != null) {
            queryWrapper.eq("is_new", isNew);
        }
        if (isHot != null) {
            queryWrapper.eq("is_hot", isHot);
        }
        if (promType != null) {
            if (promType > 0) {
                if (promId != null) {
                    queryWrapper.and(wrapper -> wrapper.eq("prom_id", promId)
                            .eq("prom_type", promType).or().in("prom_type", 0, promType));
                } else {
                    queryWrapper.eq("prom_type", 0);
                }
            }
        }
        IPage<Goods> goodsIPage = goodsService.page(new Page<>(page, size), queryWrapper);
        goodsService.withSpecGoodsPrice(goodsIPage.getRecords());
        return goodsIPage;
    }

    @GetMapping("/goods/report")
    public Map<String, Object> getReport(
            @RequestParam(value = "store_id", required = false) Integer storeId) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);

        Map<String, Object> report = new HashMap<>();
        QueryWrapper<Goods> baseQueryWrapper = new QueryWrapper<>();
        if (null != storeId) {
            baseQueryWrapper.eq("store_id", storeId);
        }
        // 跟商家id获取全部已发布的存证
        List<DepositCertificate> certificate = sellerService.getDepositCertificateBySellerId(seller.getSellerId());
        report.put("total_count", certificate.size() );
        report.put("wait_auth_count", goodsService.count(baseQueryWrapper.clone().eq("goods_state", 0)
                .eq("is_on_sale", 0)));
        report.put("on_sale_count", goodsService.count(baseQueryWrapper.eq("is_on_sale", 1)));
        report.put("off_sale_count", goodsService.count(baseQueryWrapper.clone().eq("is_on_sale", 2)));
        return report;
    }

    @ApiOperation("获取店铺商品库存")
    @GetMapping("stock/page")
    public IPage<Stock> getStockPage(@ApiParam("店铺id") @RequestParam(value = "store_id", required = false) Integer storeId,
                                     @ApiParam("商品名称") @RequestParam(value = "goods_name", required = false) String goodsName,
                                     @ApiParam("规格名称") @RequestParam(value = "key_name", required = false) String keyName,
                                     @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
                                     @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Stock> stockQueryWrapper = new QueryWrapper<>();
        if (storeId != null) {
            stockQueryWrapper.eq("g.store_id", storeId);
        }
        if (StringUtils.isNotEmpty(goodsName)) {
            stockQueryWrapper.like("g.goods_name", goodsName);
        }
        if (StringUtils.isNotEmpty(keyName)) {
            stockQueryWrapper.like("s.key_name", keyName);
        }
        stockQueryWrapper.apply("1=1");
        return goodsService.getStockPage(new Page<>(page, size), stockQueryWrapper);
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @ApiOperation("修改店铺商品库存")
    @PutMapping("/stock")
    public SBApi updateStock(@RequestBody Stock stock) {
        goodsService.updateStock(stock);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @PostMapping("/goods")
    public SBApi addGoods(@Valid @RequestBody Goods goods) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        Store store = sellerService.getStoreById(seller.getStoreId());
        goods.setStoreId(seller.getStoreId());
        goods.setOnTime(System.currentTimeMillis() / 1000);
        if (store.getGoodsExamine() == 1) {
            goods.setGoodsState(0);
            goods.setIsOnSale(0);
        } else {
            goods.setGoodsState(1);
        }
        goods.setIsOwnShop(store.getIsOwnShop());
        //校验可发布商品数量
        goodsService.checkGoodsLimit(store.getGradeId(), store.getStoreId());
        goodsService.save(goods);
        goodsImagesService.addGoodsImage(goods, goods.getGoodsImages());
        specGoodsPriceService.addSpecGoodsPriceList(goods, goods.getSpecGoodsPriceList());
        goodsAttrService.addGoodsAttr(goods, goods.getGoodsAttrs());
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @PostMapping("/goods1")
    public Integer addGoods1(@Valid @RequestBody Goods goods) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        Store store = sellerService.getStoreById(seller.getStoreId());
        goods.setStoreId(seller.getStoreId());
        goods.setOnTime(System.currentTimeMillis() / 1000);
        if (store.getGoodsExamine() == 1) {
            goods.setGoodsState(0);
            goods.setIsOnSale(0);
        } else {
            goods.setGoodsState(1);
        }
        goods.setIsOwnShop(store.getIsOwnShop());
        //校验可发布商品数量
        goodsService.checkGoodsLimit(store.getGradeId(), store.getStoreId());
        goodsService.save(goods);
        goodsImagesService.addGoodsImage(goods, goods.getGoodsImages());
        specGoodsPriceService.addSpecGoodsPriceList(goods, goods.getSpecGoodsPriceList());
        goodsAttrService.addGoodsAttr(goods, goods.getGoodsAttrs());
        return goods.getGoodsId();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @PutMapping("/goods/all")
    public SBApi saveGoodsAll(@Valid @RequestBody Goods goods) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        goods.setStoreId(seller.getStoreId());
        goods.setOnTime(System.currentTimeMillis() / 1000);
        goodsService.handleOriginalImg(goods);
        goodsService.updateById(goods);
        goodsImagesService.updateGoodsImage(goods, goods.getGoodsImages());
        specGoodsPriceService.updateSpecGoodsPriceList(goods, goods.getSpecGoodsPriceList());
        goodsAttrService.updateGoodsAttr(goods, goods.getGoodsAttrs());
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER,ADMIN,USER')")
    @PutMapping("/goods")
    public SBApi saveGoods(@RequestBody Goods goods) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("goods_id", goods.getGoodsId());
        if (seller.getStoreId() != null) {
            updateWrapper.eq("store_id", seller.getStoreId());
        }
        goodsService.update(goods, updateWrapper);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @PutMapping("/goods/is_on_sale")
    public SBApi saveGoodsIsOnSale(@RequestBody Goods goods) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("goods_id", goods.getGoodsId());
        if (seller.getStoreId() != null) {
            updateWrapper.eq("store_id", seller.getStoreId());
        }
        if (goods.getIsOnSale() == 1) {
            Goods oldGoods = goodsService.getById(goods.getGoodsId());
            if (oldGoods.getGoodsState() != 1) {
                throw new ShopException(ResultEnum.GOODS_STATE_ERROR);
            }
        }
        goods.setOnTime(System.currentTimeMillis() / 1000);
        goodsService.update(goods, updateWrapper);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @DeleteMapping("/goods")
    public SBApi deleteGoods(@RequestParam(value = "goods_id") Integer goodsId) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        int orderGoodsCountGoods = orderService.getOrderGoodsCountGoods(goodsId);
        if (orderGoodsCountGoods > 0) {
            throw new ShopException(ResultEnum.DELETE_GOOD_HAVE_ORDER);
        }
        goodsService.deleteById(seller.getStoreId(), goodsId);
        orderService.deleteCommentsByGoodsId(goodsId);
        return new SBApi();
    }

    @PutMapping("goods/list")
    public SBApi updateGoods(@RequestBody List<Goods> goodsList, SBApi sbApi) {
        goodsService.updateBatchById(goodsList);
        return sbApi;
    }

    @ApiOperation(value = "获取商品运费")
    @GetMapping("goods/freight")
    public BigDecimal dispatching(@ApiParam("商品id") @RequestParam("goods_id") Integer goodsId,
                                  @ApiParam("商品件数") @RequestParam(value = "goods_num", defaultValue = "1") Integer goodsNum,
                                  @ApiParam("地区id") @RequestParam("region_id") Integer regionId) {
        Goods goods = goodsService.getById(goodsId);
        if (goodsService.isNotShippingInRegion(goods, regionId)) {
            throw new ShopException(ResultEnum.GOODS_IS_NOT_SHIPPING);
        }
        return goodsService.getFreightPriceInRegion(goods, regionId, goodsNum);
    }

    @ApiOperation(value = "商品主图", notes = "获取商品主图")
    @GetMapping("goods/thumb_image")
    public void goodsThumbnail(@ApiParam("商品id") @RequestParam("goods_id") Integer goodsId,
                               @ApiParam("商品图片宽度") @RequestParam(value = "width", required = false) Integer width,
                               @ApiParam("商品图片高度") @RequestParam(value = "height", required = false) Integer height,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        Goods goods = goodsService.getOne(new QueryWrapper<Goods>().select("goods_id,original_img").eq("goods_id", goodsId));

//        String imageUrl = goodsService.getGoodsThumbnail(goods, width, height);
        if (goods.getOriginalImg().contains("http")) {
            response.sendRedirect(goods.getOriginalImg());
        } else {
            try {
                request.getRequestDispatcher(goods.getOriginalImg()).forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据dc_id ,store_id 获取商家多次转卖的商品
     *
     * @param collect
     * @param storeId
     * @return
     */
    @GetMapping("/goods/dc_id")
    List<Goods> getGoodsByDcid(@RequestParam("dc_ids") List<Long> collect,
                               @RequestParam("store_id") Integer storeId) {
        QueryWrapper<Goods> wrapper = new QueryWrapper<Goods>()
                .in("dc_id", collect)
                .ne("store_id", storeId);

        return goodsService.list(wrapper);

    }


    /**
     * 客户端APP再次出售
     * 修改商品价格，修改商品为上架
     *
     * @param goodsId
     * @param price
     * @return SBApi
     */
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("goods/re-sell")
    public SBApi reSellGood(@ApiParam("商品id") @RequestParam("goods_id") Integer goodsId,
                            @ApiParam("价格") @RequestParam("price") BigDecimal price) {
        goodsService.reSell(goodsId, price);
        return new SBApi();
    }

    /**
     * app 端下架
     *
     * @param goodsId
     * @return
     */
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("goods/down-sell")
    public SBApi downSellGood(@ApiParam("商品id") @RequestParam("goods_id") Integer goodsId) {
        goodsService.downSellGood(goodsId);
        return new SBApi();
    }


    /**
     * 获取商家发布的商品 提货的商品
     *
     * @param userId
     * @return
     */
    @GetMapping("/goods/seller")
    List<Goods> getgoodsBySellerUserId(@RequestParam("user_id") Integer userId) {
        return goodsService.list(new QueryWrapper<Goods>().eq("user_id", userId).eq("delete_flag", 1));
    }

    /**
     * 根据存证id获取商品信息
     * @param depositCertificateId
     * @return
     */
    @GetMapping("/seller/goods/dc_id")
    Goods goodsByDcId(@RequestParam("dc_id") Integer depositCertificateId) {
        return goodsService.getOne(new QueryWrapper<Goods>().eq("dc_id",depositCertificateId));
    }


    /**
     * 可提货/出售 :商品下架状态
     * <p> 0下架1上架2违规下架
     * 出售中：商品上架状态
     *
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("用户存证中心分页(可提货/出售 、 出售中)")
    @GetMapping("/user/depositCertificate/page")
    @ApiImplicitParams(@ApiImplicitParam(value = "is_on_sale", name = "上架状态：0下架1上架2违规下架"))
    public Page<Goods> getUserDepositCertificatePage(@ApiParam("上架状态：0下架1上架2违规下架") @RequestParam(value = "is_on_sale") Integer isOnSale,
                                                     @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
                                                     @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "15") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        Store store = sellerService.getUserSellerStore(user.getUserId());
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        if (null == store) {
            queryWrapper.eq("store_id", 0);
        }else {
            queryWrapper.eq("store_id", store.getStoreId());
        }
        queryWrapper.eq("delete_flag", 0);
        queryWrapper.eq("is_on_sale", isOnSale);
        queryWrapper.orderByDesc("goods_id");

        return goodsService.page(new Page<>(page, size), queryWrapper);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("用户存证中心(可提货/出售 、 出售中)详情")
    @GetMapping("/user/depositCertificate/detail")
    public GoodsVo getUserDepositCertificateDetail(@ApiParam("商品id") @RequestParam("goods_id") Integer goodsId) {
        User user = authenticationFacade.getPrincipal(User.class);
        // 根据 goodsId 获取商品详情
        Goods goods = goodsService.getById(goodsId);

        GoodsVo goodsVo = new GoodsVo();
        BeanUtils.copyProperties(goods, goodsVo);
        // 获取登陆者最后一次购买该商品的订单信息
        Order order = orderService.getOrderAndStoreByUserIds(user.getUserId(), goodsId);
        Store store = sellerService.getStoreById(order.getStoreId());
        order.setStore(store);
        goodsVo.setOrder(order);
        return goodsVo;
    }

    /**
     * 根据商店ID获取上架商品
     * @param storeId
     * @return
     */
    @GetMapping("/goods/storeId")
    List<Goods> getGoodsByStoreId(@RequestParam("store_id") Integer storeId ){
       return goodsService.list(new QueryWrapper<Goods>().eq("store_id",storeId).eq("is_on_sale",1).eq("delete_flag",0));
    }

}
