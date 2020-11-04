package com.soubao.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MiniappConfig {
    private String appId;
    private String mchId;
    private String key;
    private String appSecret;
    private String apiclientCert;
    private String apiclientKey;
}
