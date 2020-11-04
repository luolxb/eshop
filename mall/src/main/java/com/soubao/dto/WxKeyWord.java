package com.soubao.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("微信公众号关键字对象")
public class WxKeyWord {
    private String type;    //自动回复的类型。关注后自动回复和消息自动回复的类型仅支持文本（text）、图片（img）、语音（voice）、视频（video），关键词自动回复则还多了图文消息（news）
    private String matchMode;   //匹配模式，contain代表消息中含有该关键词即可，equal表示消息内容必须和关键词严格相同
    private String content; //对于文本类型，content是文本内容，对于图文、图片、语音、视频类型，content是mediaID
}
