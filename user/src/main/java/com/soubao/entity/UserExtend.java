package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_extend")
@ApiModel("用户发票对象")
public class UserExtend implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户发票表id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("发票抬头")
    private String invoiceTitle;

    @ApiModelProperty("纳税人识别号")
    private String taxpayer;

    @ApiModelProperty("不开发票/明细")
    private String invoiceDesc;

    @ApiModelProperty("真实姓名")
    private String realname;

    @ApiModelProperty("身份证号")
    private String idcard;


}
