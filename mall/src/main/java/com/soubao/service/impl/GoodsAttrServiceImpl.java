package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.Goods;
import com.soubao.entity.GoodsAttr;
import com.soubao.entity.GoodsCategory;
import com.soubao.service.GoodsAttrService;
import com.soubao.dao.GoodsAttrMapper;
import com.soubao.vo.GoodsListAttrFilter;
import com.soubao.vo.GoodsListFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-15
 */
@Service
@Slf4j
public class GoodsAttrServiceImpl extends ServiceImpl<GoodsAttrMapper, GoodsAttr> implements GoodsAttrService {

    @Autowired
    private GoodsAttrMapper goodsAttrMapper;

    @Override
    public List<GoodsAttr> getAttrListByGoodsId(Integer goodsId) {
        return goodsAttrMapper.selectAttrByGoodsId(goodsId);
    }

    @Override
    public Set<Integer> getGoodsIdsByAttr(String attrGroupStr) {
        Set<Integer> goodsIdSet = new HashSet<>();
        Set<Integer> attrIdList = new HashSet<>();
        Set<String> attrValueList = new HashSet<>();
        String[] attrGroupArr = attrGroupStr.split("@");
        for (String attrGroup : attrGroupArr) {
            attrIdList.add(Integer.valueOf(attrGroup.split("_")[0]));
            attrValueList.add(attrGroup.split("_")[1]);
        }
        List<GoodsAttr> goodsAttrList = list((new QueryWrapper<GoodsAttr>().select("DISTINCT(goods_id)")
                .in("attr_id", attrIdList).in("attr_value", attrValueList)));
        if (goodsAttrList.size() > 0) {
            for (GoodsAttr goodsAttr : goodsAttrList) {
                goodsIdSet.add(goodsAttr.getGoodsId());
            }
        }
        return goodsIdSet;
    }

    @Override
    public List<GoodsListAttrFilter> getGoodsListFilterForAttr(String url, GoodsCategory goodsCategory) {
        List<GoodsListAttrFilter> goodsListAttrFilters = new ArrayList<>();
        if (goodsCategory == null){
            return goodsListAttrFilters;
        }
        UriComponentsBuilder parentSb = ServletUriComponentsBuilder.fromUriString(url);
        List<String> attrQueryList = parentSb.build().getQueryParams().get("attr");
        Set<Integer> noInAttrIds = new HashSet<>();
        if (attrQueryList != null && attrQueryList.get(0).length() > 0) {
            String[] attrArr = attrQueryList.get(0).split("@");
            for (String attrStr : attrArr) {
                noInAttrIds.add(Integer.valueOf(attrStr.split("_")[0]));
            }
        }
        List<GoodsAttr> goodsAttrList = goodsAttrMapper.selectAttrForGoodsList(goodsCategory, noInAttrIds);
        Map<String, List<GoodsListFilter>> goodsListFilterMap = new HashMap<>();
        //根据attr_name分组
        for (GoodsAttr goodsAttr : goodsAttrList) {
            if (goodsListFilterMap.containsKey(goodsAttr.getGoodsAttribute().getAttrName())) {
                GoodsListFilter goodsListFilter = new GoodsListFilter(goodsAttr.getAttrValue(),
                        parentSb.replaceQueryParam("attr", goodsAttr.getAttrId() + "_" + goodsAttr.getAttrValue()).build().getQuery());
                goodsListFilterMap.get(goodsAttr.getGoodsAttribute().getAttrName()).add(goodsListFilter);
            } else {
                List<GoodsListFilter> goodsListFilterList = new ArrayList<>();
                goodsListFilterList.add(new GoodsListFilter(goodsAttr.getAttrValue(),
                        parentSb.replaceQueryParam("attr", goodsAttr.getAttrId() + "_" + goodsAttr.getAttrValue()).build().getQuery()));
                goodsListFilterMap.put(goodsAttr.getGoodsAttribute().getAttrName(), goodsListFilterList);
            }
        }
        for (Map.Entry<String, List<GoodsListFilter>> entry : goodsListFilterMap.entrySet()) {
            goodsListAttrFilters.add(new GoodsListAttrFilter(entry.getKey(), entry.getValue()));
        }
        return goodsListAttrFilters;
    }

    @Override
    public void updateGoodsAttr(Goods goods, List<GoodsAttr> goodsAttrs) {
        if(goodsAttrs != null){
            if(goodsAttrs.size() == 0){
                remove((new QueryWrapper<GoodsAttr>()).eq("goods_id", goods.getGoodsId()));
            }else{
                for(GoodsAttr goodsAttr :goodsAttrs){
                    goodsAttr.setGoodsId(goods.getGoodsId());
                }
                List<GoodsAttr> exGoodsAttrList = this.list((new QueryWrapper<GoodsAttr>()).eq("goods_id", goods.getGoodsId()));
                if(exGoodsAttrList.size() > 0){
                    Map<Integer, GoodsAttr> goodsAttrIdMap = goodsAttrs.stream().collect(Collectors.toMap(GoodsAttr::getAttrId, goodsAttr -> goodsAttr));
                    ListIterator<GoodsAttr> exGoodsAttrIt = exGoodsAttrList.listIterator();
                    Set<Integer> deleteGoodsAttrIds = new HashSet<>();
                    while(exGoodsAttrIt.hasNext()){
                        GoodsAttr exGoodsAttr = exGoodsAttrIt.next();
                        if(!goodsAttrIdMap.containsKey(exGoodsAttr.getAttrId())){
                            deleteGoodsAttrIds.add(exGoodsAttr.getGoodsAttrId());
                        }else{
                            GoodsAttr goodsAttr = goodsAttrIdMap.get(exGoodsAttr.getAttrId());
                            goodsAttr.setAttrValue(exGoodsAttr.getAttrValue());
                            goodsAttr.setGoodsAttrId(exGoodsAttr.getGoodsAttrId());
                        }
                    }
                    if(deleteGoodsAttrIds.size() > 0){
                        this.remove((new QueryWrapper<GoodsAttr>()).in("goods_attr_id", deleteGoodsAttrIds));
                    }
                }
                this.saveOrUpdateBatch(goodsAttrs);
            }
        }
    }

    @Override
    public void addGoodsAttr(Goods goods, List<GoodsAttr> goodsAttrs) {
        if(goodsAttrs != null && goodsAttrs.size() > 0){
            for(GoodsAttr goodsAttr :goodsAttrs){
                goodsAttr.setGoodsId(goods.getGoodsId());
            }
            this.saveBatch(goodsAttrs);
        }
    }
}
