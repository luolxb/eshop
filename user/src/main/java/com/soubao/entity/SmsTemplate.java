package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.*;

import com.soubao.common.constant.SendSceneMap;
import com.soubao.common.utils.TimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sms_template")
@ApiModel("短信模板配置对象")
public class SmsTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增ID")
    @TableId(value = "tpl_id", type = IdType.AUTO)
    private Integer tplId;

    @ApiModelProperty("短信签名")
    @NotBlank(message = "短信签名不能为空")
    private String smsSign;

    @ApiModelProperty("短信模板ID")
    @NotBlank(message = "短信模板ID不能为空")
    private String smsTplCode;

    @ApiModelProperty("发送短信内容")
    @NotBlank(message = "短信内容不能为空")
    private String tplContent;

    @ApiModelProperty("短信发送场景")
    @NotBlank(message = "发送场景不能为空")
    private String sendScene;

    @ApiModelProperty("添加时间")
    private Long addTime;

    @TableField(exist = false)
    private String addTimeShow;

    public String getStartTimeShow() {
        return TimeUtil.transForDateStr(addTime, "yyyy-MM-dd HH:mm:ss");
    }

    @TableField(exist = false)
    private String sendSceneDetail;

    public String getSendSceneDetail() {
        if(StringUtils.isNotEmpty(sendScene)){
            return SendSceneMap.getSendScenes().get(sendScene);
        }
        return "";
    }

    @TableField(exist = false)
    private Map<String,String> sendSceneMap;

}
