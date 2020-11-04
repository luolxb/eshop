package com.soubao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品评价信息类
 * 数据库不存在对应表
 */
@Data
@AllArgsConstructor
public class GoodsCommentStatistic implements Serializable {
    private Integer goodsId;
    private Integer imgSum;
    private Integer highSum;
    private Integer centerSum;
    private Integer lowSum;
    private Integer totalSum;

    private BigDecimal highRate;
    private BigDecimal centerRate;
    private BigDecimal lowRate;

    public GoodsCommentStatistic() {
        goodsId = 0;
        imgSum = 0;
        highSum = 0;
        centerSum = 0;
        lowSum = 0;
        totalSum = 0;

        highRate = BigDecimal.valueOf(100);
        centerRate = BigDecimal.valueOf(0);
        lowRate = BigDecimal.valueOf(0);
    }
    public GoodsCommentStatistic(Integer goodsId) {
        goodsId = goodsId;
        imgSum = 0;
        highSum = 0;
        centerSum = 0;
        lowSum = 0;
        totalSum = 0;

        highRate = BigDecimal.valueOf(100);
        centerRate = BigDecimal.valueOf(0);
        lowRate = BigDecimal.valueOf(0);
    }
}