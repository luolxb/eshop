package com.soubao.dto;

import lombok.Data;

@Data
public class StoreMemberIdsDto {
    private Integer storeId;   //店铺id
    private Integer userId; //店铺会员ID集合
}
