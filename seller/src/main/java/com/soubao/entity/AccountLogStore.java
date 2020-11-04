package com.soubao.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2020-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AccountLogStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;

    private Integer storeId;

    /**
     * 店铺金额
     */
    private BigDecimal storeMoney;

    /**
     * 店铺未结算金额
     */
    private BigDecimal pendingMoney;

    /**
     * 变动时间
     */
    private Long changeTime;

    /**
     * 描述
     */
    @TableField(value = "`desc`")
    private String desc;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 订单id
     */
    private Integer orderId;


}
