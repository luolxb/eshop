package com.soubao.entity;

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
public class UserSellerStore implements Serializable {

    @ApiModelProperty(value = "散户店铺 关联表 id")
    private Integer userToreId;

    @ApiModelProperty(value = "用户id")
    @JsonProperty("user_id")
    private Integer userId;

    @ApiModelProperty(value = "商店id")
    @JsonProperty("store_id")
    private Integer storeId;
}
