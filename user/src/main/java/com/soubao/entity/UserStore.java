package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.validation.annotation.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_store")
@ApiModel("用户店铺对象")
public class UserStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户店铺id")
    @TableId(type = IdType.AUTO)
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
