package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.soubao.common.utils.TimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-09-02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("coupon_list")
@ApiModel(value = "优惠卷集合", description = "coupon_list表")
public class CouponList implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户名称")
    @TableField(exist = false)
    private String username;

    @ApiModelProperty(value = "优惠券 对应coupon表id")
    private Integer cid;

    @ApiModelProperty(value = "优惠券名称")
    @TableField(exist = false)
    private String name;


    @ApiModelProperty(value = "发放类型 0下单赠送 1 按用户发放 2 免费领取 3 线下发放")
    private Integer type;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "订单id")
    private Integer orderId;

    @ApiModelProperty(value = "送券订单ID")
    private Integer getOrderId;

    @ApiModelProperty(value = "使用时间")
    private Long useTime;

    @ApiModelProperty(value = "优惠券兑换码")
    private String code;

    @ApiModelProperty(value = "发放时间")
    private Long sendTime;

    @ApiModelProperty(value = "商家店铺ID")
    private Integer storeId;

    @ApiModelProperty(value = "0未使用1已使用2已过期")
    private Integer status;

    @ApiModelProperty(value = "删除标识;1:删除,0未删除")
    private Boolean deleted;

    @TableField(exist = false)
    private String orderSn;

    @TableField(exist = false)
    private String typeDetail;

    public String getTypeDetail() {
        if (type == 0) {
            return "下单赠送";
        }
        if (type == 1) {
            return "按用户发放";
        }
        if (type == 2) {
            return "免费领取";
        }
        if (type == 3) {
            return "线下发放";
        }
        if (type == 4) {
            return "新人优惠券";
        }
        return typeDetail;
    }

    @TableField(exist = false)
    private String orderSnShow;

    public String getOrderSnShow() {
        if (StringUtils.isEmpty(orderSn)) {
            return "无";
        }
        return orderSn;
    }

    @TableField(exist = false)
    private String useTimeDetail;

    public String getUseTimeDetail() {
        if (useTime != null) {
            if (useTime == 0) {
                return "N";
            }
            return TimeUtil.transForDateStr(useTime, "yyyy-MM-dd HH:mm:ss");
        }
        return useTimeDetail;
    }

    @TableField(exist = false)
    private String statusDetail;

    public String getStatusDetail() {
        if (status == 0) {
            return "未使用";
        }
        if (status == 1) {
            return "已使用";
        }
        if (status == 2) {
            return "已过期";
        }
        return statusDetail;
    }


}
