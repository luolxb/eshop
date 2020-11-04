package com.soubao.vo;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class SpecGoodsVo {

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 对应spec_goods_price商品规格id
     */
    private Long itemId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品sku名称
     */
    private String keyName;

    public String getKeyName() {
        if (keyName == null) {
            return keyName = "--";
        }
        return keyName;
    }

    /**
     * 商品库存
     */
    private Integer storeCount;

    /**
     * 商品价格
     */
    private BigDecimal shopPrice;

    public BigDecimal getShopPrice() {
        if (shopPrice == null && price != null) {
            return shopPrice = price;
        }
        return shopPrice;
    }

    /**
     * 商品价格
     */
    private BigDecimal price;
}
