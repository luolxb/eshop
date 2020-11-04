package com.soubao.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 平台支出金额或赠送积分记录日志
 * </p>
 *
 * @author dyr
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ExpenseLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作管理员
     */
    private Integer adminId;

    /**
     * 支出金额
     */
    private BigDecimal money;

    /**
     * 赠送积分
     */
    private Integer integral;

    /**
     * 支出类型0商家提现1用户提现2订单取消退款3订单售后退款4其他
     */
    private Boolean type;

    /**
     * 日志记录时间
     */
    private Integer addtime;

    /**
     * 业务关联ID
     */
    private Integer logTypeId;

    /**
     * 涉及会员id
     */
    private Integer userId;

    /**
     * 涉及商家id
     */
    private Integer storeId;


}
