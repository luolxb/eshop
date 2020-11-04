package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/spec")
@Api(value = "商品规格控制器", tags = {"商品规格关接口"})
public class SpecController {
    @Autowired
    private SpecService specService;
    @Autowired
    private SpecTypeService specTypeService;
    @Autowired
    private SpecItemService specItemService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SpecGoodsPriceService specGoodsPriceService;
    @Autowired
    private SpecImageService specImageService;

    @ApiOperation(value = "商品规格项及其子项列表")
    @GetMapping("/goods")
    public List<Spec> goodsSpecs(@ApiParam("商品主键") @RequestParam("goods_id") Integer goodsId) {
        List<SpecGoodsPrice> specGoodsPriceList = specGoodsPriceService.list((new QueryWrapper<SpecGoodsPrice>())
                .eq("goods_id", goodsId));
        return specService.selectSpecsWithItem(specGoodsPriceList);
    }

    @GetMapping(value = "/spec_goods_price/list")
    public List<SpecGoodsPrice> getSpecGoodsPriceList(@RequestParam(value = "goods_id", required = false) Integer goodsId,
                                                      @RequestParam(value = "spec_item_ids") Set<Integer> specItemIds) {
        return specService.getSpecGoodsPriceListBySpecItemIds(specItemIds, goodsId);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "获取商品规格列表", notes = "商品规格表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type_id", value = "商品模型id", paramType = "query"),
    })
    @ApiResponse(code = 400, message = "商品规格列表", response = Spec.class)
    public List<Spec> getSpecs(@RequestParam(value = "type_id", required = false) Integer typeId){
        QueryWrapper<Spec> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(typeId)){
            List<SpecType> specTypes = specTypeService.list((new QueryWrapper<SpecType>()).select("spec_id").eq("type_id", typeId));
            Set<Integer> specIds = specTypes.stream().map(SpecType::getSpecId).collect(Collectors.toSet());
            if(specTypes.size() > 0){
                queryWrapper.in("id", specIds);
            }
        }
        return specService.list(queryWrapper);
    }

    @GetMapping(value = "/store_spec_item")
    @ApiOperation(value = "获取商品规格与及子项列表", notes = "商品规格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type_id", value = "商品模型id", paramType = "query"),
            @ApiImplicitParam(name = "store_id", value = "店铺id", paramType = "query"),
    })
    @ApiResponse(code = 400, message = "商品规格列表", response = Spec.class)
    public List<Spec> getSpecAndItem(@RequestParam(value = "type_id") Integer typeId,
                                     @RequestParam(value = "store_id") Integer storeId){
        return specService.getStoreBindSpecAndItem(storeId, typeId);
    }

    @GetMapping(value = "/goods_spec_item")
    @ApiOperation(value = "获取商品规格与及子项列表", notes = "商品规格,用于商品编辑")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type_id", value = "商品模型id", paramType = "query"),
            @ApiImplicitParam(name = "goods_id", value = "goods_id", paramType = "query"),
    })
    @ApiResponse(code = 400, message = "商品规格列表", response = Spec.class)
    public List<Spec> getGoodsSpecAndItem(@RequestParam(value = "type_id") Integer typeId,
                                          @RequestParam(value = "goods_id") Integer goodsId){
        Goods goods = goodsService.getById(goodsId);
        List<Spec> specs = specService.getStoreBindSpecAndItem(goods.getStoreId(), typeId);
        List<SpecImage> specImages = specImageService.list((new QueryWrapper<SpecImage>()).eq("goods_id", goodsId));
        ConcurrentMap<Integer, String> specIdToSrc = specImages.stream().collect(Collectors.toConcurrentMap(SpecImage::getSpecImageId, SpecImage::getSrc));
        List<SpecGoodsPrice> specGoodsPrices = specGoodsPriceService.list((new QueryWrapper<SpecGoodsPrice>()).eq("goods_id", goodsId));
        Set<Integer> specItemIds = specGoodsPriceService.getSpecItemIds(specGoodsPrices);
        for (Spec spec : specs) {
            for(SpecItem specItem: spec.getSpecItemList()){
                specItem.setIsCheck(specItemIds.contains(specItem.getId()));
                specItem.setSrc(specIdToSrc.getOrDefault(specItem.getId(), ""));
            }
        }
        return specs;
    }

    @DeleteMapping(value = "/item")
    public SBApi deleteSpecItem(@RequestParam(value = "id") Integer specItemId,
                                @RequestParam(value = "store_id") Integer storeId,
                                SBApi sbApi){
        SpecItem specItem = specItemService.getOne((new QueryWrapper<SpecItem>())
                .eq("store_id", storeId).eq("id", specItemId));
        specItemService.deleteSpecItem(specItem);
        return sbApi;
    }

    @PostMapping(value = "/item")
    public SBApi addSpecItem(@RequestBody SpecItem specItem,
                             SBApi sbApi) {
        int specItemHaveOne = specItemService.count((new QueryWrapper<SpecItem>())
                .eq("store_id", specItem.getStoreId()).eq("item", specItem.getItem()).eq("spec_id", specItem.getSpecId()));
        if(specItemHaveOne > 0){
            throw new ShopException(ResultEnum.SPEC_HAVE_NAME);
        }
        int specItemLimitCount = specItemService.count((new QueryWrapper<SpecItem>()).eq("store_id", specItem.getStoreId()).eq("spec_id", specItem.getSpecId()));
        if(specItemLimitCount >= 15){
            throw new ShopException(ResultEnum.SPEC_HAVE_TOO_MANY);
        }
        specItemService.save(specItem);
        return sbApi;
    }


    @PostMapping(value = "/items")
    public SBApi saveSpecItems(@RequestBody List<SpecItem> specItems){
        if (specItems.size() == 0) {
            throw new ShopException(ResultEnum.ADD_SPEC_ERROR);
        }
        specItemService.saveOrUpdateBatch(specItems);
        return new SBApi();
    }

    @DeleteMapping(value = "/{id}")
    public SBApi deleteSpecItem(@PathVariable(value = "id") Integer id, SBApi sbApi){
        List<SpecItem> specItems = specItemService.list((new QueryWrapper<SpecItem>()).eq("spec_id", id));
        specItemService.deleteSpecItemList(specItems);
        specTypeService.remove((new QueryWrapper<SpecType>()).eq("spec_id", id));
        specService.removeById(id);
        return sbApi;
    }
}
