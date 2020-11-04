package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderGoods implements Serializable {

    @TableId(value = "rec_id", type = IdType.AUTO)
    private Integer recId;


    private Integer orderId;

    @TableField(exist = false)
    private Integer dcId;

    @TableField(exist = false)
    private List<GoodsImages> GoodsImages;

    @TableField(exist = false)
    private String originalImg;

    private Integer goodsId;

    private String goodsName;

    private String goodsSn;
    private Integer goodsNum;
    /**
     * 上架时间
     */
    private Long onTime;

    private BigDecimal finalPrice;

    private BigDecimal goodsPrice;
    /**
     * 上架价格
     */
    @TableField(exist = false)
    private BigDecimal onGoodsPrice;

    private BigDecimal costPrice;

    private BigDecimal memberGoodsPrice;

    private Integer giveIntegral;

    private String specKey;
    private String specKeyName;

    private String barCode;
    private Integer isComment;

    private Integer promType;

    private Integer promId;

    private Integer isSend;

    private Integer deliveryId;

    private String sku;

    private Integer storeId;


    private Integer commission;

    private Integer isCheckout;

    private Integer deleted;
    private BigDecimal distribut;


    private Integer shopId;

    @TableField(exist = false)
    @ApiModelProperty(value = "认购时间")
    @JsonProperty("subscription_time")
    private Date subscriptionTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "认购价格")
    @JsonProperty("subscription_price")
    private BigDecimal subscriptionPrice;

    public BigDecimal getFinalPrice(){
        if (promType != null && promType == 4){
            return goodsPrice;
        }
        return finalPrice;
    }

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private Order order;

    @TableField(exist = false)
    private BigDecimal cutFee;

    @TableField(exist = false)
    private Integer isDelivery;

    @TableField(exist = false)
    private Integer limitNum;

    @TableField(exist = false)
    private BigDecimal goodsFee;

    @TableField(exist = false)
    private BigDecimal goodsTotal;
    public BigDecimal getGoodsTotal(){
        if(null != finalPrice && null != goodsNum){
            goodsTotal = finalPrice.multiply(new BigDecimal((goodsNum)));
        }
        return goodsTotal;
    }

    @TableField(exist = false)
    private Integer unsend;//是否申请退换货
    @TableField(exist = false)
    private Boolean checked;//发货页是否选中发货
    @TableField(exist = false)
    private String storeName;//店铺名称
    @TableField(exist = false)
    private String isCommentDesc;

    public String getIsCommentDesc() {
        if (isComment != null) {
            if (isComment == 1) {
                return "已评价";
            }
            return "未评价";
        }
        return isCommentDesc;
    }
}