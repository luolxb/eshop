package com.soubao.entity;

import com.soubao.validation.annotation.Phone;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserStore {
    @ApiModelProperty("用户店铺id")
    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("店铺名")
    @NotBlank
    private String storeName;

    @ApiModelProperty("真名")
    @NotBlank
    private String trueName;

    @ApiModelProperty("QQ")
    private String qq;

    @ApiModelProperty("手机号码")
    @Phone
    private String mobile;

    @ApiModelProperty("店铺图片")
    private String storeImg;

    @ApiModelProperty("开店时间")
    private Long storeTime;
}
