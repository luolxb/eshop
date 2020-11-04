package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;

import com.soubao.common.utils.TimeUtil;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-09-25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_action")
public class OrderAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "action_id", type = IdType.AUTO)
    private Integer actionId;

    /**
     * 订单ID
     */
    private Integer orderId;

    /**
     * 操作人 0 为用户操作，其他为管理员id
     */
    private Integer actionUser;

    private Integer orderStatus;

    private Integer shippingStatus;

    private Integer payStatus;

    /**
     * 订单操作日志变更的钱
     */
    @TableField(exist = false)
    private BigDecimal amount;

    /**
     * 操作备注
     */
    private String actionNote;

    /**
     * 操作时间
     */
    private Long logTime;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 0管理员1商家2前台用户
     */
    private Integer userType;

    /**
     * 商家店铺ID
     */
    private Integer storeId;

    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private Seller seller;

    @TableField(exist = false)
    private String logTimeDesc;
    public String getLogTimeDesc(){
        if (logTime != null) {
            return TimeUtil.transForDateStr(this.logTime, "yyyy-MM-dd HH:mm:ss");
        }
        return logTimeDesc;
    }

    @TableField(exist = false)
    private String actionUserDesc;

    @TableField(exist = false)
    private String orderStatusDesc;
    public String getOrderStatusDesc(){
        if (orderStatus != null){
            switch (orderStatus){
                case 0:
                    return "待确认";
                case 1:
                    return "已确认";
                case 2:
                    return "已收货";//已完成
                case 3:
                    return "已取消";
                case 4:
                    return "已评价";
                case 5:
                    return "已关闭";
            }
        }
        return null;
    }

    @TableField(exist = false)
    private String payStatusDesc;
    public String getPayStatusDesc(){
        if (payStatus != null){
            switch (payStatus){
                case 0:
                    return "未支付";
                case 1:
                    return "已支付";
                case 2:
                    return "部分支付";
                case 3:
                    return "已退款";
                case 4:
                    return "拒绝退款";
            }
        }
        return null;
    }

    @TableField(exist = false)
    private String shippingStatusDesc;
    public String getShippingStatusDesc(){
        if (shippingStatus != null){
            switch (shippingStatus){
                case 0:
                    return "未发货";
                case 1:
                    return "已发货";
                case 2:
                    return "部分发货";
            }
        }
        return null;
    }

}
