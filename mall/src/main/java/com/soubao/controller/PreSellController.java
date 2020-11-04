package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.PreSell;
import com.soubao.service.PreSellService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author dyr
 * @since 2019-10-24
 */
@RestController
@RequestMapping("pre_sell")
public class PreSellController {
    @Autowired private PreSellService preSellService;

    @GetMapping("{id}")
    public PreSell preSell(@PathVariable(value = "id") Integer id) {
        PreSell preSell = preSellService.getById(id);
        return preSell;
    }

    @ApiOperation(value = "预售活动列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "title", value = "活动标题", paramType = "query"),
        @ApiImplicitParam(name = "status", value = "活动状态", paramType = "query"),
        @ApiImplicitParam(name = "page", value = "当前页码", defaultValue = "1", paramType = "query"),
        @ApiImplicitParam(name = "size", value = "每页显示条数", defaultValue = "10", paramType = "query")
    })
    @GetMapping("page")
    public IPage<PreSell> preSells(
            @RequestParam(value = "store_id", required = false) Integer storeId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<PreSell> wrapper = new QueryWrapper<>();
        if (storeId != null) {
            wrapper.eq("store_id", storeId);
        }
        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("pre_sell_id");
        return preSellService.page(new Page<>(page, size), wrapper);
    }

    @ApiOperation(value = "活动审核")
    @ApiImplicitParams({
        @ApiImplicitParam(
                name = "pre_sell_id",
                value = "活动id",
                required = true,
                paramType = "query"),
        @ApiImplicitParam(
                name = "status",
                value = "审核状态：1通过，2拒绝",
                required = true,
                paramType = "query")
    })
    @PutMapping("status")
    public SBApi status(@RequestBody PreSell preSell) {
        preSellService.updatePreSellStatus(preSell);
        return new SBApi();
    }

    @ApiOperation(value = "关闭活动")
    @ApiImplicitParam(name = "prom_id", value = "活动id", required = true, paramType = "path")
    @PutMapping("close_prom/{prom_id}")
    public SBApi close(@PathVariable("prom_id") Integer promId, SBApi sbApi) {
        preSellService.closeProm(promId);
        return sbApi;
    }

    @PostMapping
    public SBApi addPreSell(@Valid @RequestBody PreSell preSell) {
        preSellService.addPreSell(preSell);
        return new SBApi();
    }

    @PutMapping("is_finished")
    public SBApi isFinished(@RequestBody PreSell preSell, SBApi sbApi) {
        preSellService.isFinished(preSell);
        return sbApi;
    }

    @PutMapping("delete")
    public SBApi delPreSell(@RequestBody PreSell preSell, SBApi sbApi) {
        preSellService.deletePreSell(preSell);
        return sbApi;
    }

    @PutMapping
    public SBApi updatePreSell(@Valid @RequestBody PreSell preSell, SBApi sbApi) {
        preSellService.updatePreSell(preSell);
        return sbApi;
    }

    @GetMapping("count")
    public Integer getCount(@RequestParam(value = "status", required = false) Integer status) {
        QueryWrapper<PreSell> wrapper = new QueryWrapper<>();
        if (status != null) {
            wrapper.eq("status", status);
        }
        return preSellService.count(wrapper);
    }
}
