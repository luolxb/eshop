package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.WxMenu;
import com.soubao.entity.WxUser;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.WxMenuService;
import com.soubao.service.WxUserService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/wx_menu")
public class WxMenuController {
    @Autowired
    private WxMenuService wxMenuService;
    @Autowired
    private WxUserService wxUserService;

    @ApiOperation("获取菜单-树形结构")
    @GetMapping("tree")
    public List<WxMenu> menu() {
        WxUser wxUser = wxUserService.getOne(new QueryWrapper<WxUser>().last("limit 1"));
        if (wxUser == null) {
            throw new ShopException(ResultEnum.NOT_CONFIGURED_WX);
        }
        return wxMenuService.listToTree(wxMenuService.list());
    }

    @ApiOperation("添加菜单")
    @PostMapping
    public SBApi add(@Valid @RequestBody WxMenu wxMenu) {
        wxMenuService.addMenu(wxMenu);
        return new SBApi();
    }

    @ApiOperation("更新菜单")
    @PutMapping
    public SBApi update(@Valid @RequestBody WxMenu wxMenu) {
        wxMenuService.updateById(wxMenu);
        return new SBApi();
    }

    @ApiOperation("删除菜单")
    @DeleteMapping
    public SBApi remove(@RequestParam("id") Integer id) {
        wxMenuService.remove(new QueryWrapper<WxMenu>().eq("id", id).or().eq("pid", id));
        return new SBApi();
    }

    @ApiOperation("生成微信菜单")
    @PutMapping("push")
    public SBApi pushMenu(){
        wxMenuService.pushMenu();
        return new SBApi();
    }


}
