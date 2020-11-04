package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("store_shipping")
public class StoreShipping implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("物流ID")
    private Integer shippingId;

    @ApiModelProperty("店铺ID")
    private Integer storeId;
}
