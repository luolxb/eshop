package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.soubao.validation.annotation.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sms_log")
@ApiModel("短信日志对象")
public class SmsLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("手机号")
    @Phone
    private String mobile;

    @ApiModelProperty("session_id")
    private String sessionId;

    @ApiModelProperty("发送时间")
    private Long addTime;

    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("商家id")
    private Integer sellerId;

    @ApiModelProperty("1:发送成功,0:发送失败")
    private Integer status;

    @ApiModelProperty("短信内容")
    private String msg;

    @ApiModelProperty("发送场景,1:用户注册,2:找回密码,3:客户下单,4:客户支付,5:商家发货,6:身份验证")
    private Integer scene;

    @ApiModelProperty("发送短信异常内容")
    private String errorMsg;


}
