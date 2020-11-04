package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.soubao.common.utils.TimeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 团购商品表
 * </p>
 *
 * @author dyr
 * @since 2019-08-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("group_buy")
@JsonIgnoreProperties(value = {})
public class GroupBuy implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 活动名称
     */
    @NotBlank(message = "活动标题不能为空")
    private String title;

    /**
     * 开始时间
     */

    @NotNull(message = "活动开始时间不能为空")
    private Long startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "活动结束时间不能为空")
    private Long endTime;

    /**
     * 商品ID
     */
    @NotNull(message = "请选择参与抢购的商品")
    private Integer goodsId;

    /**
     * 对应spec_goods_price商品规格id
     */
    private Integer itemId;

    /**
     * 团购价格
     */
    private BigDecimal price;

    /**
     * 商品参团数
     */
    private Integer goodsNum;

    /**
     * 商品已购买数
     */
    private Integer buyNum;

    /**
     * 已下单人数
     */
    private Integer orderNum;

    /**
     * 虚拟购买数
     */
    private Integer virtualNum;

    /**
     * 折扣
     */
    private BigDecimal rebate;

    /**
     * 本团介绍
     */
    private String intro;

    /**
     * 商品原价
     */
    private BigDecimal goodsPrice;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 是否推荐 0.未推荐 1.已推荐
     */
    private Integer recommend;

    /**
     * 查看次数
     */
    private Integer views;

    /**
     * 商家店铺ID
     */
    private Integer storeId;

    /**
     * 团购状态，0待审核，1正常2拒绝3关闭
     */
    private Integer status;

    /**
     * 是否结束,1结束 ，0正常
     */
    private Integer isEnd;

    /**
     * 是否删除,1是 ，0否
     */
    private Integer isDeleted;


    private Date gmtCreate;

    private Date gmtModified;

    @TableField(exist = false)
    private String isEndDesc;

    public String getIsEndDesc() {
        if (isEnd != null) {
            return isEnd == 1 ? "是" : "否";
        }
        return isEndDesc;
    }

    @TableField(exist = false)
    private String statusDesc;  //团购状态描述

    public String getStatusDesc() {
        if (status != null) {
            switch (status) {
                case 0:
                    return "待审核";
                case 1:
                    if (startTime != null && startTime > System.currentTimeMillis() / 1000) {
                        return "未开始";
                    }
                    return "正常";
                case 2:
                    return "未通过";
                case 3:
                    return "管理员关闭";
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
            return TimeUtil.transForDateStr(startTime, "yyyy-MM-dd");
        }
        return startTimeShow;
    }

    @TableField(exist = false)
    private String endTimeShow;   //结束时间展示

    public String getEndTimeShow() {
        if (endTime != null) {
            return TimeUtil.transForDateStr(endTime, "yyyy-MM-dd");
        }
        return endTimeShow;
    }

    @Valid // 嵌套验证必须用@Valid
    @NotNull(message = "请选择团购商品")
    @Size(min = 1, message = "团购商品至少一项规格或无规格属性")
    @TableField(exist = false)
    private List<GroupBuyGoodsItem> groupBuyGoodsItem;//团购商品规格

}
