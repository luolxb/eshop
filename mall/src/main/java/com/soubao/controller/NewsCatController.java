package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.soubao.entity.NewsCat;
import com.soubao.service.NewsCatService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "新闻分类接口")
@RestController
@RequestMapping("/news_cat")
public class NewsCatController {
    @Autowired
    private NewsCatService newsCatService;

    @ApiOperation("新闻分类列表")
    @GetMapping("list")
    public List<NewsCat> list() {
        return newsCatService.list();
    }

    @ApiOperation("更新新闻分类排序值")
    @PutMapping("sort_order")
    public SBApi updateSortOrder(@ApiParam("新闻分类id") @RequestParam("cat_id") Integer catId,
                                 @ApiParam("排序值") @RequestParam("sort_order") Integer sortOrder) {
        newsCatService.update(new UpdateWrapper<NewsCat>().set("sort_order", sortOrder).eq("cat_id", catId));
        return new SBApi();
    }

    @ApiOperation("获取一个新闻分类")
    @GetMapping
    public NewsCat getOne(@ApiParam("新闻分类id") @RequestParam("cat_id") Integer catId) {
        return newsCatService.getById(catId);
    }

    @ApiOperation("添加新闻分类")
    @PostMapping
    public SBApi save(@ApiParam("新增的新闻分类对象") @Valid @RequestBody NewsCat newsCat) {
        newsCatService.saveNewsCat(newsCat);
        return new SBApi();
    }

    @ApiOperation("更新新闻分类")
    @PutMapping
    public SBApi update(@ApiParam("更新后的新闻分类对象") @Valid @RequestBody NewsCat newsCat) {
        newsCatService.updateNewsCat(newsCat);
        return new SBApi();
    }

    @ApiOperation("删除新闻分类")
    @DeleteMapping
    public SBApi update(@ApiParam("新闻分类id") @RequestParam("cat_id") Integer catId) {
        newsCatService.removeNewsCat(catId);
        return new SBApi();
    }

}
