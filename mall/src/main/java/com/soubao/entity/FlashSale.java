package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.utils.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("flash_sale")
public class FlashSale implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "活动标题不能为空")
    private String title;

    private Integer goodsId;

    private Integer itemId;

    @Digits(integer = 7, fraction = 2, message = "抢购价格不正确，至多七位整数与两位小数")
    @DecimalMin(value = "0.01", message = "抢购价格不能小于0.01")
    @DecimalMax(value = "9999999", message = "抢购价格不能大于9999999")
    @NotNull(message = "抢购价格不能为空")
    private BigDecimal price;

    @NotNull(message = "抢购数量不能为空")
    private Integer goodsNum;

    @NotNull(message = "限购数量不能为空")
    private Integer buyLimit;

    private Integer buyNum;

    private Integer orderNum;

    @NotNull(message = "活动开始时间不能为空")
    private Long startTime;

    private Long endTime;

    private Integer isEnd;

    @NotBlank(message = "请选择参与抢购的商品")
    private String goodsName;

    private Integer storeId;


    private Integer recommend;

    private Integer status;


    @NotBlank(message = "活动描述不能为空")
    private String description;


    private Integer isDel;

    private Date gmtCreate;

    private Date gmtModified;

    @TableField(exist = false)
    private BigDecimal shopPrice; // 商品原价

    @TableField(exist = false)
    private BigDecimal percent; // 商品原价

    @TableField(exist = false)
    private String statusDesc; // 抢购状态描述

    public String getStatusDesc() {
        if (isEnd != null && status != null) {
            if (isEnd == 1) {
                if (status == 0) {
                    return "已过期&未审核";
                }
                return "已结束";
            }
            switch (status) {
                case 0:
                    return "待审核";
                case 1:
                    if (startTime > System.currentTimeMillis() / 1000) {
                        return "未开始";
                    } else {
                        return "进行中";
                    }
                case 2:
                    return "未通过";
                case 3:
                    return "管理员关闭";
                case 4:
                    return "已售馨";
                default:
                    return "";
            }
        }
        return statusDesc;
    }

    @TableField(exist = false)
    private String startTimeShow; // 起始时间展示

    public String getStartTimeShow() {
        return TimeUtil.transForDateStr(startTime, "yyyy-MM-dd HH:mm:ss");
    }

    @TableField(exist = false)
    private String endTimeShow; // 结束时间展示

    public String getEndTimeShow() {
        return TimeUtil.transForDateStr(endTime, "yyyy-MM-dd HH:mm:ss");
    }

    private static final long serialVersionUID = 1L;
}
