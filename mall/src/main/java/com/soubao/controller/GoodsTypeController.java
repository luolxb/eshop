package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.*;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2019-11-20
 */
@Api(value = "商品模型")
@RestController
@RequestMapping("goods_type")
@Slf4j
public class GoodsTypeController {
    @Autowired
    private GoodsTypeService goodsTypeService;
    @Autowired
    private GoodsAttributeService goodsAttributeService;
    @Autowired
    private BrandTypeService brandTypeService;
    @Autowired
    private SpecService specService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    @Autowired
    private SpecTypeService specTypeService;

    @GetMapping("page")
    @ApiOperation(value = "获取商品模型分页", notes = "用于总后台模型管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "条数", paramType = "query", defaultValue = "10"),
    })
    @ApiResponse(code = 400, message = "商品模型分页", response = IPage.class)
    public IPage<GoodsType> getPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size,
                                    @RequestParam(value = "name", required = false) String name) {
        QueryWrapper<GoodsType> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("id");
        return goodsTypeService.page(new Page<>(page, size), queryWrapper);
    }

    @GetMapping
    public GoodsType getGoodsType(@RequestParam(value = "id") Integer id) {
        GoodsType goodsType = goodsTypeService.getById(id);
        goodsType.setGoodsCategoryList(goodsCategoryService.list((new QueryWrapper<GoodsCategory>()).eq("type_id", id)));
        goodsType.setGoodsAttributes(goodsAttributeService.list((new QueryWrapper<GoodsAttribute>()).eq("type_id", id)));
        goodsType.setSpecList(specService.selectSpecByTypeId(id));
        goodsType.setBrandTypeList(brandTypeService.list((new QueryWrapper<BrandType>()).eq("type_id", id)));
        return goodsType;
    }

    @PostMapping
    public SBApi createGoodsType(@Valid @RequestBody GoodsType goodsType, SBApi sbApi){
        goodsTypeService.save(goodsType);
        goodsTypeService.updateForBind(goodsType);
        return sbApi;
    }

    @PutMapping
    public SBApi updateGoodsType(@Valid @RequestBody GoodsType goodsType, SBApi sbApi) {
        goodsTypeService.updateById(goodsType);
        goodsTypeService.updateForBind(goodsType);
        return sbApi;
    }

    @DeleteMapping("{type_id}")
    public SBApi deleteGoodsType(@PathVariable(value = "type_id") Integer typeId, SBApi sbApi){
        int specTypeCount = specTypeService.count((new QueryWrapper<SpecType>()).eq("type_id", typeId));
        if(specTypeCount > 0){
            throw new ShopException(ResultEnum.GOODS_TYPE_HAVE_SPEC);
        }
        int brandTypeCount = brandTypeService.count((new QueryWrapper<BrandType>()).eq("type_id", typeId));
        if(brandTypeCount > 0){
            throw new ShopException(ResultEnum.GOODS_TYPE_HAVE_BRAND);
        }
        int goodsAttributeCount = goodsAttributeService.count((new QueryWrapper<GoodsAttribute>()).eq("type_id", typeId));
        if(goodsAttributeCount > 0){
            throw new ShopException(ResultEnum.GOODS_TYPE_HAVE_ATTR);
        }
        goodsCategoryService.update((new UpdateWrapper<GoodsCategory>()).set("type_id", 0).eq("type_id", typeId));
        goodsTypeService.removeById(typeId);
        return sbApi;
    }

    @DeleteMapping("goods_category/{cat_id}")
    public SBApi canDeleteCategory(@PathVariable(value = "cat_id") Integer catId, SBApi sbApi){
        int goodsUseCatCount = goodsService.count((new QueryWrapper<Goods>()).eq("cat_id3", catId));
        if(goodsUseCatCount > 0){
            throw new ShopException(ResultEnum.CAT_HAVE_GOODS);
        }
        return sbApi;
    }
}
