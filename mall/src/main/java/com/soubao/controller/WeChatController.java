package com.soubao.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.dto.WeChatFollower;
import com.soubao.dto.WeChatUser;
import com.soubao.entity.WxUser;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.WeChatService;
import com.soubao.service.WxUserService;
import com.soubao.utils.WeChatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/wechat")
@Api(value = "微信控制器", tags = {"微信相关控制器接口"})
public class WeChatController {
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private WeChatService weChatService;

    /**
     * 处理微信服务器发来的get请求，进行签名的验证
     * @param signature 微信端发来的签名
     * @param timestamp 微信端发来的时间戳
     * @param nonce 微信端发来的随机字符串
     * @param echostr 微信端发来的验证字符串
     * @return
     */
    @ApiOperation("微信公众号服务器验证")
    @GetMapping
    public String index(@RequestParam("signature")String signature,
                        @RequestParam("timestamp")String timestamp,
                        @RequestParam("nonce")String nonce,
                        @RequestParam("echostr")String echostr) {
        WxUser wxUser = wxUserService.getOne(new QueryWrapper<WxUser>().last("limit 1"));
        if (wxUser == null) {
            throw new ShopException(ResultEnum.NOT_CONFIGURED_WX);
        }
        String mutualToken = wxUser.getMutualToken();
        boolean check = WeChatUtil.checkSignature(mutualToken, signature, timestamp, nonce);
        return check ? echostr : null;
    }

    /**
     * 此处是处理微信服务器的消息转发的
     * @param request
     * @return
     */
    @ApiOperation("处理接收推送消息")
    @PostMapping
    public String processMsg(HttpServletRequest request) {
        // 调用核心服务类接收处理请求
        return weChatService.processRequest(request);
    }

    /**
     * 获取粉丝列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("page/follower")
    public WeChatFollower getFollowerPage(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                          @RequestParam(value = "size", defaultValue = "10")Integer size){
        WeChatFollower weChatFollower = weChatService.getWeChatFollower();
        JSONArray openIds = weChatFollower.getOpenIds();
        if (!openIds.isEmpty()){
            int offset = (page - 1) * size;
            int toIndex = offset + size > weChatFollower.getTotal() ? weChatFollower.getTotal() : offset + size;
            System.out.println("toIndex============="+toIndex);
            List<Object> openIdsPage = openIds.subList(offset, toIndex);
            List<WeChatUser> weChatFollowerUsers = weChatService.getWeChatFollowersUserInfo(openIdsPage);
            weChatFollower.setWeChatFollowerUsers(weChatFollowerUsers);
        }
        System.out.println("===============");
        System.out.println(weChatFollower);
        return weChatFollower;
    }

}
