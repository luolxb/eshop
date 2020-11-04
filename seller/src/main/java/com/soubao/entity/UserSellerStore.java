package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 散户店铺 关联表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_seller_store")
public class UserSellerStore implements Serializable {

    @ApiModelProperty(value = "散户店铺 关联表 id")
    @TableId(value = "user_tore_id", type = IdType.AUTO)
    private Integer userToreId;

    @ApiModelProperty(value = "用户id")
    @JsonProperty("user_id")
    private Integer userId;

    @ApiModelProperty(value = "商店id")
    @JsonProperty("store_id")
    private Integer storeId;
}
