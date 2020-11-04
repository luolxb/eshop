package com.soubao.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("微信公众号图文消息对象")
public class ArticleItem {
//    private String Title;
//    private String Description;
//    private String PicUrl;
//    private String Url;

    private String title;
    private String description;
    private String picUrl;
    private String url;
}
