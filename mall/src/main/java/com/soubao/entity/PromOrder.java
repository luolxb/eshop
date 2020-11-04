package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.utils.TimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dyr
 * @since 2019-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("prom_order")
@ApiModel(value = "订单优惠对象", description = "prom_order表")
public class PromOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单优惠主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "活动名称不能为空")
    @ApiModelProperty(value = "活动名称")
    private String title;

    @ApiModelProperty(value = "活动类型(0满额打折,1满额优惠金额,2满额送积分,3满额送优惠券)")
    private Integer type;

    @NotNull(message = "请填写最小使用金额")
    @ApiModelProperty(value = "最小金额")
    private BigDecimal money;

    @NotBlank(message = "优惠不能为空")
    @ApiModelProperty(value = "优惠体现(0打折率,1金额,2积分,3优惠券id)")
    private String expression;

    @ApiModelProperty(value = "活动描述")
    private String description;

    @ApiModelProperty(value = "活动开始时间")
    private Long startTime;

    @ApiModelProperty(value = "活动结束时间")
    private Long endTime;

    @ApiModelProperty(value = "正常，0管理员关闭")
    private Integer status;

    @ApiModelProperty(value = "适用范围")
    @TableField("`group`")
    private String group;

    @NotBlank(message = "请上传图片")
    @ApiModelProperty(value = "活动宣传图")
    private String promImg;

    @ApiModelProperty(value = "商家店铺id")
    private Integer storeId;

    @ApiModelProperty(value = "排序权重")
    private Integer orderby;

    @ApiModelProperty(value = "是否推荐")
    private Integer recommend;

    @ApiModelProperty(value = "优惠描述")
    @TableField(exist = false)
    private String typeDesc;

    public String getTypeDesc() {
        if (type != null) {
            switch (type) {
                case 0:
                    return "满额打折";
                case 1:
                    return "满额优惠金额";
                case 2:
                    return "满额送积分";
                case 3:
                    return "满额送优惠券";
                default:
                    return "";
            }
        }
        return typeDesc;
    }

    @ApiModelProperty(value = "状态描述")
    @TableField(exist = false)
    private String statusDesc;

    public String getStatusDesc() {
        Long time = System.currentTimeMillis() / 1000;
        if (startTime != null && endTime != null && status != null) {
            if (time > startTime && time < endTime && status == 1) {
                return "进行中";
            } else if (time < startTime) {
                return "未开始";
            } else if (status == 0) {
                return "管理员关闭";
            } else {
                return "已过期";
            }
        }
        return statusDesc;
    }

    @NotBlank(message = "活动开始时间必须")
    @ApiModelProperty(value = "起始时间格式化")
    @TableField(exist = false)
    private String startTimeShow;

    public String getStartTimeShow() {
        if (startTime != null) {
            return TimeUtil.transForDateStr(startTime, "yyyy-MM-dd");
        }
        return startTimeShow;
    }

    @NotBlank(message = "活动结束时间必须")
    @ApiModelProperty(value = "结束时间格式化")
    @TableField(exist = false)
    private String endTimeShow;

    public String getEndTimeShow() {
        if (endTime != null) {
            return TimeUtil.transForDateStr(endTime, "yyyy-MM-dd");
        }
        return endTimeShow;
    }

    @ApiModelProperty(value = "是否删除")
    private boolean isDeleted;
}
