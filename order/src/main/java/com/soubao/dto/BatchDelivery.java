package com.soubao.dto;

import com.soubao.entity.Order;
import lombok.Data;

import java.util.List;

/**
 * 批量发货数据接收对象
 */
@Data
public class BatchDelivery {
    private Integer storeId;
    private Integer sellerId;
    private Integer storeAddressId;
    private String shippingCode;
    private String shippingName;
    private String note;
    private List<Order> orders;
}
