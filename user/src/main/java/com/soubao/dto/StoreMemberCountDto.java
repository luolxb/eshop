package com.soubao.dto;

import lombok.Data;

@Data
public class StoreMemberCountDto {
    private Integer storeId;   //店铺id
    private Integer memberCount; //会员数
}
