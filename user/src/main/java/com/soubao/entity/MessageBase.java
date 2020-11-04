package com.soubao.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel("消息")
public class MessageBase {
    @ApiModelProperty("标题")
    protected String messageTitle;
    @ApiModelProperty("内容")
    protected String messageContent;
    @ApiModelProperty("发送时间")
    protected Long sendTime;
}
