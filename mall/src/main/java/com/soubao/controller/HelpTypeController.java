package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.soubao.entity.HelpType;
import com.soubao.service.HelpTypeService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "帮助类型接口")
@RestController
@RequestMapping("/help_type")
public class HelpTypeController {
    @Autowired
    private HelpTypeService helpTypeService;

    @ApiOperation("获取帮助顶级分类列表")
    @GetMapping("list/top_level")
    public List<HelpType> list() {
        return helpTypeService.list(new QueryWrapper<HelpType>().eq("help_show", 1).eq("level", 0));
    }

    @ApiOperation("获取树状结构帮助分类")
    @GetMapping("tree")
    public List<HelpType> tree() {
        return helpTypeService.listToTree(helpTypeService.list());
    }

    @ApiOperation("更新帮助分类排序值")
    @PutMapping("sort_order")
    public SBApi updateSortOrder(@ApiParam("帮助分类id") @RequestParam("type_id") Integer typeId,
                                 @ApiParam("排序值") @RequestParam("sort_order") Integer sortOrder) {
        helpTypeService.update(new UpdateWrapper<HelpType>().set("sort_order", sortOrder).eq("type_id", typeId));
        return new SBApi();
    }

    @ApiOperation("获取一个帮助分类")
    @GetMapping
    public HelpType getOne(@ApiParam("帮助分类id") @RequestParam("type_id") Integer typeId) {
        return helpTypeService.getById(typeId);
    }

    @ApiOperation("添加帮助分类")
    @PostMapping
    public SBApi save(@ApiParam("新增的帮助分类对象") @Valid @RequestBody HelpType helpType) {
        helpTypeService.saveHelpType(helpType);
        return new SBApi();
    }

    @ApiOperation("更新帮助分类")
    @PutMapping
    public SBApi update(@ApiParam("更新后的帮助分类对象") @Valid @RequestBody HelpType helpType) {
        helpTypeService.updateHelpType(helpType);
        return new SBApi();
    }

    @ApiOperation("删除帮助分类")
    @DeleteMapping
    public SBApi remove(@ApiParam("删除的帮助分类id") @RequestParam("type_id") Integer typeId) {
        helpTypeService.removeHelpType(typeId);
        return new SBApi();
    }

}
