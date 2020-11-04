package com.soubao.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("微信公众号菜单对象")
public class MenuButtonDto {
    private String type;   //菜单类型
    private String name; //菜单名称
    @JSONField(name="sub_button")
    private List<MenuButtonDto> subButton;  //子菜单集合
    private String key;
    private String url;
    private String pagepath; //小程序页面地址
    private String appid;   //小程序appid
}
