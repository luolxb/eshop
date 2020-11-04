package com.soubao.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.WxUser;

public interface WxUserService extends IService<WxUser> {

    /**
     * 微信响应结果处理
     * @param resultJson
     * @return
     */
    JSONObject resultHandle(String resultJson);

    /**
     * 获取微信access_token
     * @return access_token
     */
    String getAccessToken();

}
