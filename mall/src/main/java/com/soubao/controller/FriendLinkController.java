package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.FriendLink;
import com.soubao.service.FriendLinkService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "友情链接接口")
@RestController
@RequestMapping("/friend_link")
public class FriendLinkController {
    @Autowired
    private FriendLinkService friendLinkService;

    @ApiOperation("分页")
    @GetMapping("page")
    public IPage<FriendLink> page(
            @ApiParam(name = "page", value = "页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @ApiParam(name = "size", value = "单页记录数") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return friendLinkService.page(new Page<>(page, size), new QueryWrapper<FriendLink>().orderByAsc("orderby"));
    }

    @GetMapping
    @ApiOperation("获取一条友情链接")
    public FriendLink getOne(@ApiParam("友情链接Id") @RequestParam("link_id") Integer linkId) {
        return friendLinkService.getById(linkId);
    }

    @PostMapping
    @ApiOperation("添加友情链接")
    public SBApi save(@ApiParam("新增的友情链接对象") @Valid @RequestBody FriendLink friendLink) {
        friendLinkService.save(friendLink);
        return new SBApi();
    }

    @PutMapping
    @ApiOperation("更新友情链接")
    public SBApi update(@ApiParam("更新后的友情链接对象") @Valid @RequestBody FriendLink friendLink) {
        friendLinkService.updateById(friendLink);
        return new SBApi();
    }

    @DeleteMapping
    @ApiOperation("删除友情链接")
    public SBApi remove(@ApiParam("要删除的友情链接id") @RequestParam("link_id") Integer linkId) {
        friendLinkService.removeById(linkId);
        return new SBApi();
    }

}
