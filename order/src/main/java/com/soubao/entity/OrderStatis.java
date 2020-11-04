package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 商家订单结算表
 * </p>
 *
 * @author dyr
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_statis")
public class OrderStatis implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 店铺名称
     */
    @TableField(exist = false)
    private String storeName;

    /**
     * 开始日期
     */
    private Long startDate;

    /**
     * 结束日期
     */
    private Long endDate;

    /**
     * 订单商品金额
     */
    private BigDecimal orderTotals;

    /**
     * 物流运费
     */
    private BigDecimal shippingTotals;

    /**
     * 退款金额
     */
    private BigDecimal returnTotals;

    /**
     * 退还积分
     */
    private Integer returnIntegral;

    /**
     * 平台抽成
     */
    private BigDecimal commisTotals;

    /**
     * 送出积分金额
     */
    private BigDecimal giveIntegral;

    /**
     * 本期应结
     */
    private BigDecimal resultTotals;

    /**
     * 创建记录日期
     */
    private Long createDate;

    /**
     * 店铺id
     */
    private Integer storeId;

    /**
     * 优惠价
     */
    private BigDecimal orderPromAmount;

    /**
     * 优惠券抵扣
     */
    private BigDecimal couponPrice;

    /**
     * 订单使用积分
     */
    private Integer integral;

    /**
     * 分销金额
     */
    private BigDecimal distribut;

    /**
     * 实付款
     */
    private BigDecimal payMoney;

    /**
     * 调整金额
     */
    private BigDecimal discount;


}
