package com.soubao.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.utils.TimeUtil;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author dyr
 * @since 2019-10-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pre_sell")
public class PreSell implements Serializable, GoodsProm {

    private static final long serialVersionUID = 1L;

    /**
     * 预售id
     */
    @TableId(value = "pre_sell_id", type = IdType.AUTO)
    private Integer preSellId;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 商品名称
     */
    @NotBlank(message = "请选择参与预售的商品")
    private String goodsName;

    /**
     * 规格id
     */
    private Integer itemId;

    /**
     * 规格名称
     */
    private String itemName;

    /**
     * 预售标题
     */
    @NotBlank(message = "预售标题必须")
    @Size(max = 255, message = "预售标题长度不得超过255字符")
    private String title;

    /**
     * 预售描述
     */
    @TableField(value = "`desc`")
    private String desc;

    /**
     * 订购商品数
     */
    private Integer depositGoodsNum;

    /**
     * 订购订单数
     */
    private Integer depositOrderNum;

    /**
     * 预售库存
     */
    @NotNull(message = "库存必须")
    private Integer stockNum;

    /**
     * 活动开始时间
     */
    private Long sellStartTime;

    /**
     * 活动结束时间
     */
    private Long sellEndTime;

    /**
     * 尾款支付开始时间
     */
    private Long payStartTime;

    /**
     * 尾款支付结束时间
     */
    private Long payEndTime;

    @TableField(exist = false)
    private boolean isPayDxpired;

    /**
     * 是否过了支付时间
     *
     * @return
     */
    public Boolean getIsPayDxpired() {
        return System.currentTimeMillis() / 1000 > payEndTime;
    }

    @TableField(exist = false)
    private boolean isAblePay;

    /**
     * 是否可以支付尾款
     *
     * @return
     */
    public Boolean getIsAblePay() {
        long now = (System.currentTimeMillis() / 1000);
        if (isFinished == 2 && now <= payEndTime && now >= payStartTime) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 订金
     */
    @NotNull(message = "订金必须")
    private BigDecimal depositPrice;

    /**
     * 价格阶梯。预定人数达到多少个时，价格为多少钱
     */
    private String priceLadder;

    /**
     * 开始发货时间描述
     */
    @NotBlank(message = "发货时间描述必须")
    @Size(max = 255, message = "发货时间描述长度不得超过255字符")
    private String deliveryTimeDesc;

    /**
     * 店铺id
     */
    private Integer storeId;

    /**
     * 是否已结束:0,正常；1，结束（待处理）；2,成功结束；3，失败结束。
     */
    private Integer isFinished;

    /**
     * 团购状态，0待审核，1正常2拒绝3关闭
     */
    private Integer status;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    @TableField(exist = false)
    private String statusDesc;

    public String getStatusDesc() {
        if (status != null) {
            switch (status) {
                case 0:
                    return "审核中";
                case 1:
                    return "已通过";
                case 2:
                    return "审核失败";
                case 3:
                    return "管理员关闭";
                default:
                    return "";
            }
        }
        return statusDesc;
    }

    @TableField(exist = false)
    private String sellTimeShow;   //活动时间展示

    public String getSellTimeShow() {
        if (sellStartTime != null && sellEndTime != null) {
            return TimeUtil.transForDateStr(sellStartTime, "yyyy-MM-dd HH:mm:ss")
                    + " 至 "
                    + TimeUtil.transForDateStr(sellEndTime, "yyyy-MM-dd HH:mm:ss");
        }
        return sellTimeShow;
    }

    @TableField(exist = false)
    private String payTimeShow;   //支付时间展示

    public String getPayTimeShow() {
        if (depositPrice.compareTo(BigDecimal.ZERO) == 0){
            return "---";
        }
        return TimeUtil.transForDateStr(payStartTime, "yyyy-MM-dd HH:mm:ss")
                + " 至 "
                + TimeUtil.transForDateStr(payEndTime, "yyyy-MM-dd HH:mm:ss");
    }

    @TableField(exist = false)
    private Store store;

//    @Valid
    @TableField(exist = false)
    private List<LadderPrice> ladderPrice;

    public List<LadderPrice> getLadderPrice() {
        if (priceLadder != null) {
            return JSON.parseArray(priceLadder, LadderPrice.class);
        }
        return ladderPrice;
    }

    @TableField(exist = false)
    private BigDecimal shopPrice;

    @Override
    public BigDecimal getShopPrice() {
        List<LadderPrice> ladderPriceTmp = getLadderPrice();
        int ladderPriceSize = ladderPriceTmp.size();
        if (ladderPriceSize == 1) {
            return ladderPriceTmp.get(0).getPrice();
        }
        for (int ladderPriceIndex = 0; ladderPriceIndex < ladderPriceSize; ladderPriceIndex++) {
            if (ladderPriceIndex == 0 && ladderPriceTmp.get(ladderPriceIndex).getAmount() >= depositGoodsNum) {
                return ladderPriceTmp.get(ladderPriceIndex).getPrice();
            }
            if (ladderPriceIndex == (ladderPriceSize - 1)) {
                return ladderPriceTmp.get(ladderPriceIndex).getPrice();
            }
            if (depositGoodsNum >= ladderPriceTmp.get(ladderPriceIndex).getAmount()
                    && depositGoodsNum < ladderPriceTmp.get(ladderPriceIndex + 1).getAmount()) {
                return ladderPriceTmp.get(ladderPriceIndex).getPrice();
            }
        }
        return null;
    }

    @TableField(exist = false)
    private BigDecimal payMoney;

    @Override
    public BigDecimal getPayMoney() {
        return depositPrice;
    }

    @TableField(exist = false)
    private Integer stock;

    @Override
    public Integer getStock() {
        return stockNum;
    }

    @TableField(exist = false)
    private String promTypeDesc;

    @Override
    public String getPromTypeDesc() {
        return "预售";
    }

    @TableField(exist = false)
    private Long promStartTime;

    @Override
    public Long getPromStartTime() {
        return (long) sellStartTime;
    }

    @TableField(exist = false)
    private Long promEndTime;

    @Override
    public Long getPromEndTime() {
        return (long) sellEndTime;
    }

    @TableField(exist = false)
    private boolean isOn;

    @Override
    public boolean getIsOn() {
        long now = System.currentTimeMillis() / 1000;
        if (sellStartTime != null || sellEndTime != null || status != null || isFinished != null) {
            if (now > getPromStartTime() && now < getPromEndTime() && status == 1 && isFinished == 0) {
                return true;
            }
        }
        return false;
    }

    @TableField(exist = false)
    private Integer buyLimit;

    @Override
    public Integer getBuyLimit() {
        return 0;
    }

    @NotBlank(message = "活动开始时间必须")
    @TableField(exist = false)
    private String startTimeShow;   //活动开始时间

    public String getStartTimeShow() {
        if (sellStartTime != null) {
            return TimeUtil.transForDateStr(sellStartTime, "yyyy-MM-dd HH:mm:ss");
        }
        return startTimeShow;
    }

    @NotBlank(message = "活动结束时间必须")
    @TableField(exist = false)
    private String endTimeShow;   //活动结束时间

    public String getEndTimeShow() {
        if (sellEndTime != null) {
            return TimeUtil.transForDateStr(sellEndTime, "yyyy-MM-dd HH:mm:ss");
        }
        return endTimeShow;
    }

    @TableField(exist = false)
    private String payStartTimeShow;   //尾款支付开始时间

    public String getPayStartTimeShow() {
        if (payStartTime != null) {
            return TimeUtil.transForDateStr(payStartTime, "yyyy-MM-dd HH:mm:ss");
        }
        return payStartTimeShow;
    }

    @TableField(exist = false)
    private String payEndTimeShow;   //尾款支付结束时间

    public String getPayEndTimeShow() {
        if (payEndTime != null) {
            return TimeUtil.transForDateStr(payEndTime, "yyyy-MM-dd HH:mm:ss");
        }
        return payEndTimeShow;
    }


}
