package com.soubao.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@ApiModel(value = "意见反馈", description = "feedback表")
public class FeedbackRq implements Serializable {

    @ApiModelProperty(value = "意见反馈id")
    private Integer feedbackId;

    @ApiModelProperty(value = "反馈内容")
    @NotBlank(message = "反馈内容不能为空")
    private String content;

    @ApiModelProperty(value = "图片1")
    private String pic1Url;

    @ApiModelProperty(value = "图片2")
    private String pic2Url;

    @ApiModelProperty(value = "图片3")
    private String pic3Url;

    @ApiModelProperty(value = "图片4")
    private String pic4Url;

    @ApiModelProperty(value = "反馈人")
    private Integer userId;

    @ApiModelProperty(value = "创建时间/反馈时间")
    private Date createTime;

    @ApiModelProperty(value = "回复")
    private String reply;

    @ApiModelProperty(value = "回复时间")
    private Date replyTime;


}
