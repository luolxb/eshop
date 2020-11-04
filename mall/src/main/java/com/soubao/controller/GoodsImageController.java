package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.Goods;
import com.soubao.entity.GoodsImages;
import com.soubao.service.GoodsImagesService;
import com.soubao.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/goods/image")
@Api(value = "商品图片控制器", tags = {"商品图片相关接口"})
public class GoodsImageController {
    @Autowired
    private GoodsImagesService goodsImagesService;
    @Autowired
    private GoodsService goodsService;

    @ApiOperation(value = "商品图片列表", notes = "获取商品图片列表", httpMethod = "GET")
    @GetMapping("/list")
    public List<GoodsImages> images(@ApiParam("商品id") @RequestParam("goods_id") Integer goodsId) {
        return goodsImagesService.list((new QueryWrapper<GoodsImages>()).eq("goods_id", goodsId).orderByDesc("img_sort"));
    }

}
