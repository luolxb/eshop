package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Goods;
import com.soubao.entity.PromGoods;
import com.soubao.service.CouponService;
import com.soubao.service.GoodsService;
import com.soubao.service.PromGoodsService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("prom_goods")
@RestController
@Api(value = "促销活动控制器", tags = {"促销活动相关接口"})
public class PromGoodsController {
    @Autowired
    private PromGoodsService promGoodsService;

    @ApiOperation(value = "优惠促销列表", notes = "获取优惠促销列表", httpMethod = "GET")
    @GetMapping("/page")
    public IPage<PromGoods> promGoodsPage(
            @ApiParam("店铺id") @RequestParam(value = "store_id",required = false) Integer storeId,
            @ApiParam("起始时间") @RequestParam(value = "start_time", required = false) Long startTime,
            @ApiParam("截止时间") @RequestParam(value = "end_time", required = false) Long endTime,
            @ApiParam("活动状态") @RequestParam(value = "status", required = false) Integer status,
            @ApiParam("活动名称") @RequestParam(value = "title", required = false) String title,
            @ApiParam("当前页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @ApiParam("每页显示条数") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<PromGoods> wrapper = new QueryWrapper<>();
        if (storeId != null) {
            wrapper.eq("store_id", storeId);
        }
        if (startTime != null) {
            wrapper.ge("start_time", startTime);
        }
        if (endTime != null) {
            wrapper.lt("end_time", endTime);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
        }
        wrapper.eq("is_deleted", 0);
        wrapper.orderByDesc("start_time");
        return promGoodsService.page(new Page<>(page, size), wrapper);
    }

    @GetMapping("/goods/page")
    public IPage<Goods> goodsIPage(
            @RequestParam(value = "store_id", required = false) Integer storeId,
            @RequestParam(value = "start_time", required = false) Long startTime,
            @RequestParam(value = "end_time", required = false) Long endTime,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "recommend", defaultValue = "1") Integer recommend,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<PromGoods> wrapper = new QueryWrapper<>();
        wrapper.eq("p.status", status).eq("p.is_end", 0).eq("p.is_deleted", 0)
                .eq("p.recommend", recommend);
        if (storeId != null) {
            wrapper.eq("p.store_id", storeId);
        }
        if (!StringUtils.isEmpty(title)) {
            wrapper.like("p.title", title);
        }
        if (startTime != null) {
            wrapper.ge("p.start_time", startTime);
        }else{
            wrapper.lt("p.start_time", System.currentTimeMillis() / 1000);
        }
        if (endTime != null) {
            wrapper.lt("p.end_time", endTime);
        }else{
            wrapper.gt("p.end_time", System.currentTimeMillis() / 1000);
        }
        wrapper.apply(false, "group by g.goods_id").orderByDesc("p.id");
        return promGoodsService.goodsPage(new Page<>(page, size), wrapper);
    }

    @ApiOperation(value = "更新活动是否推荐")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "recommend", value = "是否推荐：1是，0否", required = true, paramType = "query")
    })
    @PutMapping("recommend")
    public SBApi recommend(@RequestBody PromGoods promGoods, SBApi sbApi) {
        promGoodsService.update(new UpdateWrapper<PromGoods>().set("recommend", promGoods.getRecommend()).eq("id", promGoods.getId()));
        return sbApi;
    }

    @ApiOperation(value = "删除活动")
    @ApiImplicitParam(name = "prom_id", value = "活动id", required = true, paramType = "path")
    @DeleteMapping("{prom_id}")
    public SBApi remove(@PathVariable("prom_id") Integer promId) {
        promGoodsService.removePromGoods(promId);
        return new SBApi();
    }

    @ApiOperation(value = "关闭活动")
    @ApiImplicitParam(name = "prom_id", value = "活动id", required = true, paramType = "path")
    @PutMapping("close_prom/{prom_id}")
    public SBApi close(@PathVariable("prom_id") Integer promId) {
        promGoodsService.closeProm(promId);
        return new SBApi();
    }

    @ApiOperation(value = "获取活动")
    @GetMapping("/{id}")
    public PromGoods getPromGoods(@PathVariable(value = "id") Integer promGoodsId) {
        return promGoodsService.getPromGoods(promGoodsId);
    }

    @ApiOperation(value = "新增活动")
    @PostMapping
    public SBApi addPromGoods(@Valid @RequestBody PromGoods promGoods) {
        promGoodsService.addPromGoods(promGoods);
        return new SBApi();
    }

    @ApiOperation(value = "修改活动")
    @PutMapping
    public SBApi updatePromGoods(@Valid @RequestBody PromGoods promGoods) {
        promGoodsService.updatePromGoods(promGoods);
        return new SBApi();
    }

}
