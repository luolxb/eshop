package com.soubao.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.WxNews;
import com.soubao.service.WxNewsService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "图文素材接口", tags = {"图文素材相关接口"})
@RestController
@RequestMapping("/wx_news")
public class WxNewsController {
    @Autowired
    private WxNewsService wxNewsService;

    @GetMapping
    public WxNews getWxText(@ApiParam("图文素材id") @RequestParam("id")Integer id){
        return wxNewsService.getById(id);
    }

    @GetMapping("page")
    public IPage<WxNews> page(@ApiParam("页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
                              @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "10") Integer size){
        return wxNewsService.page(new Page<>(page, size));
    }

    @PostMapping
    public SBApi save(@Valid @RequestBody WxNews wxNews){
        wxNews.setUpdateTime(System.currentTimeMillis() / 1000);
        wxNewsService.save(wxNews);
        return new SBApi();
    }
    @PutMapping
    public SBApi update(@Valid @RequestBody WxNews wxNews){
        wxNews.setUpdateTime(System.currentTimeMillis() / 1000);
        wxNewsService.updateById(wxNews);
        return new SBApi();
    }

    @DeleteMapping
    public SBApi remove(@RequestParam("id") Integer id){
        wxNewsService.removeById(id);
        return new SBApi();
    }
}
