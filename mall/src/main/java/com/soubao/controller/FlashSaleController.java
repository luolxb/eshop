package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.FlashSale;
import com.soubao.service.FlashSaleService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("flash_sale")
@RestController
@Api(
        value = "秒杀活动控制器",
        tags = {"秒杀活动相关接口"})
public class FlashSaleController {
    @Autowired private FlashSaleService flashSaleService;

    @GetMapping("page")
    @ApiOperation(value = "秒杀活动商品列表分页", notes = "秒杀活动商品列表", httpMethod = "GET")
    public IPage<FlashSale> goods(
            @ApiParam("店铺id") @RequestParam(value = "store_id", required = false) Integer storeId,
            @ApiParam("起始时间") @RequestParam(value = "start_time", required = false) Long startTime,
            @ApiParam("截止时间") @RequestParam(value = "end_time", required = false) Long endTime,
            @ApiParam("活动状态") @RequestParam(value = "status", required = false) Integer status,
            @ApiParam("推荐") @RequestParam(value = "recommend", required = false) Integer recommend,
            @ApiParam("活动标题") @RequestParam(value = "title", required = false) String title,
            @ApiParam("页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<FlashSale> flashSaleQueryWrapper = new QueryWrapper<>();
        if (storeId != null) {
            flashSaleQueryWrapper.eq("fs.store_id", storeId);
        }
        if (startTime != null) {
            flashSaleQueryWrapper.ge("fs.start_time", startTime);
        }
        if (status != null) {
            flashSaleQueryWrapper.eq("fs.status", status);
            if (status == 1) {
                flashSaleQueryWrapper.ge("fs.end_time", System.currentTimeMillis() / 1000);
            }
        }
        if (endTime != null && status == null) {
            flashSaleQueryWrapper.lt("fs.end_time", endTime);
        }
        if (recommend != null) {
            flashSaleQueryWrapper.eq("fs.recommend", recommend);
        }

        if (!StringUtils.isEmpty(title)) {
            flashSaleQueryWrapper.like("fs.title", title);
        }
        flashSaleQueryWrapper.eq("fs.is_del", 0);
        flashSaleQueryWrapper.orderByDesc("fs.id");
        return flashSaleService.selectFlashSaleGoodsPage(
                new Page<>(page, size), flashSaleQueryWrapper);
    }

    @ApiOperation(value = "更新活动是否推荐")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动id", required = true, paramType = "query"),
            @ApiImplicitParam(
                    name = "recommend",
                    value = "是否推荐：1是，0否",
                    required = true,
                    paramType = "query")
    })
    @PutMapping("recommend")
    public SBApi recommend(@RequestBody FlashSale flashSale) {
        flashSaleService.update(
                new UpdateWrapper<FlashSale>()
                        .set("recommend", flashSale.getRecommend())
                        .eq("id", flashSale.getId()));
        return new SBApi();
    }

    @ApiOperation(value = "活动审核")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "活动id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "status", value = "审核状态：1通过，2拒绝", required = true, paramType = "query")})
    @PutMapping("status")
    public SBApi status(@RequestBody FlashSale flashSale) {
        flashSaleService.updateFlashSaleStatus(flashSale);
        return new SBApi();
    }

    @ApiOperation(value = "删除活动")
    @ApiImplicitParam(name = "prom_id", value = "活动id", required = true, paramType = "path")
    @DeleteMapping("{prom_id}")
    public SBApi remove(@PathVariable("prom_id") Integer promId) {
        flashSaleService.removeFlashSale(promId);
        return new SBApi();
    }

    @ApiOperation(value = "关闭活动")
    @ApiImplicitParam(name = "prom_id", value = "活动id", required = true, paramType = "path")
    @PutMapping("close_prom/{prom_id}")
    public SBApi close(@PathVariable("prom_id") Integer promId) {
        flashSaleService.closeProm(promId);
        return new SBApi();
    }

    @GetMapping("/{prom_id}")
    public FlashSale flashSale(@PathVariable("prom_id") Integer promId) {
        return flashSaleService.getById(promId);
    }

    @ApiOperation(value = "新增活动")
    @PostMapping
    public SBApi addFlashSale(@Valid @RequestBody FlashSale flashSale) {
        flashSaleService.saveFlashSale(flashSale);
        return new SBApi();
    }

    @ApiOperation(value = "编辑活动")
    @PutMapping
    public SBApi updateFlashSale(@Valid @RequestBody FlashSale flashSale) {
        flashSaleService.updateFlashSale(flashSale);
        return new SBApi();
    }

    @GetMapping("count")
    public Integer getCount(@RequestParam(value = "status", required = false) Integer status,
                            @RequestParam(value = "is_end", required = false) Integer isEnd) {
        QueryWrapper<FlashSale> wrapper = new QueryWrapper<>();
        if (status != null) {
            wrapper.eq("status", status);
        }
        if (isEnd != null) {
            wrapper.eq("is_end", isEnd);
        }
        return flashSaleService.count(wrapper);
    }
}
