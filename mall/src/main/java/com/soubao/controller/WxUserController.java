package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.WxUser;
import com.soubao.service.WxUserService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/wx_user")
@Api(value = "微信公众号控制器", tags = {"微信公众号相关控制器接口"})
public class WxUserController {
    @Autowired
    private WxUserService wxUserService;

    @ApiOperation("获取微信公众号配置")
    @GetMapping
    public WxUser getWxUser(){
        return wxUserService.getOne(new QueryWrapper<WxUser>().last("limit 1"));
    }

    @ApiOperation("保存微信公众号配置")
    @PostMapping
    public SBApi save(@RequestBody WxUser wxUser) {
        wxUser.setCreateTime(System.currentTimeMillis() / 1000);
        wxUser.setApiurl("/mall/wechat");//更新apiurl
        wxUserService.save(wxUser);
        return new SBApi();
    }

    @ApiOperation("更新微信公众号配置")
    @PutMapping
    public SBApi update(@RequestBody WxUser wxUser) {
        wxUser.setWebExpires(0L);//更新过期时间
        wxUserService.updateById(wxUser);
        return new SBApi();
    }
}
