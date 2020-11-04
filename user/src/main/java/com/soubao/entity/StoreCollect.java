package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soubao.common.utils.TimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("store_collect")
@ApiModel("店铺收藏对象")
public class StoreCollect implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("索引id")
    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("收藏的店铺id")
    private Integer storeId;

    @ApiModelProperty("收藏店铺时间")
    private Long addTime;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("用户名称名称")
    private String userName;

    @ApiModelProperty("店铺等级名称")
    @TableField(exist = false)
    private String sgName;

    @ApiModelProperty("店铺Logo")
    @TableField(exist = false)
    @JsonIgnore
    private String storeLogo;

    @ApiModelProperty("店铺头像")
    @TableField(exist = false)
    private String storeAvatar;

    @ApiModelProperty("店铺收藏数量")
    @TableField(exist = false)
    private Integer storeCollect;

    @ApiModelProperty("店铺服务态度分数")
    @TableField(exist = false)
    private BigDecimal storeServicecredit;

    @TableField(exist = false)
    private String addTimeDesc;

    @TableField(exist = false)
    private Store store;

    public String getAddTimeDesc() {
        return TimeUtil.transForDateStr(this.addTime, "yyyy-MM-dd HH:mm:ss");
    }

}
