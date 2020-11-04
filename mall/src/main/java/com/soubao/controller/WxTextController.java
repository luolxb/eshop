package com.soubao.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.WxText;
import com.soubao.service.WxTextService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "文本素材接口", tags = {"文本素材相关接口"})
@RestController
@RequestMapping("/wx_text")
public class WxTextController {
    @Autowired
    private WxTextService wxTextService;

    @GetMapping
    public WxText getWxText(@ApiParam("文本素材id") @RequestParam("id")Integer id){
        return wxTextService.getById(id);
    }

    @GetMapping("page")
    public IPage<WxText> page(@ApiParam("页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
                              @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "10") Integer size){
        return wxTextService.page(new Page<>(page, size));
    }

    @PostMapping
    public SBApi save(@Valid @RequestBody WxText wxText){
        wxText.setUpdateTime(System.currentTimeMillis() / 1000);
        wxTextService.save(wxText);
        return new SBApi();
    }
    @PutMapping
    public SBApi update(@Valid @RequestBody WxText wxText){
        wxText.setUpdateTime(System.currentTimeMillis() / 1000);
        wxTextService.updateById(wxText);
        return new SBApi();
    }

    @DeleteMapping
    public SBApi remove(@RequestParam("id") Integer id){
        wxTextService.removeById(id);
        return new SBApi();
    }

}
