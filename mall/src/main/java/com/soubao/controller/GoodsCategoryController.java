package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.GoodsAttr;
import com.soubao.entity.GoodsAttribute;
import com.soubao.entity.GoodsCategory;
import com.soubao.entity.Store;
import com.soubao.service.GoodsAttrService;
import com.soubao.service.GoodsAttributeService;
import com.soubao.service.GoodsCategoryService;
import com.soubao.service.SellerService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(value = "商品分类接口", tags = {"商品分类相关接口"})
@RequestMapping("/goods/category")
public class GoodsCategoryController {
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    @Autowired
    private GoodsAttributeService goodsAttributeService;
    @Autowired
    private GoodsAttrService goodsAttrService;
    @Autowired
    private SellerService sellerService;

    @ApiOperation(value = "商品分类列表查询接口")
    @GetMapping("list")
    public List<GoodsCategory> categoryList(
            @ApiParam("模型id") @RequestParam(value = "type_id", required = false) Integer typeId,
            @ApiParam("父id") @RequestParam(value = "parent_id", required = false) Integer parentId,
            @ApiParam("第几级") @RequestParam(value = "level", required = false) Integer level,
            @ApiParam("id组合") @RequestParam(value = "id", required = false) Set<Integer> ids) {
        QueryWrapper<GoodsCategory> goodsCategoryQueryWrapper = new QueryWrapper<>();
        goodsCategoryQueryWrapper.orderByDesc("sort_order");
        if (level != null) {
            goodsCategoryQueryWrapper.eq("level", level);
        }
        if (parentId != null) {
            goodsCategoryQueryWrapper.eq("is_show", 1).eq("parent_id", parentId);
        }
        if (ids != null) {
            goodsCategoryQueryWrapper.in("id", ids);
        }
        if (typeId != null) {
            goodsCategoryQueryWrapper.eq("type_id", typeId);
        }
        return goodsCategoryService.list(goodsCategoryQueryWrapper);
    }

    @ApiOperation(value = "根据一级分类id获取所有商品子孙类")
    @GetMapping("progeny/{cat_id}")
    public List<GoodsCategory> secAndThirdCategoryList(@ApiParam("一级分类id") @PathVariable("cat_id") Integer catId) {
        return goodsCategoryService.getSecAndThirdCategoryListByFirstId(catId);
    }

    @GetMapping("tree")
    public List<GoodsCategory> goodsCategoriesTree(
            @RequestParam(value = "levels", required = false) Set<Integer> levels,
            @RequestParam(value = "ids", required = false) Set<Integer> ids) {
        QueryWrapper<GoodsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id,name,mobile_name,parent_id,type_id,image,level");
        if (levels != null) {
            queryWrapper.in("level", levels);
        }
        if (ids != null) {
            queryWrapper.in("id", ids);
        }
        return goodsCategoryService.listToTree(goodsCategoryService.list(queryWrapper));
    }

    @GetMapping("list/admin")
    public List<GoodsCategory> categoryTypeList(@RequestParam(value = "parent_id", defaultValue = "0") Integer parentId) {
        return goodsCategoryService.getGoodsCategoryWithGoodsType(parentId);
    }

    @GetMapping("brands")
    public List<GoodsCategory> getGoodsCatBrand() {
        return goodsCategoryService.getCategoryWithBrand();
    }

    @GetMapping
    public GoodsCategory getOne(@RequestParam(value = "id") Integer id) {
        return goodsCategoryService.getById(id);
    }

    @PostMapping
    public SBApi save(@Valid @RequestBody GoodsCategory goodsCategory) {
        goodsCategoryService.save(goodsCategory);
        GoodsCategory insertGoodsCategory = goodsCategoryService.getById(goodsCategory.getId());
        //兼容php数据库设计
        if (insertGoodsCategory.getParentId() == 0) {
            insertGoodsCategory.setParentIdPath(insertGoodsCategory.getParentId() + "_" + insertGoodsCategory.getId());
        } else {
            insertGoodsCategory.setParentIdPath(insertGoodsCategory.getParentIdPath() + "_" + insertGoodsCategory.getId());
        }
        goodsCategoryService.updateById(insertGoodsCategory);
        return new SBApi();
    }

    @PutMapping
    public SBApi update(@Valid @RequestBody GoodsCategory goodsCategory) {
        goodsCategoryService.updateGoodsCategory(goodsCategory);
        return new SBApi();
    }

    @DeleteMapping("{id}")
    public SBApi remove(@PathVariable(value = "id") Integer id) {
        goodsCategoryService.deleteById(id);
        return new SBApi();
    }

    @GetMapping("attributes")
    public List<GoodsAttribute> goodsAttr(@RequestParam(value = "cat_id3") Integer catId3,
                                          @RequestParam(value = "goods_id", required = false) Integer goodsId) {
        GoodsCategory goodsCategory = goodsCategoryService.getById(catId3);
        List<GoodsAttribute> goodsAttributeList = goodsAttributeService.list((new QueryWrapper<GoodsAttribute>())
                .eq("type_id", goodsCategory.getTypeId()).eq("attr_index", 1));
        if (goodsId != null) {
            List<GoodsAttr> goodsAttrList = goodsAttrService.list((new QueryWrapper<GoodsAttr>()).eq("goods_id", goodsId));
            if (goodsAttrList.size() > 0) {
                Map<Integer, String> goodsAttrIdToValue = goodsAttrList.stream().collect(Collectors.toMap(GoodsAttr::getAttrId, GoodsAttr::getAttrValue));
                for (GoodsAttribute goodsAttribute : goodsAttributeList) {
                    if (goodsAttrIdToValue.containsKey(goodsAttribute.getAttrId())) {
                        goodsAttribute.setAttrValue(goodsAttrIdToValue.get(goodsAttribute.getAttrId()));
                    }
                }
            }
        }
        return goodsAttributeList;
    }

}
