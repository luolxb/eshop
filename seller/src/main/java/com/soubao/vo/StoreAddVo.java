package com.soubao.vo;

import lombok.Data;

@Data
public class StoreAddVo {
    private String storeName;
    private String mobile;
    private String email;
    private String sellerName;
    private String password;
    private Integer isOwnShop;
}
