package com.soubao.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("微信公众号关键字自动回复对象")
public class WxKeywordAutoReply {
    private String ruleName;    //规则名称（自动回复类型）：autoreply-video\autoreply-text\autoreply-voice\autoreply-news
    private Long createTime;    //创建时间
    private String replyMode;   //回复模式，reply_all代表全部回复，random_one代表随机回复其中一条
    private List<WxKeyWord> keywordList; //匹配的关键词集合
    private List<WxReply> replyList;   //回复的内容集合
}
