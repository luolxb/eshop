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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tp_flash_sell")
public class FlashSale implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 活动标题
     * <p>
     * Table:     tp_flash_sale
     * Column:    title
     * Nullable:  false
     */
    private String title;

    private Integer goodsId;

    /**
     * 对应spec_goods_price商品规格id
     * <p>
     * Table:     tp_flash_sale
     * Column:    item_id
     * Nullable:  true
     */
    private Integer itemId;

    private BigDecimal price;

    /**
     * 商品参加活动数
     * <p>
     * Table:     tp_flash_sale
     * Column:    goods_num
     * Nullable:  true
     */
    private Integer goodsNum;

    private Integer buyLimit;

    /**
     * 已购买人数
     * <p>
     * Table:     tp_flash_sale
     * Column:    buy_num
     * Nullable:  false
     */
    private Integer buyNum;

    private Integer orderNum;

    /**
     * 开始时间
     * <p>
     * Table:     tp_flash_sale
     * Column:    start_time
     * Nullable:  false
     */
    private Long startTime;

    private Long endTime;

    private Integer isEnd;

    /**
     * 商品名称
     * <p>
     * Table:     tp_flash_sale
     * Column:    goods_name
     * Nullable:  true
     */
    private String goodsName;

    private Integer storeId;

    /**
     * 是否推荐
     * <p>
     * Table:     tp_flash_sale
     * Column:    recommend
     * Nullable:  true
     */
    private Integer recommend;

    private Integer status;

    private String description;

    private Integer isDel;

    private Date gmtCreate;

    private Date gmtModified;

    @TableField(exist = false)
    private BigDecimal shopPrice;   //商品原价

    @TableField(exist = false)
    private BigDecimal percent;   //商品原价

    @TableField(exist = false)
    private String statusDesc;  //抢购状态描述

    public String getStatusDesc() {
        if (isEnd != null && status != null) {
            if (isEnd == 1){
                if (status == 0){
                    return "已过期&未审核";
                }
                return "已结束";
            }
            switch (status) {
                case 0:
                    return "待审核";
                case 1:
                    return "进行中";
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
    private String startTimeShow;   //起始时间展示

    public String getStartTimeShow() {
        if (startTime != null) {
            return TimeUtil.transForDateStr(startTime, "yyyy-MM-dd HH:mm:ss");
        }
        return startTimeShow;
    }

    @TableField(exist = false)
    private String endTimeShow;   //结束时间展示

    public String getEndTimeShow() {
        if (endTime != null) {
            return TimeUtil.transForDateStr(endTime, "yyyy-MM-dd HH:mm:ss");
        }
        return endTimeShow;
    }

    private static final long serialVersionUID = 1L;
}