package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.NewsComment;
import com.soubao.service.NewsCommentService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "新闻评论接口")
@RestController
@RequestMapping("/news_comment")
public class NewsCommentController {
    @Autowired
    private NewsCommentService newsCommentService;

    @ApiOperation("新闻评论分页")
    @GetMapping("page")
    public IPage<NewsComment> page(
            @ApiParam("新闻id") @RequestParam(value = "article_id", required = false) Integer article_id,
            @ApiParam(name = "page", value = "页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @ApiParam(name = "size", value = "单页记录数") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<NewsComment> newsCommentIPage = newsCommentService.page(new Page<>(page, size), new QueryWrapper<NewsComment>()
                .eq("article_id", article_id));
        newsCommentService.withUser(newsCommentIPage.getRecords());
        return newsCommentIPage;
    }

    @ApiOperation("审核评论")
    @PutMapping("check")
    public SBApi checkComment(@ApiParam("评论id") @RequestParam("comment_id") Integer commentId,
                              @ApiParam("审核状态") @RequestParam("check_type") Integer checkType) {
        newsCommentService.update(new UpdateWrapper<NewsComment>().set("check_type", checkType).eq("comment_id", commentId));
        return new SBApi();
    }

}
