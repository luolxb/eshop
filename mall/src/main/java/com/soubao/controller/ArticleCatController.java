package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.soubao.entity.ArticleCat;
import com.soubao.service.ArticleCatService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author UI
 */
@Api(tags = "文章分类接口")
@RestController
@RequestMapping("/article_cat")
public class ArticleCatController {
    @Autowired
    private ArticleCatService articleCatService;

    @ApiOperation("获取树状结构文章分类")
    @GetMapping("tree")
    public List<ArticleCat> tree() {
        return articleCatService.listToTree(articleCatService.list(new QueryWrapper<ArticleCat>()
                .orderByAsc("parent_id", "sort_order")));
    }

    @ApiOperation("更新文章分类排序值")
    @PutMapping("sort_order")
    public SBApi updateSortOrder(@ApiParam("文章分类id") @RequestParam("cat_id") Integer catId,
                                 @ApiParam("排序值") @RequestParam("sort_order") Integer sortOrder) {
        articleCatService.update(new UpdateWrapper<ArticleCat>().set("sort_order", sortOrder).eq("cat_id", catId));
        return new SBApi();
    }

    @ApiOperation("获取一个文章分类")
    @GetMapping
    public ArticleCat getOne(@ApiParam("文章分类id") @RequestParam("cat_id") Integer catId) {
        return articleCatService.getById(catId);
    }

    @ApiOperation("添加文章分类")
    @PostMapping
    public SBApi save(@ApiParam("新增的文章分类对象") @Valid @RequestBody ArticleCat articleCat) {
        articleCatService.saveArticleCat(articleCat);
        return new SBApi();
    }

    @ApiOperation("更新文章分类")
    @PutMapping
    public SBApi update(@ApiParam("更新后的文章分类对象") @Valid @RequestBody ArticleCat articleCat) {
        articleCatService.updateArticleCat(articleCat);
        return new SBApi();
    }

    @ApiOperation("删除文章分类")
    @DeleteMapping
    public SBApi remove(@ApiParam("删除的文章分类id") @RequestParam("cat_id") Integer catId) {
        articleCatService.removeArticleCat(catId);
        return new SBApi();
    }

}
