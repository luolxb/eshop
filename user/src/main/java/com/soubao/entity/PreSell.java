package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author dyr
 * @since 2019-10-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pre_sell")
public class PreSell implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 预售id
     */
    @TableId(value = "pre_sell_id", type = IdType.AUTO)
    private Integer preSellId;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 规格id
     */
    private Integer itemId;

    /**
     * 规格名称
     */
    private String itemName;

    /**
     * 预售标题
     */
    private String title;

    /**
     * 预售描述
     */
    @TableField(value = "`desc`")
    private String desc;

    /**
     * 订购商品数
     */
    private Integer depositGoodsNum;

    /**
     * 订购订单数
     */
    private Integer depositOrderNum;

    /**
     * 预售库存
     */
    private Integer stockNum;

    /**
     * 活动开始时间
     */
    private Long sellStartTime;

    /**
     * 活动结束时间
     */
    private Long sellEndTime;

    /**
     * 尾款支付开始时间
     */
    private Long payStartTime;

    /**
     * 尾款支付结束时间
     */
    private Long payEndTime;

    @TableField(exist = false)
    private boolean isPayDxpired;

    /**
     * 是否过了支付时间
     *
     * @return
     */
    public Boolean getIsPayDxpired() {
        if ((System.currentTimeMillis() / 1000) > payEndTime) {
            return true;
        } else {
            return false;
        }
    }

    @TableField(exist = false)
    private boolean isAblePay;

    /**
     * 是否可以支付尾款
     *
     * @return
     */
    public Boolean getIsAblePay() {
        long now = (System.currentTimeMillis() / 1000);
        if (isFinished == 2 && now <= payEndTime && now >= payStartTime) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 订金
     */
    private BigDecimal depositPrice;

    /**
     * 价格阶梯。预定人数达到多少个时，价格为多少钱
     */
    private String priceLadder;

    /**
     * 开始发货时间描述
     */
    private String deliveryTimeDesc;

    /**
     * 店铺id
     */
    private Integer storeId;

    /**
     * 是否已结束:0,正常；1，结束（待处理）；2,成功结束；3，失败结束。
     */
    private Integer isFinished;

    /**
     * 团购状态，0待审核，1正常2拒绝3关闭
     */
    private Integer status;

    /**
     * 是否删除
     */
    private Integer isDeleted;

}
