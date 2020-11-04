package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 优惠券面额表
 * </p>
 *
 * @author dyr
 * @since 2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("coupon_price")
public class CouponPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "coupon_price_id", type = IdType.AUTO)
    private Integer couponPriceId;

    /**
     * 优惠券面额
     */
    private Integer couponPriceValue;


}
