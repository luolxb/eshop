package com.soubao.controller;


import com.soubao.entity.MenuCfg;
import com.soubao.service.MenuCfgService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-05-14
 */
@Api(value = "个人中心配置控制器", tags = {"个人中心配置相关接口"})
@RestController
@RequestMapping("/menu")
public class MenuCfgController {
    @Autowired
    private MenuCfgService menuCfgService;

    @ApiOperation("个人中心配置")
    @GetMapping
    public List<MenuCfg> getAll() {
        return menuCfgService.list();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping
    public SBApi update(@RequestBody MenuCfg menuCfg) {
        menuCfgService.updateById(menuCfg);
        return new SBApi();
    }
}
