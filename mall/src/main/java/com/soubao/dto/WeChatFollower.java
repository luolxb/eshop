package com.soubao.dto;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("微信公众号关注者信息对象")
public class WeChatFollower {

    private Integer total;//关注该公众账号的总用户数

    private Integer count;//拉取的OPENID个数，最大值为10000

    private JSONArray openIds;//列表数据，关注者OPENID的列表

    private String nextOpenID;//拉取列表的最后一个用户的OPENID

    //公众号关注者集合
    private List<WeChatUser> weChatFollowerUsers;
}
