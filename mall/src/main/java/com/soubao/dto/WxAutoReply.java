package com.soubao.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("微信公众号自动回复对象")
public class WxAutoReply {
    private Integer isAddFriendReplyOpen;   //关注后自动回复是否开启
    private Integer isAutoReplyOpen;    //消息自动回复是否开启
    private String addFriendAutoReplyType;  //关注时自动回复类型
    private String addFriendAutoReplyContent;   //关注时自动回复内容
    private String messageDefaultAutoReplyType; //消息默认回复类型
    private String messageDefaultAutoReplyContent;  //消息默认回复内容
    private List<WxKeywordAutoReply> keywordAutoReplyList;    //关键词自动回复集合

}
