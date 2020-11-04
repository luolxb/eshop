package com.soubao.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("微信用户对象")
public class WeChatUser {
    private String subscribe;   // 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
    private String openid;      // 用户的标识，对当前公众号唯一
    private String nickname;    // 用户的昵称
    private String sex;         // 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    private String city;        // 用户所在城市
    private String country;     // 用户所在国家
    private String province;    // 用户所在省份
    private String language;    // 用户的语言，简体中文为zh_CN
    private List<String> tagIds;// 用户被打上的标签ID列表
}
