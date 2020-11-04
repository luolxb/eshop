package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.SystemArticle;
import com.soubao.service.SystemArticleService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "系统文章控制器", tags = {"系统文章相关接口"})
@RequestMapping("system_article")
@RestController
public class SystemArticleController {
    @Autowired
    private SystemArticleService systemArticleService;

    @GetMapping("list")
    @ApiOperation("系统文章列表")
    public List<SystemArticle> list() {
        return systemArticleService.list();
    }

    @GetMapping
    @ApiOperation(value = "获取单条系统文章")
    public SystemArticle getSystemArticle(@RequestParam("doc_code") String docCode) {
        return systemArticleService.getOne((new QueryWrapper<SystemArticle>().eq("doc_code", docCode)));
    }

    @PostMapping
    @ApiOperation("添加系统文章")
    public SBApi save(@ApiParam("新增的系统文章对象") @Valid @RequestBody SystemArticle systemArticle) {
        systemArticleService.saveSystemArticle(systemArticle);
        return new SBApi();
    }

    @PutMapping
    @ApiOperation("更新系统文章")
    public SBApi update(@ApiParam("更新后的系统文章对象") @Valid @RequestBody SystemArticle systemArticle) {
        systemArticleService.updateSystemArticle(systemArticle);
        return new SBApi();
    }

}
