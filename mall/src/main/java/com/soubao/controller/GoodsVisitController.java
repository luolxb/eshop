package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Goods;
import com.soubao.entity.GoodsVisit;
import com.soubao.entity.User;
import com.soubao.service.GoodsService;
import com.soubao.service.GoodsVisitService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.vo.DateVo;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(
        value = "商品浏览记录控制器",
        tags = {"商品浏览记录相关接口"})
@RequestMapping("/goods/visit")
public class GoodsVisitController {

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private GoodsVisitService goodsVisitService;

    @Autowired
    private GoodsService goodsService;

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("获取单个商品浏览记录")
    @GetMapping
    public GoodsVisit getGoodsVisit(@ApiParam("商品id") @RequestParam(value = "goods_id") Integer goodsId) {
        User user = authenticationFacade.getPrincipal(User.class);
        return goodsVisitService.getOne((new QueryWrapper<GoodsVisit>())
                .eq("goods_id", goodsId).eq("user_id", user.getUserId()));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("添加商品浏览记录")
    @PostMapping
    public SBApi visitLog(Goods requestGoods, SBApi sbApi) {
        User user = authenticationFacade.getPrincipal(User.class);
        Goods goods = goodsService.getById(requestGoods.getGoodsId());
        goodsVisitService.loggingGoods(user, goods);
        sbApi.setMsg("添加成功");
        return sbApi;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("商品访问记录")
    @GetMapping("/page")
    public Page<DateVo> visitLog(
            @ApiParam("分类id") @RequestParam(value = "cat_id", required = false) Integer catId,
            @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer pageIndex,
            @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "20") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        QueryWrapper<GoodsVisit> wrapper = new QueryWrapper<>();
        if (catId != null) {
            wrapper.eq("gv.cat_id3", catId);
        }
        wrapper.eq("gv.user_id", user.getUserId());
        return goodsVisitService.getVisitByDateGroup(new Page<>(pageIndex, size), wrapper);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("删除商品访问记录")
    @DeleteMapping
    public SBApi visitLog(
            @ApiParam("商品id") @RequestParam(value = "goods_id") Integer goodsId, SBApi sbApi) {
        User user = authenticationFacade.getPrincipal(User.class);
        goodsVisitService.remove(
                (new QueryWrapper<GoodsVisit>())
                        .eq("goods_id", goodsId)
                        .eq("user_id", user.getUserId()));
        sbApi.setMsg("删除成功");
        return sbApi;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("访问记录分类")
    @GetMapping("/cat/list")
    public List<GoodsVisit> visitCatCount() {
        User user = authenticationFacade.getPrincipal(User.class);
        return goodsVisitService.getVisitCatCount(user);
    }
}
