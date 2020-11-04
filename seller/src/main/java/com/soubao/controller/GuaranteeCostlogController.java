package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.GuaranteeCostlog;
import com.soubao.service.GuaranteeCostlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 店铺消费者保障服务保证金日志表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-03-11
 */
@RestController
@RequestMapping("/guarantee_costlog")
public class GuaranteeCostlogController {
    @Autowired
    private GuaranteeCostlogService guaranteeCostlogService;

    @GetMapping("page")
    public IPage<GuaranteeCostlog> page(@RequestParam("store_id") Integer storeId,
                                        @RequestParam("grt_id") Integer grtId,
                                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return guaranteeCostlogService.page(new Page<>(page, size), new QueryWrapper<GuaranteeCostlog>()
                .eq("store_id", storeId).eq("grt_id", grtId));
    }

}
