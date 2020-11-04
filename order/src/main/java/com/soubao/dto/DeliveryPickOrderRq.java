package com.soubao.dto;

import com.soubao.entity.vo.PickOrderDeliveryRq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 提货订单发货请求实体类
 */
@Data
@ApiModel("发货订单实体类")
public class DeliveryPickOrderRq {

    @ApiModelProperty("商店地址ID")
    private Integer storeAddressId;
    private String note;
    @ApiModelProperty("物流单号")
    private String invoiceNo;
    @ApiModelProperty("提货订单")
    private List<PickOrderDeliveryRq> pickOrderDeliveryRqList;
}
