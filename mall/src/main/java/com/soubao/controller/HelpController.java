package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Help;
import com.soubao.service.HelpService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Api(tags = "帮助接口")
@RestController
@RequestMapping("/help")
public class HelpController {
    @Autowired
    private HelpService helpService;

    @GetMapping("page")
    @ApiOperation("帮助分页")
    public IPage<Help> page(@ApiParam("帮助类别id") @RequestParam(value = "type_id", required = false) Integer typeId,
                            @ApiParam("帮助标题模糊搜索") @RequestParam(value = "help_title", required = false) String helpTitle,
                            @ApiParam(name = "page", value = "页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
                            @ApiParam(name = "size", value = "单页记录数") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Help> helpQueryWrapper = new QueryWrapper<>();
        if (typeId != null) {
            helpQueryWrapper.eq("type_id", typeId);
        }
        if (!StringUtils.isEmpty(helpTitle)) {
            helpQueryWrapper.like("help_title", helpTitle);
        }
        helpQueryWrapper.orderByDesc("help_id");
        IPage<Help> helpIPage = helpService.page(new Page<>(page, size), helpQueryWrapper);
        helpService.withHelpType(helpIPage.getRecords());
        return helpIPage;
    }

    @GetMapping
    @ApiOperation("获取一条帮助")
    public Help getOne(@ApiParam("帮助Id") @RequestParam("help_id") Integer helpId) {
        return helpService.getById(helpId);
    }

    @PostMapping
    @ApiOperation("添加帮助")
    public SBApi save(@ApiParam("新增的帮助对象") @Valid @RequestBody Help help) {
        helpService.saveHelp(help);
        return new SBApi();
    }

    @PutMapping
    @ApiOperation("更新帮助")
    public SBApi update(@ApiParam("更新后的帮助对象") @Valid @RequestBody Help help) {
        helpService.updateHelp(help);
        return new SBApi();
    }

    @DeleteMapping
    @ApiOperation("删除帮助")
    public SBApi remove(@ApiParam("帮助Id") @RequestParam("help_id") Integer helpId) {
        helpService.removeHelp(helpId);
        return new SBApi();
    }

}
