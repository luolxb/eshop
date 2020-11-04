package com.soubao.controller;

import com.soubao.entity.GoodsAttr;
import com.soubao.service.GoodsAttrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/goods_attr")
@Api(value = "商品属性值", tags = {"商品属性值相关接口"})
public class GoodsAttrController {

    @Autowired
    private GoodsAttrService goodsAttrService;

    @ApiOperation(value = "商品属性列表", notes = "获取商品属性", httpMethod = "GET")
    @GetMapping("/list")
    public List<GoodsAttr> content(@RequestParam("goods_id") Integer goodsId) {
        return goodsAttrService.getAttrListByGoodsId(goodsId);
    }
}
