package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.SpecGoodsPrice;
import com.soubao.service.SpecGoodsPriceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/spec_goods_price")
@Api(value = "商品规格价格控制器", tags = {"商品规格价格相关接口"})
public class SpecGoodsPriceController {

    @Autowired
    private SpecGoodsPriceService specGoodsPriceService;

    @ApiOperation(value = "规格价格列表")
    @GetMapping("/list")
    public List<SpecGoodsPrice> specGoodsPrices(@ApiParam("商品主键") @RequestParam("goods_id") Integer goodsId) {
        return specGoodsPriceService.list((new QueryWrapper<SpecGoodsPrice>()).eq("goods_id", goodsId));
    }
}
