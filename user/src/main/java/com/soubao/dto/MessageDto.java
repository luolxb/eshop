package com.soubao.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Set;

@Data
@ApiModel("发送站内信参数对象")
public class MessageDto {
    private String title;   //标题
    private String content; //内容
    private Integer type;   //类型：个体/全体
    private Set<Integer> userIds;  //接受用户id集合
    private Integer storeId = 0;    //发送店铺id，为0时为总平台管理员
}
