package com.soubao.vo;

import lombok.Data;

@Data
public class StoreSlideVO {
    private String[] slides;    //店铺幻灯片集合
    private String[] slideUrls; //店铺幻灯片对应url集合
}
