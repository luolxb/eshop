package com.soubao.dto;

import lombok.Data;

/**
 * 消费者保障服务申请数据接收对象
 */
@Data
public class GuaranteeApplyDto {
    private Integer applyType;
    private Integer grtId;
    private Integer storeId;
    private Integer sellerId;
}
