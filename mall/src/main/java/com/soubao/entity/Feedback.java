package com.soubao.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@ApiModel(value = "意见反馈", description = "feedback表")
@TableName("feedback")
public class Feedback  implements Serializable {

    @Id
    @ApiModelProperty(value = "意见反馈id")
    @TableId(value = "feedback_id", type = IdType.AUTO)
    @JsonProperty("feedback_id")
    private Integer feedbackId;

    @ApiModelProperty(value = "反馈内容")
    @JsonProperty("content")
    private String content;

    @ApiModelProperty(value = "图片1")
    @JsonProperty("pic1_url")
    private String pic1Url;

    @ApiModelProperty(value = "图片2")
    @JsonProperty("pic2_url")
    private String pic2Url;

    @ApiModelProperty(value = "图片3")
    @JsonProperty("pic3_url")
    private String pic3Url;

    @ApiModelProperty(value = "图片4")
    @JsonProperty("pic4_url")
    private String pic4Url;

    @ApiModelProperty(value = "反馈人")
    @JsonProperty("user_id")
    private Integer userId;

    @ApiModelProperty(value = "创建时间/反馈时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "回复")
    @JsonProperty("reply")
    private String reply;

    @ApiModelProperty(value = "回复时间")
    @JsonProperty("reply_time")
    private Date replyTime;


}
