package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.StockLog;
import com.soubao.service.StockLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-03-24
 */
@RestController
@RequestMapping("stock_log")
public class StockLogController {

    @Autowired
    private StockLogService stockLogService;

    @GetMapping("page")
    public IPage<StockLog> getStockLogPage(@RequestParam(value = "store_id",required = false) Integer storeId,
                                           @RequestParam(value = "start_time", required = false) Long startTime,
                                           @RequestParam(value = "end_time", required = false) Long endTime,
                                           @RequestParam(value = "type", required = false) Integer type,
                                           @RequestParam(value = "goods_name", required = false) String goodsName,
                                           @RequestParam(value = "page", defaultValue = "1") Integer page,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<StockLog> queryWrapper = new QueryWrapper<>();
        if (storeId != null) {
            queryWrapper.eq("store_id", storeId);
        }
        if (startTime != null && endTime != null) {
            queryWrapper.between("ctime", startTime, endTime);
        }
        if (type != null && type == 1) {
            queryWrapper.gt("stock", 0);
        }
        if (type != null && type == -1) {
            queryWrapper.lt("stock", 0);
        }
        if (goodsName != null) {
            queryWrapper.like("goods_name", goodsName);
        }
        queryWrapper.orderByDesc("ctime");
        IPage<StockLog> stockLogIPage = stockLogService.page(new Page<>(page, size), queryWrapper);
        stockLogService.withStore(stockLogIPage.getRecords());
        return stockLogIPage;
    }

    @GetMapping("order_goods/page")
    public IPage<StockLog> getStockLogPage(@RequestParam(value = "start_time") Long startTime,
                                           @RequestParam(value = "end_time") Long endTime,
                                           @RequestParam(value = "store_id", required = false) Integer storeId,
                                           @RequestParam(value = "page", defaultValue = "1") Integer page,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<StockLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("goods_id,any_value(goods_name) as goods_name,sum(stock) as stock");
        queryWrapper.between("ctime", startTime, endTime);
        queryWrapper.ne("order_sn", "''");
        queryWrapper.lt("stock", 0);
        queryWrapper.orderByAsc("sum(stock)").groupBy("goods_id");
        if (null != storeId) {
            queryWrapper.eq("store_id", storeId);
        }
        return stockLogService.page(new Page<>(page, size), queryWrapper);
    }

}
