package com.soubao.entity;

import java.math.BigDecimal;

public interface GoodsProm {
    /**
     * 获取活动价
     * @return
     */
    BigDecimal getShopPrice();

    /**
     * 获取订金
     * @return
     */
    BigDecimal getPayMoney();

    /**
     * 获取活动库存
     * @return
     */
    Integer getStock();

    /**
     * 获取活动类型描述：预售，抢购，团购，促销，拼团
     * @return
     */
    String getPromTypeDesc();

    /**
     * 获取活动开始时间
     */
    Long getPromStartTime();
    /**
     * 获取活动结束时间
     */
    Long getPromEndTime();

    /**
     * 活动是否正在进行
     * @return
     */
    boolean getIsOn();
    /**
     * 单次购买限制 0表示不限制
     * @return
     */
    Integer getBuyLimit();

}
