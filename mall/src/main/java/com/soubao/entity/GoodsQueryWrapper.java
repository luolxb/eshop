package com.soubao.entity;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.Set;

@Data
public class GoodsQueryWrapper {
    private QueryWrapper<Goods> goodsQueryWrapper;

    //https://blog.csdn.net/HiBoyljw/article/details/89951019 示例参考
    private BoolQueryBuilder goodsQueryBuilder = QueryBuilders.boolQuery();
    private FieldSortBuilder fieldSortBuilder = null;
    private boolean isElasticsearch = false;

    public GoodsQueryWrapper() {
        goodsQueryWrapper = new QueryWrapper<>();
    }

    public NativeSearchQueryBuilder getNativeSearchQueryBuilder(){
        return new NativeSearchQueryBuilder().withQuery(goodsQueryBuilder).withSort(fieldSortBuilder);
    }

    public GoodsQueryWrapper isOnSale(Integer isOnSale){
        if(isOnSale != null){
            goodsQueryWrapper.eq("is_on_sale", isOnSale);
            goodsQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("is_on_sale", isOnSale)));
        }
        return this;
    }


    public GoodsQueryWrapper goodsState(Set<Integer> goodsState){
        if(goodsState != null){
            goodsQueryWrapper.in("goods_state", goodsState);
            goodsQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("goods_state", goodsState)));
        }
        return this;
    }

    public GoodsQueryWrapper isRecommend(Integer isRecommend){
        if(isRecommend != null){
            goodsQueryWrapper.eq("is_recommend", isRecommend);
            goodsQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("is_recommend", isRecommend)));
        }
        return this;
    }

    public GoodsQueryWrapper goodsCategory(GoodsCategory goodsCategory) {
        if(goodsCategory != null){
            goodsQueryWrapper.eq("cat_id" + goodsCategory.getLevel(), goodsCategory.getId());//商品分类筛选
            goodsQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("cat_id" + goodsCategory.getLevel(), goodsCategory.getId())));
        }
        return this;
    }

    public GoodsQueryWrapper catId1(Integer catId1){
        if (catId1 != null){
            goodsQueryWrapper.eq("cat_id1", catId1);
            goodsQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("cat_id1", catId1)));
        }
        return this;
    }

    public GoodsQueryWrapper brandIds(Set<Integer> brandIds) {
        if(brandIds != null && !brandIds.isEmpty()){
            goodsQueryWrapper.in("brand_id", brandIds);
            goodsQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("brand_id", brandIds)));
        }
        return this;
    }

    /**
     * 调用前需判断goodsIdSet是否为空，为空时表示没有符合条件的商品，控制器直接返回即可
     * @param goodsIdSet 商品id集合
     * @return
     */
    public GoodsQueryWrapper inGoodsIds(Set<Integer> goodsIdSet){
        goodsQueryWrapper.in("goods_id", goodsIdSet);
        goodsQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("goods_id", goodsIdSet)));
        return this;
    }

    public GoodsQueryWrapper notInGoodsIds(Set<Integer> goodsIdSet){
        if(goodsIdSet != null && !goodsIdSet.isEmpty()){
            goodsQueryWrapper.notIn("goods_id", goodsIdSet);
            goodsQueryBuilder.must(QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery("goods_id", goodsIdSet)));
        }
        return this;
    }

    public GoodsQueryWrapper isExchangeIntegral(boolean isExchangeIntegral){
        if(isExchangeIntegral){
            goodsQueryWrapper.eq("is_virtual", 0).gt("exchange_integral", 0);
            goodsQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("is_virtual", 0))
                    .mustNot(QueryBuilders.termQuery("exchange_integral", 0)));
        }
        return this;
    }
    public GoodsQueryWrapper startPrice(Integer startPrice){
        if(startPrice != null && startPrice > 0){
            goodsQueryWrapper.ge("shop_price", startPrice);
            goodsQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("shop_price").gt(startPrice)));
        }
        return this;
    }
    public GoodsQueryWrapper endPrice(Integer endPrice){
        if(endPrice != null && endPrice > 0){
            goodsQueryWrapper.le("shop_price", endPrice);
            goodsQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("shop_price").lt(endPrice)));
        }
        return this;
    }

    public GoodsQueryWrapper orderBy(String orderBy, String sort){
        if(!StringUtils.isEmpty(orderBy)){
            if(!StringUtils.isEmpty(sort) && sort.toLowerCase().equals("asc")){
                goodsQueryWrapper.orderByAsc(orderBy);
                fieldSortBuilder = SortBuilders.fieldSort(orderBy).order(SortOrder.ASC);
            }else{
                goodsQueryWrapper.orderByDesc(orderBy);
                fieldSortBuilder = SortBuilders.fieldSort(orderBy).order(SortOrder.DESC);
            }
        }
        return this;
    }

    public GoodsQueryWrapper goodsName(String goodsName) {
        if (!StringUtils.isEmpty(goodsName)){
            goodsQueryWrapper.like("goods_name", goodsName);
        }
        return this;
    }

    public GoodsQueryWrapper isDistribut(Integer isDistribution) {
        if(isDistribution != null) {
            goodsQueryWrapper.gt("distribut", 0);
            goodsQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("distribut").gt(0)));
        }
        return this;
    }

    public GoodsQueryWrapper isBrand(){
        goodsQueryWrapper.ne("brand_id", 0);
        goodsQueryBuilder.must(QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery("brand_id", 0)));
        return this;
    }

    public GoodsQueryWrapper isHot(Integer isHost) {
        if(isHost != null){
            goodsQueryWrapper.eq("is_hot", isHost);
            goodsQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("is_hot", isHost)));
        }
        return this;
    }

    public GoodsQueryWrapper isNew(Integer isNew) {
        if(isNew != null){
            goodsQueryWrapper.eq("is_new", isNew);
            goodsQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("is_new", isNew)));
        }
        return this;
    }


    public GoodsQueryWrapper storeId(Integer storeId) {
        if(storeId != null){
            goodsQueryWrapper.eq("store_id", storeId);
            goodsQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("store_id", storeId)));
        }
        return this;
    }

    public GoodsQueryWrapper promType(Integer promType) {
        if(promType != null){
            goodsQueryWrapper.eq("prom_type", promType);
            goodsQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("prom_type", promType)));
        }
        return this;
    }

    public GoodsQueryWrapper promId(Integer promId) {
        if(promId != null){
            goodsQueryWrapper.eq("prom_id", promId);
            goodsQueryBuilder.must(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("prom_id", promId)));
        }
        return this;
    }

    public GoodsQueryWrapper search(String words) {
        if(StringUtils.isNotEmpty(words)){
            isElasticsearch = true;
            goodsQueryWrapper.like("goods_name", words);
            goodsQueryBuilder.must(QueryBuilders.multiMatchQuery(words, "goods_name", "goods_remark", "keywords")).boost(2.0f);//boost：设置查询权重，默认是1
        }
        return this;
    }

}
