package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.common.constant.ShopConstant;
import com.soubao.dao.GoodsElasticsearchMapper;
import com.soubao.dao.GoodsMapper;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.*;
import com.soubao.common.utils.GoodsImageUtils;
import com.soubao.vo.GoodsListFilter;
import com.soubao.vo.Stock;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.index.IndexNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service("goodsService")
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    @Autowired
    private GoodsElasticsearchMapper goodsElasticsearchMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private SpecGoodsPriceService specGoodsPriceService;
    @Autowired
    private FreightConfigService freightConfigService;
    @Autowired
    private FreightTemplateService freightTemplateService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private GoodsAttributeService goodsAttributeService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private PreSellService preSellService;
    @Autowired
    private TeamActivityService teamActivityService;
    @Autowired
    private TeamGoodsItemService teamGoodsItemService;
    @Autowired
    private ShopConstant shopConstant;
    @Autowired
    private CartService cartService;
    @Autowired
    private GoodsImagesService goodsImagesService;
    @Autowired
    private GoodsConsultService goodsConsultService;
    @Autowired
    private SpecImageService specImageService;
    @Autowired
    private GoodsAttrService goodsAttrService;
    @Autowired
    private GoodsCollectService goodsCollectService;
    @Autowired
    private SellerService sellerService;
    @Value("${spring.resources.static-locations}")
    private String staticLocation;

    @Override
    public boolean isNotShippingInRegion(Goods goods, Integer regionId) {
        if (regionId == 0) {
            return true;
        }
        if (goods.getIsFreeShipping().equals(1)) {
            return false;
        }
        return freightConfigService.getOneByGoodsAndRegion(goods, regionId) == null;
    }

    @Override
    public BigDecimal getFreightPriceInRegion(Goods goods, Integer regionId, Integer goodsNum) {
        BigDecimal freightPrice = BigDecimal.valueOf(0);
        if (goods.getIsFreeShipping().equals(1)) {
            return freightPrice;
        }
        FreightTemplate freightTemplate = freightTemplateService.getById(goods.getTemplateId());
        BigDecimal totalUnit = BigDecimal.valueOf(goodsNum);//默认按件数
        switch (freightTemplate.getType()) {
            case 1:
                //按重量
                totalUnit = BigDecimal.valueOf(goods.getWeight()).multiply(BigDecimal.valueOf(goodsNum));//总重量
                break;
            case 2:
                //按体积
                totalUnit = BigDecimal.valueOf(goods.getVolume()).multiply(BigDecimal.valueOf(goodsNum));//总体积
                break;
        }
        FreightConfig freightConfig = freightConfigService.getOneByGoodsAndRegion(goods, regionId);
        if (freightConfig == null) {
            throw new ShopException(ResultEnum.NO_CONFIG_FREIGHT);
        }
        if ((totalUnit.compareTo(BigDecimal.valueOf(freightConfig.getFirstUnit())) > 0)
                && (freightConfig.getContinueUnit() > 0)) {
            BigDecimal average = (totalUnit.subtract(BigDecimal.valueOf(freightConfig.getFirstUnit()))).divide(BigDecimal.valueOf(freightConfig.getContinueUnit()));
            freightPrice = freightConfig.getFirstMoney().add(freightConfig.getFirstMoney().multiply(average));
        } else {
            freightPrice = freightConfig.getFirstMoney();
        }
        return freightPrice;
    }

    @Override
    public Set<GoodsListFilter> getGoodsListFilterForMenu(String url) {
        UriComponentsBuilder parentSb = ServletUriComponentsBuilder.fromUriString(url);
        Set<GoodsListFilter> goodsListFilterList = new HashSet<>();
        //品牌
        List<String> brandQueryList = parentSb.build().getQueryParams().get("brand_ids");
        if (brandQueryList != null && brandQueryList.get(0).length() > 0) {
            List<Brand> brandList = brandService.list((new QueryWrapper<Brand>()).inSql("id", brandQueryList.get(0)));
            StringBuilder brandName = new StringBuilder();
            for (Brand brand : brandList) {
                brandName.append(brand.getName());
            }
            String filterUrl = parentSb.replaceQueryParam("brand_ids", "").build().getQuery();
            goodsListFilterList.add(new GoodsListFilter("品牌:" + brandName.toString(), filterUrl));
        }

        //属性
        List<String> attrQueryList = parentSb.build().getQueryParams().get("attr");
        if (attrQueryList != null && attrQueryList.get(0).length() > 0) {
            String[] attrArr = attrQueryList.get(0).split("@");
            Set<Integer> attrIdList = new HashSet<>();
            for (String attrStr : attrArr) {
                attrIdList.add(Integer.valueOf(attrStr.split("_")[0]));
            }
            List<GoodsAttribute> goodsAttributeList = goodsAttributeService.list((new QueryWrapper<GoodsAttribute>()).
                    select("attr_id", "attr_name", "attr_values").in("attr_id", attrIdList));
            for (GoodsAttribute goodsAttribute : goodsAttributeList) {
                String attrValStr = goodsAttribute.getAttrId() + "_" + goodsAttribute.getAttrValues();
                Set<String> attrQuerySet = new HashSet(Arrays.asList(attrArr));
                attrQuerySet.remove(attrValStr);
                String attrQueryStr = String.join("@", attrQuerySet);
                goodsListFilterList.add(new GoodsListFilter(goodsAttribute.getAttrName() + ":" + goodsAttribute.getAttrValues()
                        , parentSb.replaceQueryParam("attr", attrQueryStr).build().getQuery()));
            }
        }

        //价格
        List<String> startPriceQueryList = parentSb.build().getQueryParams().get("start_price");
        List<String> endPriceQueryList = parentSb.build().getQueryParams().get("end_price");
        if (startPriceQueryList != null && startPriceQueryList.get(0).length() > 0
                && endPriceQueryList != null && endPriceQueryList.get(0).length() > 0) {
            String filterUrl = parentSb.replaceQueryParam("start_price", "")
                    .replaceQueryParam("end_price", "").build().getQuery();
            goodsListFilterList.add(new GoodsListFilter("价格:" + startPriceQueryList.get(0) + "-" + endPriceQueryList.get(0), filterUrl));
        }
        return goodsListFilterList;
    }

    @Override
    public Map<String, Object> getGoodsListFilterForOrderBy(String url) {
        UriComponentsBuilder sb = ServletUriComponentsBuilder.fromUriString(url);
        List<String> sortQueryList = sb.build().getQueryParams().get("sort");
        String sort = "desc";
        if (sortQueryList != null && sortQueryList.get(0).length() > 0) {
            if (sortQueryList.get(0).toLowerCase().equals(sort)) {
                sort = "asc";
            } else {
                sort = "desc";
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("orderby_default", sb.replaceQueryParam("order_by", "")
                .replaceQueryParam("sort", "").build().getQuery());
        resultMap.put("orderby_sales_sum", sb.replaceQueryParam("order_by", "sales_sum")
                .replaceQueryParam("sort", "desc").build().getQuery());
        resultMap.put("orderby_price", sb.replaceQueryParam("order_by", "shop_price")
                .replaceQueryParam("sort", sort).build().getQuery());
        resultMap.put("orderby_comment_count", sb.replaceQueryParam("order_by", "comment_count")
                .replaceQueryParam("sort", "desc").build().getQuery());
        resultMap.put("orderby_is_new", sb.replaceQueryParam("order_by", "is_new")
                .replaceQueryParam("sort", "desc").build().getQuery());
        return resultMap;
    }

    @Override
    public List<GoodsListFilter> getGoodsListFilterForPrice(String url, GoodsCategory goodsCategory) {
        List<GoodsListFilter> goodsListFilters = new ArrayList<>();
        UriComponentsBuilder parentSb = ServletUriComponentsBuilder.fromUriString(url);
//        List<String> startPriceQueryList = parentSb.build().getQueryParams().get("start_price");
//        List<String> endPriceQueryList = parentSb.build().getQueryParams().get("end_price");
//        if ((startPriceQueryList != null && startPriceQueryList.get(0).length() > 0)
//                || (endPriceQueryList != null && endPriceQueryList.get(0).length() > 0)) {// 此判断异常 当传入了价格区间时返回空集
//            return goodsListFilters;
//        }
        QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
        goodsQueryWrapper.select("min(shop_price) as min_shop_price, max(shop_price) as max_shop_price")
                .eq("is_on_sale", 1).eq("goods_state", 1);//基础筛选
        List<String> goodsNameQueryList = parentSb.build().getQueryParams().get("goods_name");
        if (goodsNameQueryList != null && goodsNameQueryList.get(0).length() > 0) {
            goodsQueryWrapper.like("goods_name", goodsNameQueryList.get(0));
        }
        List<String> brandIdList = parentSb.build().getQueryParams().get("brand_ids");
        if (brandIdList != null && brandIdList.get(0).length() > 0) {
            goodsQueryWrapper.eq("brand_id", brandIdList.get(0));
        }
        if (goodsCategory != null) {
            goodsQueryWrapper.eq("cat_id" + goodsCategory.getLevel(), goodsCategory.getId());//商品分类筛选
        }
        Goods goods = this.getOne(goodsQueryWrapper);
        if (goods != null) {
            MathContext mc = new MathContext(2, RoundingMode.UP);
            int space = 5;
            BigDecimal spaceDecimal = new BigDecimal(space);
            BigDecimal maxShopPrice = goods.getMaxShopPrice().setScale(0, BigDecimal.ROUND_UP);
            BigDecimal minShopPrice = goods.getMinShopPrice().setScale(0, BigDecimal.ROUND_DOWN);
            BigDecimal spaceNum = maxShopPrice.subtract(minShopPrice).divide(spaceDecimal, mc).setScale(0, BigDecimal.ROUND_UP);
            BigDecimal startPrice = minShopPrice.add(spaceNum);
            goodsListFilters.add(new GoodsListFilter("￥0-" + startPrice, parentSb.replaceQueryParam("start_price", 0).replaceQueryParam("end_price", startPrice).build().getQuery()));
            for (int i = 0; i < (space - 1); i++) {
                goodsListFilters.add(new GoodsListFilter("￥" + startPrice + "-" + startPrice.add(spaceNum),
                        parentSb.replaceQueryParam("start_price", startPrice)
                                .replaceQueryParam("end_price", startPrice.add(spaceNum)).build().getQuery()));
                startPrice = startPrice.add(spaceNum);
            }
        }
        return goodsListFilters;
    }

    @Override
    public int getNotAddGoodsCount(User user) {
        //非分销商
        if (user.getIsDistribut() != 1) {
            throw new ShopException(ResultEnum.NOT_DISTRIBUTORS);
        }
        return goodsMapper.selectNotAddGoodsCount(user.getUserId());
    }

    @Override
    public void stock(Goods goods) {
        goodsMapper.stock(goods);
    }

    @Override
    public String getGoodsThumbnail(Goods goods, Integer width, Integer height) {
        if (goods == null || StringUtils.isEmpty(goods.getOriginalImg())) {
            return shopConstant.getGoodsNoOriginalImage();
        }
        String originalImg = goods.getOriginalImg();
        if (originalImg.indexOf("http") == 0) {
            //if (!originalImg.contains("aliyuncs.com")) //
            /* 图片缩放
                lfit：等比缩放，限制在指定w与h的矩形内的最大图片。
                mfit：等比缩放，延伸出指定w与h的矩形框外的最小图片。
                fill：固定宽高，将延伸出指定w与h的矩形框外的最小图片进行居中裁剪。
                pad：固定宽高，缩放填充。
                fixed：固定宽高，强制缩放。 */
            String mValue = "pad";
            boolean isGif = originalImg.substring(originalImg.lastIndexOf(".")).lastIndexOf(".gif") >= 0;
            if (isGif) {
                //GIF格式的图片支持指定宽高缩放，不支持等比缩放（等比缩放情况下，动态图会变成静态图）
                mValue = "pad";
            }
            originalImg += "?x-oss-process=image/resize,m_" + mValue + ",h_" + height + ",w_" + width;
            Map<Object, Object> configMap = configService.getConfigMap();
            String markWidth = configMap.get("water_mark_width").toString();
            String markHeight = configMap.get("water_mark_height").toString();
            String safeUrlBase64;
            if ("1".equals(configMap.get("water_is_mark"))
                    && !isGif//gif不加水印
                    && width > Integer.parseInt(StringUtils.isEmpty(markWidth) ? "0" : markWidth)
                    && height > Integer.parseInt(StringUtils.isEmpty(markHeight) ? "0" : markHeight)) {
                if ("img".equals(configMap.get("water_mark_type"))) {
                    String markImg = configMap.get("water_mark_img").toString();
                    if (markImg.indexOf("http") == 0 && markImg.contains("aliyuncs.com")) {
                        //给oss链接图片加上oss图片水印
                        String path = "";   //oss图片相对路径，用于base64编码后加在oss图片链接参数上以添加水印
                        try {
                            path = new URL(markImg).getPath().substring(1);   //getFile(); upload/1591758071382.gif
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        safeUrlBase64 = Base64.getUrlEncoder().encodeToString(path.getBytes(StandardCharsets.UTF_8));
                        originalImg += ",image/watermark,image_"
                                + safeUrlBase64 + ",t_"
                                + configMap.get("water_mark_degree") + ",g_"
                                + GoodsImageUtils.getWaterPosition(configMap.get("water_sel").toString());
                    }
                } else {
                    //文字水印
                    String color = configMap.get("water_mark_txt_color").toString();
                    if (StringUtils.isEmpty(color) || !Pattern.matches("#[0-9a-fA-F]{6}", color)) {
                        color = "000000";
                    } else {
                        color = color.substring(1);
                    }
                    String ossFontType = "ZmFuZ3poZW5naGVpdGk";//阿里云URL安全Base64编码后的字体类型值
                    safeUrlBase64 = Base64.getUrlEncoder()
                            .encodeToString(configMap.get("water_mark_txt").toString().getBytes(StandardCharsets.UTF_8));
                    originalImg += ",image/watermark,type_" + ossFontType + ",size_" + configMap.get("water_mark_txt_size")
                            + ",text_" + safeUrlBase64 + ",color_" + color + ",t_" + configMap.get("water_mark_degree")
                            + ",g_" + GoodsImageUtils.getWaterPosition((String) configMap.get("water_sel"));
                }
            }
            return originalImg;
        }

        //商品原始图不存在直接返回默认图片
        File originalImgFile = new File("../" + originalImg);
        if (originalImgFile.exists()) {
//            originalImgFile.
        } else {
            return shopConstant.getGoodsNoOriginalImage();
        }


        //获取上传目录
        String staticPath = staticLocation.substring(staticLocation.indexOf(":") + 1);
        StringBuilder uploadPath = new StringBuilder(staticPath).append(shopConstant.getImageUpload());
        Integer goodsId = goods.getGoodsId();
        StringBuilder goodsThumbName = new StringBuilder("/goods/thumb/").append(goodsId).append("/goods_thumb_")
                .append(goodsId).append("_").append(width).append("_").append(height);

        //判断缩略图是否存在，存在则直接返回
        String[] suffixArr = {".jpg", ".jpeg", ".gif", ".png"};
        for (String suffix : suffixArr) {
            String thumbPath = uploadPath.toString() + goodsThumbName.toString() + suffix;
            if (new File(thumbPath).exists()) {
                return thumbPath;
            }
        }

        //生成缩略图
        //商品缩略图文件夹不存在则创建文件夹
        File file = new File(uploadPath.toString() + "/goods/thumb/" + goodsId);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new ShopException(ResultEnum.MKDIR_ERROR);
            }
        }

        String thumbPath = "";
        try {
            thumbPath = file.getAbsolutePath() + "/goods_thumb_" + goodsId + "_" + width + "_" + height
                    + originalImg.substring(originalImg.lastIndexOf("."));
            Thumbnails.of("../" + originalImg)
                    .size(width, height)
                    .toFile(thumbPath);
        } catch (IOException e) {
            log.error("商品缩略图获取失败,失败原因：" + e.getMessage());
        }
        //文件上传目录右斜杠替换为左斜杠以匹配文件缩略图目录
        String tempUpPath = uploadPath.substring(uploadPath.indexOf("/"));
        log.info("thumbPath:{}", thumbPath);
        log.info("tempUpPath:{}", tempUpPath);
        return thumbPath.substring(thumbPath.lastIndexOf(tempUpPath)).replace("\\", "/");
    }

    @Override
    public void handleOriginalImg(Goods goods) {
        Goods oldGoods = this.getById(goods.getGoodsId());
        if (!oldGoods.getOriginalImg().equals(goods.getOriginalImg())) {
            //更改了商品原始图
            if (oldGoods.getOriginalImg().indexOf("http") != 0) {
                //旧原始图非oss图，删除商品旧原始图与缩略图
                File oldFile = new File("../" + oldGoods.getOriginalImg());
                if (oldFile.exists()) {
                    FileUtils.deleteQuietly(oldFile);
                }
                String thumbPath = staticLocation.substring(staticLocation.indexOf(":") + 1) +
                        shopConstant.getImageUpload() +
                        "/goods/thumb/" + goods.getGoodsId();
                File file = new File(thumbPath);
                if (file.exists()) {
                    try {
                        FileUtils.deleteDirectory(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public IPage<Goods> changeSPageToIPage(org.springframework.data.domain.Page<Goods> goodsSPage) {
        IPage<Goods> goodsIPage = new Page<>();
        goodsIPage.setRecords(goodsSPage.getContent());
        goodsIPage.setCurrent(goodsSPage.getPageable().getPageNumber());
        goodsIPage.setPages(goodsSPage.getTotalPages());
        goodsIPage.setSize(goodsSPage.getPageable().getPageSize());
        goodsIPage.setTotal(goodsSPage.getTotalElements());
        return goodsIPage;
    }

    @Override
    public IPage<Goods> iPage(GoodsQueryWrapper goodsQueryWrapper, Page<Goods> page) {
        if (goodsQueryWrapper.isElasticsearch()) {
            try {
                SearchQuery query = goodsQueryWrapper.getNativeSearchQueryBuilder().withPageable(PageRequest.of((int) page.getCurrent(), (int) page.getSize())).build();
                log.info("Elasticsearch查询语句：{}", query.getQuery());
                org.springframework.data.domain.Page<Goods> goodsElasticsearchPage = goodsElasticsearchMapper.search(query);
                log.info("Elasticsearch查询返回size：{}", goodsElasticsearchPage.getSize());
                log.info("Elasticsearch查询返回TotalElements：{}", goodsElasticsearchPage.getTotalElements());
                log.info("Elasticsearch查询返回TotalPages：{}", goodsElasticsearchPage.getTotalPages());
                log.info("Elasticsearch查询返回Pageable：{}", goodsElasticsearchPage.getPageable());
                return changeSPageToIPage(goodsElasticsearchPage);
            } catch (IndexNotFoundException e) {
                log.error("Elasticsearch没有商品索引");
                return page(page, goodsQueryWrapper.getGoodsQueryWrapper());
            } catch (NoNodeAvailableException e) {
                log.error("Elasticsearch服务没有链接上");
                return page(page, goodsQueryWrapper.getGoodsQueryWrapper());
            }
        } else {
            return page(page, goodsQueryWrapper.getGoodsQueryWrapper());
        }
    }

    @Override
    public GoodsProm getGoodsProm(Integer promType, Integer promId, Long itemId) {
        GoodsProm goodsProm = null;
        if (promId > 0) {
            switch (promType) {
                case 4:
                    goodsProm = preSellService.getById(promId);
                    break;
                case 6:
                    TeamActivity teamActivity = teamActivityService.getById(promId);
                    TeamGoodsItem teamGoodsItem = teamGoodsItemService.getOne((new QueryWrapper<TeamGoodsItem>())
                            .eq("team_id", teamActivity.getTeamId()).eq("item_id", itemId));
                    teamActivity.setTeamPrice(teamGoodsItem.getTeamPrice());
                    goodsProm = teamActivity;
            }
        }
        return goodsProm;
    }

    @Override
    public GoodsSku getGoodsSku(Integer goodsId, Integer itemId) {
        Goods goods = this.getById(goodsId);
        GoodsSku goodsSku = new GoodsSku(goods);
        if (itemId != null && itemId > 0) {
            goodsSku.setSpecGoodsPrice(specGoodsPriceService.getById(itemId));
        }
        return goodsSku;
    }

    @Override
    @Transactional
    public void deleteById(Integer storeId, Integer goodsId) {
        this.remove((new QueryWrapper<Goods>()).eq("store_id", storeId).eq("goods_id", goodsId));
        cartService.remove((new QueryWrapper<Cart>()).eq("store_id", storeId).eq("goods_id", goodsId));
        goodsImagesService.remove((new QueryWrapper<GoodsImages>()).eq("goods_id", goodsId));
        goodsConsultService.remove((new QueryWrapper<GoodsConsult>()).eq("goods_id", goodsId));
        specGoodsPriceService.remove((new QueryWrapper<SpecGoodsPrice>()).eq("goods_id", goodsId));
        specImageService.remove((new QueryWrapper<SpecImage>()).eq("goods_id", goodsId));
        goodsAttrService.remove((new QueryWrapper<GoodsAttr>()).eq("goods_id", goodsId));
        goodsCollectService.remove((new QueryWrapper<GoodsCollect>()).eq("goods_id", goodsId));
    }

    @Override
    public void recoveryPromType(Integer goodsId, Integer itemId) {
        if (itemId != null && itemId != 0) {
            int count = specGoodsPriceService.count(new QueryWrapper<SpecGoodsPrice>()
                    .eq("goods_id", goodsId).ne("item_id", itemId).gt("prom_type", 0));
            specGoodsPriceService.update(new UpdateWrapper<SpecGoodsPrice>()
                    .set("prom_id", 0).set("prom_type", 0).eq("item_id", itemId));
            if (count == 0) {//该商品下的规格都没有活动
                this.update(new UpdateWrapper<Goods>().set("prom_id", 0).set("prom_type", 0)
                        .eq("goods_id", goodsId));
            }
        } else {
            this.update(new UpdateWrapper<Goods>().set("prom_id", 0).set("prom_type", 0)
                    .eq("goods_id", goodsId));
        }
    }

    @Override
    public void recoveryPromTypes(Set<Integer> goodsIds, Set<Integer> itemIds, Set<Integer> specGoodsIds) {
        if (itemIds.size() > 0) {
            //商品下是否有规格参与活动
            Set<Integer> iGoodsIds = specGoodsPriceService.list(new QueryWrapper<SpecGoodsPrice>()
                    .select("goods_id")
                    .gt("prom_type", 0)
                    .in("goods_id", specGoodsIds)
                    .notIn("item_id", itemIds)
                    .groupBy("goods_id"))
                    .stream()
                    .map(SpecGoodsPrice::getGoodsId)
                    .collect(Collectors.toSet());
            if (iGoodsIds.size() > 0) {
                Set<Integer> gIds = specGoodsIds.stream().filter(sid -> !iGoodsIds.contains(sid)).collect(Collectors.toSet());
                if (gIds.size() > 0) {
                    goodsIds.addAll(gIds);
                }
            } else {
                goodsIds.addAll(specGoodsIds);
            }
            specGoodsPriceService.update(new UpdateWrapper<SpecGoodsPrice>()
                    .set("prom_id", 0)
                    .set("prom_type", 0).in("item_id", itemIds));
        }
        if (goodsIds.size() > 0) {
            update(new UpdateWrapper<Goods>()
                    .set("prom_id", 0)
                    .set("prom_type", 0)
                    .in("goods_id", goodsIds));
        }
    }

    @Override
    public void checkGoodsLimit(Integer gradeId, Integer storeId) {
        int currGoodsCount = this.count(new QueryWrapper<Goods>().eq("store_id", storeId));
        StoreGrade storeGrade = sellerService.getStoreGrade(gradeId);
        if (storeGrade == null) {
            throw new ShopException(ResultEnum.STORE_GRADE_NOT_EXISTS);
        } else if (storeGrade.getSgGoodsLimit() != 0 && currGoodsCount >= storeGrade.getSgGoodsLimit()) {
            throw new ShopException(ResultEnum.RELEASE_GOODS_COUNT_MAX);
        }
    }

    @Override
    public IPage<Stock> getStockPage(Page<Stock> page, QueryWrapper<Stock> stockQueryWrapper) {
        return goodsMapper.selectStockPage(page, stockQueryWrapper);
    }

    @Override
    public void updateStock(Stock stock) {
        if (stock.getItemId() == null) {
            this.update(new UpdateWrapper<Goods>()
                    .set("store_count", stock.getStoreCount())
                    .eq("goods_id", stock.getGoodsId()));
        } else {
            specGoodsPriceService.update(new UpdateWrapper<SpecGoodsPrice>()
                    .set("store_count", stock.getStoreCount())
                    .eq("item_id", stock.getItemId()));
        }
    }

    @Override
    public void withStore(List<Goods> records) {
        if (!records.isEmpty()) {
            Set<Integer> storeIds = records.stream().map(Goods::getStoreId).collect(Collectors.toSet());
            Map<Integer, Store> storeMap = sellerService.getStoreListByIds(storeIds).stream().collect(Collectors.toMap(Store::getStoreId, store -> store));
            for (Goods goods : records) {
                if (storeMap.containsKey(goods.getStoreId())) {
                    goods.setStoreName(storeMap.get(goods.getStoreId()).getStoreName());
                }
            }
        }
    }

    @Override
    public void withSpecGoodsPrice(List<Goods> records) {
        if (!records.isEmpty()) {
            Set<Integer> goodsIds = records.stream().map(Goods::getGoodsId).collect(Collectors.toSet());
            Map<Integer, List<SpecGoodsPrice>> specGoodsPriceMap = specGoodsPriceService
                    .list(new QueryWrapper<SpecGoodsPrice>().in("goods_id", goodsIds))
                    .stream().collect(Collectors.groupingBy(SpecGoodsPrice::getGoodsId));
            records.forEach(goods -> {
                List<SpecGoodsPrice> specGoodsPrices = specGoodsPriceMap.get(goods.getGoodsId());
                if (specGoodsPrices == null) {
                    goods.setSpecGoodsPriceList(new ArrayList<>());
                } else {
                    goods.setSpecGoodsPriceList(specGoodsPrices);
                }
            });
        }
    }

    @Override
    public void withGoodsCategory(List<Goods> records) {
        if (!records.isEmpty()) {
            Set<Integer> catIds = new HashSet<>();
            for (Goods goods : records) {
                catIds.add(goods.getCatId1());
                catIds.add(goods.getCatId2());
                catIds.add(goods.getCatId3());
            }
            if (catIds.size() == 0 || catIds.size() == 1 && catIds.contains(0)) {
                return;
            }
            Map<Integer, GoodsCategory> goodsCategoryMap = goodsCategoryService.listByIds(catIds).stream().collect(Collectors.toMap(GoodsCategory::getId, item -> item));
            records.forEach(goods -> {
                List<GoodsCategory> goodsCategories = new ArrayList<>();
                if (goodsCategoryMap.containsKey(goods.getCatId1())) {
                    goodsCategories.add(goodsCategoryMap.get(goods.getCatId1()));
                }
                if (goodsCategoryMap.containsKey(goods.getCatId2())) {
                    goodsCategories.add(goodsCategoryMap.get(goods.getCatId2()));
                }
                if (goodsCategoryMap.containsKey(goods.getCatId3())) {
                    goodsCategories.add(goodsCategoryMap.get(goods.getCatId3()));
                }
                goods.setGoodsCategoryList(goodsCategories);
            });
        }
    }

    /**
     * 客户端APP再次出售
     * 修改商品价格，修改商品为上架
     *  isOnSale:0下架,1上架,2违规下架
     * @param goodsId
     * @param price
     */
    @Override
    public void reSell(Integer goodsId, BigDecimal price) {
        // 判断商品是否已提货 delete_flag=1
        Goods goods1 = this.getById(goodsId);
        if (null == goods1) {
            throw new ShopException(400,"出售失败，商品不存在");
        }
        if (goods1.getDeleteFlag() == 1) {
            throw new ShopException(400,"商品已提货不能出售");
        }
        Goods goods = new Goods();
        goods.setGoodsId(goodsId);
        goods.setShopPrice(price);
        goods.setMarketPrice(price);
        goods.setOnTime(System.currentTimeMillis() / 1000);
        goods.setIsOnSale(1);
        goods.setStoreCount(1);
        this.baseMapper.updateById(goods);
    }

    /**
     * 下架，修改isOnSale
     * isOnSale:0下架,1上架,2违规下架
     * @param goodsId
     */
    @Override
    public void downSellGood(Integer goodsId) {
        // 判断商品是否已提货 delete_flag=1
        Goods goods1 = this.getById(goodsId);
        if (null == goods1) {
            throw new ShopException(400,"下架失败，商品不存在");
        }
        if (goods1.getDeleteFlag() == 1) {
            throw new ShopException(400,"操作失败，商品已提货");
        }
        Goods goods = new Goods();
        goods.setGoodsId(goodsId);
        goods.setIsOnSale(0);
        this.baseMapper.updateById(goods);
    }

    @Override
    public IPage<Goods> pageMyShopGoods(Page<Goods> page, QueryWrapper<Goods> queryWrapper) {
        return goodsMapper.selectPageMyShopGoods(page, queryWrapper);
    }

}






