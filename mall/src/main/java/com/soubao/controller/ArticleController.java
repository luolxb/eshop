package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Article;
import com.soubao.entity.ArticleCat;
import com.soubao.common.vo.SBApi;
import com.soubao.service.ArticleCatService;
import com.soubao.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "文章接口")
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleCatService articleCatService;

    @ApiOperation("文章分页")
    @GetMapping("page")
    public IPage<Article> page(
            @ApiParam("文章类别id") @RequestParam(value = "cat_id", required = false) Integer catId,
            @ApiParam("文章标题模糊搜索") @RequestParam(value = "title", required = false) String title,
            @ApiParam(name = "page", value = "页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @ApiParam(name = "size", value = "单页记录数") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<Article> articleIPage = articleService.page(new Page<>(page, size), catId, title);
        articleService.withArticleCat(articleIPage.getRecords());
        return articleIPage;
    }

    @ApiOperation("文章目录")
    @GetMapping("directory")
    public List<ArticleCat> getList(
            @ApiParam("默认分组") @RequestParam(value = "cat_type", defaultValue = "1") Integer catType) {
        List<ArticleCat> articleCatList = articleCatService.list(new QueryWrapper<ArticleCat>().eq("cat_type", catType));
        articleCatService.withArticle(articleCatList, new QueryWrapper<Article>().select("article_id,title,link,cat_id,is_open").eq("is_open", 1));
        return articleCatList;
    }

    @GetMapping
    @ApiOperation("获取一篇文章")
    public Article getOne(@ApiParam("文章Id") @RequestParam("article_id") Integer articleId) {
        return articleService.getById(articleId);
    }

    @PostMapping
    @ApiOperation("添加文章")
    public SBApi save(@ApiParam("添加的文章对象") @Valid @RequestBody Article article) {
        articleService.saveArticle(article);
        return new SBApi();
    }

    @PutMapping
    @ApiOperation("更新文章")
    public SBApi update(@ApiParam("更新后的文章对象") @Valid @RequestBody Article article) {
        articleService.updateById(article);
        return new SBApi();
    }

    @DeleteMapping
    @ApiOperation("删除文章")
    public SBApi removeBatch(
            @ApiParam("要删除的文章id") @RequestParam("article_id") Integer articleId) {
        articleService.removeById(articleId);
        return new SBApi();
    }

    @PutMapping("click")
    @ApiOperation("点击文章，增加文章的点击量")
    public SBApi click(@ApiParam("新闻id") @RequestParam("article_id") Integer articleId) {
        articleService.update(new UpdateWrapper<Article>().setSql("click=click+1").eq("article_id", articleId));
        return new SBApi();
    }

    @GetMapping("count")
    @ApiOperation("文章总数")
    public Integer getCount() {
        return articleService.count();
    }
}
