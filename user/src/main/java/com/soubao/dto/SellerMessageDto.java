package com.soubao.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Set;

@Data
@ApiModel("商家发送站内信")
public class SellerMessageDto {
    private String title;
    private String content;
    private Integer type;
    private Set<Integer> userIds;

}
