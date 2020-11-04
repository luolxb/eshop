package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.News;
import com.soubao.service.NewsService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@Api(tags = "新闻接口")
@RestController
@RequestMapping("news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping("page")
    @ApiOperation("新闻分页")
    public IPage<News> page(@ApiParam("发布者id") @RequestParam(value = "user_id", required = false) Integer userId,
                            @ApiParam("新闻类别id") @RequestParam(value = "cat_id", required = false) Integer catId,
                            @ApiParam("新闻标题模糊搜索") @RequestParam(value = "title", required = false) String title,
                            @ApiParam(name = "page", value = "页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
                            @ApiParam(name = "size", value = "单页记录数") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<News> newsIPage = newsService.page(new Page<>(page, size), userId, catId, title);
        newsService.withNewsCat(newsIPage.getRecords());
        newsService.withSource(newsIPage.getRecords());
        return newsIPage;
    }

    @GetMapping
    @ApiOperation("获取一条新闻")
    public News getOne(@ApiParam("新闻Id") @RequestParam("article_id") Integer articleId) {
        return newsService.getById(articleId);
    }

    @PostMapping
    @ApiOperation("添加新闻")
    public SBApi save(@ApiParam("新增的新闻对象")@Valid @RequestBody News news) {
        newsService.saveNews(news);
        return new SBApi();
    }

    @PutMapping
    @ApiOperation("更新新闻")
    public SBApi update(@ApiParam("更新后的新闻对象")@Valid @RequestBody News news) {
        newsService.updateById(news);
        return new SBApi();
    }

    @DeleteMapping
    @ApiOperation("批量删除新闻")
    public SBApi removeBatch(@ApiParam("要删除的新闻id集合") @RequestParam("ids") Set<Integer> articleIds) {
        newsService.removeByIds(articleIds);
        return new SBApi();
    }

    @DeleteMapping("seller")
    @ApiOperation("商家删除新闻")
    public SBApi removeBySeller(@ApiParam("要删除的商家新闻id") @RequestParam("article_id") Integer articleId,
                                  @ApiParam("执行删除操作的商家id") @RequestParam("user_id") Integer userId) {
        newsService.remove(new QueryWrapper<News>().eq("user_id", userId).eq("article_id", articleId));
        return new SBApi();
    }

    @PutMapping("click")
    @ApiOperation("点击新闻，增加新闻的点击量")
    public SBApi click(@ApiParam("新闻id") @RequestParam("article_id") Integer articleId) {
        newsService.update(new UpdateWrapper<News>().setSql("click=click+1").eq("article_id", articleId));
        return new SBApi();
    }
}
