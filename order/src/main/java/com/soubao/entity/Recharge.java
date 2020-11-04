package com.soubao.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.soubao.common.utils.TimeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2020-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("recharge")
public class Recharge implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;

    /**
     * 会员ID
     */
    private Long userId;

    /**
     * 会员昵称
     */
    private String nickname;

    /**
     * 充值单号
     */
    private String orderSn;

    /**
     * 充值金额
     */
    private BigDecimal account;

    /**
     * 充值时间
     */
    private Long ctime;

    /**
     * 支付时间
     */
    private Integer payTime;

    private String payCode;

    /**
     * 支付方式
     */
    private String payName;

    /**
     * 充值状态0:待支付 1:充值成功 2:交易关闭
     */
    private Integer payStatus;

    /**
     * 第三方平台交易流水号
     */
    private String transactionId;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private String ctimeShow;

    @TableField(exist = false)
    private String payStatusDesc;

    public String getCtimeShow() {
        return TimeUtil.transForDateStr(ctime, "yyyy-MM-dd");
    }

    public String getPayStatusDesc() {
        if (payStatus != null) {
            if (payStatus == 0) {
                return "待支付";
            }else if (payStatus == 1) {
                return "充值成功";
            }else if (payStatus == 2) {
                return "交易关闭";
            }
        }
        return null;
    }

}
