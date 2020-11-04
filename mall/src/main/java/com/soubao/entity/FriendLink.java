package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("friend_link")
@ApiModel(value = "友情链接对象", description = "friend_link表")
public class FriendLink implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键索引")
    @TableId(value = "link_id", type = IdType.AUTO)
    private Integer linkId;

    @ApiModelProperty("链接名称")
    @NotBlank(message = "链接名称不能为空")
    private String linkName;

    @ApiModelProperty("链接地址")
    @NotBlank(message = "链接地址不能为空")
    private String linkUrl;

    @ApiModelProperty("链接图片")
    private String linkLogo;

    @ApiModelProperty("排序")
    @NotNull(message = "排序值不能为空")
    @Max(value = 255, message = "排序最大值不能超过255")
    private Integer orderby;

    @ApiModelProperty("是否显示")
    private Integer isShow;

    @ApiModelProperty("是否新窗口打开")
    private Integer target;

}
