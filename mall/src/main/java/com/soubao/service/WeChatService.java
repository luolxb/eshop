package com.soubao.service;

import com.soubao.dto.WeChatFollower;
import com.soubao.dto.WeChatUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface WeChatService {

    /**
     * 处理微信请求
     * @param request
     * @return
     */
    String processRequest(HttpServletRequest request);

    /**
     * 获取公众号粉丝openids
     * @return
     */
    WeChatFollower getWeChatFollower();

    /**
     * 获取众号粉丝用户信息
     * @param openIdsPage
     * @return
     */
    List<WeChatUser> getWeChatFollowersUserInfo(List<Object> openIdsPage);
}
