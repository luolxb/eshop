package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.soubao.entity.*;
import com.soubao.service.*;
import com.soubao.dao.GoodsTypeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-11-20
 */
@Service("goodsTypeService")
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements GoodsTypeService {
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    @Autowired
    private GoodsAttributeService goodsAttributeService;
    @Autowired
    private BrandTypeService brandTypeService;
    @Autowired
    private SpecService specService;
    @Autowired
    private SpecTypeService specTypeService;
    @Override
    public void bindGoodsGoodsCategoryList(GoodsType goodsType, List<GoodsCategory> goodsCategoryList) {
        if(goodsType.getId() > 0){
            goodsCategoryService.update((new UpdateWrapper<GoodsCategory>()).eq("type_id", goodsType.getId()).set("type_id", 0));
        }
        if(goodsCategoryList.size() > 0){
            Set<Integer> goodsCategoryIds = new HashSet<>();
            for(GoodsCategory goodsCategory : goodsCategoryList){
                goodsCategory.setTypeId(goodsType.getId());
                goodsCategoryIds.add(goodsCategory.getParentIds().get(2));
            }
            if(goodsCategoryIds.size() > 0){
                goodsCategoryService.update((new UpdateWrapper<GoodsCategory>()).in("id", goodsCategoryIds).set("type_id", goodsType.getId()));
            }
        }
    }

    @Override
    public void bindGoodsAttribute(GoodsType goodsType, List<GoodsAttribute> goodsAttributes) {
        if(goodsAttributes.size() > 0){
            for(GoodsAttribute goodsAttribute : goodsAttributes){
                goodsAttribute.setTypeId(goodsType.getId());
            }
            goodsAttributeService.saveOrUpdateBatch(goodsAttributes);
        }
    }

    @Override
    public void bindSpec(GoodsType goodsType, List<Spec> specList) {
        if(specList.size() > 0) {
            specService.saveOrUpdateBatch(specList);
            specTypeService.remove((new QueryWrapper<SpecType>()).eq("type_id", goodsType.getId()));
            List<SpecType> specTypeList = new ArrayList<>();
            for (Spec spec : specList) {
                specTypeList.add(new SpecType(goodsType.getId(), spec.getId()));
            }
            specTypeService.saveBatch(specTypeList);
        }
    }

    @Override
    public void bindBand(GoodsType goodsType, List<BrandType> brandTypeList) {
        brandTypeService.remove((new QueryWrapper<BrandType>()).eq("type_id", goodsType.getId()));
        if(goodsType.getBrandTypeList().size() > 0){
            for(BrandType brandType : goodsType.getBrandTypeList()){
                brandType.setTypeId(goodsType.getId());
            }
            brandTypeService.saveBatch(goodsType.getBrandTypeList());
        }
    }

    @Override
    public void updateForBind(GoodsType goodsType) {
        bindGoodsGoodsCategoryList(goodsType, goodsType.getGoodsCategoryList());
        bindGoodsAttribute(goodsType, goodsType.getGoodsAttributes());
        bindSpec(goodsType, goodsType.getSpecList());
        bindBand(goodsType, goodsType.getBrandTypeList());
    }
}
