package com.soubao.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderAndPickOrderVo implements Serializable {

    @ApiModelProperty("订单ID")
    private Integer orderId;
    private String orderSn;

    @ApiModelProperty("提货订单ID")
    private Integer pickOrderId;
    private String pickOrderSn;

    @ApiModelProperty("商品图片")
    private String originalImg;

    @ApiModelProperty("商品ID")
    private Integer goodsId;

    @ApiModelProperty("订单状态")
    private Integer orderStatus;

    @ApiModelProperty("订单状态描述")
    private String orderStatusDsc;

    @ApiModelProperty("提货订单状态")
    private Integer pickOrderStatus;

    @ApiModelProperty("下单时间")
    private int addTime;

    @ApiModelProperty("商品价格")
    private BigDecimal goodsPrice;

    @ApiModelProperty("商店名称")
    private String storeName;
    private String storeLog;

    @ApiModelProperty("商店ID")
    private String storeId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("是否可取消")
    private boolean cancel =false;

    /**
     * pickOrderStatus : 提货订单状态（0：待发货/提货中 1:确认发货 2：已发货/提货中 3：已完成/已提货）
     * payStatus 订单支付状态 0待支付 1已支付 5已取消订单
     *
     * @param orderStatus
     * @param pickOrderStatus
     * @return
     */
    public String getOrderStatusDsc(Integer orderStatus, Integer pickOrderStatus) {
        if (orderStatus == null) {
            switch (pickOrderStatus) {
                case 0:
                    orderStatusDsc = "待发货";
                    break;
                case 1:
                    orderStatusDsc = "确认发货";
                    break;
                case 2:
                    orderStatusDsc = "待收货";
                    break;
                case 3:
                    orderStatusDsc = "已完成";
                    break;
            }
        }

        if (pickOrderStatus == null) {
            switch (orderStatus) {
                case 0:
                    orderStatusDsc = "待付款";
                    break;
                case 1:
                    orderStatusDsc = "已付款";
                    break;
                case 5:
                    orderStatusDsc = "已取消";
                    break;
            }
        }
        return orderStatusDsc;

    }


    private static final long serialVersionUID = 1L;


}
