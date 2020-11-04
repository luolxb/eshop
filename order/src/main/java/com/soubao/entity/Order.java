package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.constant.OrderConstant;
import com.soubao.common.utils.TimeUtil;
import com.soubao.validation.group.order.Modify;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("`order`")
public class Order implements Serializable {
    @ApiModelProperty(value = "订单主键")
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;
    @ApiModelProperty(value = "订单编号")
    private String orderSn;
    @ApiModelProperty(value = "用户主键")
    private Integer userId;
    @ApiModelProperty(value = "主订单编号")
    private String masterOrderSn;
    @ApiModelProperty(value = "订单状态(0待确认，1已确认，2已收货，3已取消，4已完成，5已作废)")
    private Integer orderStatus;
    @ApiModelProperty(value = "支付状态(0待支付，1已支付，2部分支付，3已退款，4拒绝退款,5取消支付)")
    private Integer payStatus;
    /**
     * 2020 9 1
     * 0.发货已经审批，待物流)
     * 1.已发货
     * 3. 可提货
     * 4. 提货中、商家中心代确认提货申请
     * 5. 出售
     * 6. 已出售
     * 7. 已提货，确认收货
     *
     */
    @ApiModelProperty(value = "发货状态(1已发货，2部分发货，3待发货申请，4已申请，待发货审批，5,当前商品被设置为重新出售，0，发货已经审批，待物流)")
    private Integer shippingStatus;
    @ApiModelProperty(value = "收货人姓名")
    private String consignee;
    @ApiModelProperty(value = "地址表主键：国家")
    private Integer country;
    @ApiModelProperty(value = "地址表主键：省份")
    private Integer province;
    @ApiModelProperty(value = "地址表主键：城市")
    private Integer city;
    @ApiModelProperty(value = "地址表主键：县区")
    private Integer district;
    @ApiModelProperty(value = "地址表主键：乡镇")
    private Integer twon;
    @ApiModelProperty(value = "详细地址")
    private String address;
    @ApiModelProperty(value = "邮政编码")
    private String zipcode;
    @ApiModelProperty(value = "手机号码")
    private String mobile;
    @ApiModelProperty(value = "邮件")
    private String email;
    @ApiModelProperty(value = "物流编号")
    private String shippingCode;
    @ApiModelProperty(value = "物流名称")
    private String shippingName;
    @Digits(groups = Modify.class, integer = 7, fraction = 2, message = "运费金额格式长度超出范围")
    @DecimalMin(groups = Modify.class, value = "0.00", message = "运费金额不能小于0.00")
    @DecimalMax(groups = Modify.class, value = "9999999", message = "运费金额不能大于9999999")
    @NotNull(groups = Modify.class, message = "运费金额不能为空")
    @ApiModelProperty(value = "运费")
    private BigDecimal shippingPrice;
    @ApiModelProperty(value = "发货时间")
    private Long shippingTime;
    @ApiModelProperty(value = "支付code")
    private String payCode;
    @ApiModelProperty(value = "支付方式名称")
    private String payName;
    @ApiModelProperty(value = "发票抬头")
    private String invoiceTitle;
    @ApiModelProperty(value = "纳税人识别号")
    private String taxpayer;
    @ApiModelProperty(value = "商品总价")
    private BigDecimal goodsPrice;
    @ApiModelProperty(value = "余额抵扣金额")
    private BigDecimal userMoney;
    @ApiModelProperty(value = "优惠券抵扣金额")
    private BigDecimal couponPrice;
    @ApiModelProperty(value = "抵扣积分")
    private Integer integral;
    @ApiModelProperty(value = "积分抵扣金额")
    private BigDecimal integralMoney;
    @ApiModelProperty(value = "应付款金额")
    private BigDecimal orderAmount;
    @ApiModelProperty(value = "订单总价")
    private BigDecimal totalAmount;
    @ApiModelProperty(value = "订金（预售）")
    private BigDecimal paidMoney;
    @ApiModelProperty(value = "下单时间")
    private Long addTime;
    @ApiModelProperty(value = "收货确认时间")
    private Long confirmTime;
    @ApiModelProperty(value = "支付时间")
    private Long payTime;
    @ApiModelProperty(value = "第三方平台交易流水号")
    private String transactionId;
    @ApiModelProperty(value = "活动表主键")
    private Integer promId;
    @ApiModelProperty(value = "活动类型（0默认4预售5虚拟6拼团）")
    private Integer promType;
    @ApiModelProperty(value = "订单优惠表主键")
    private Integer orderPromId;
    @ApiModelProperty(value = "订单优惠金额")
    private BigDecimal orderPromAmount;

    @Digits(groups = Modify.class, integer = 7, fraction = 2, message = "价格调整格式长度超出范围")
    @DecimalMax(groups = Modify.class, value = "9999999", message = "价格调整不能大于9999999")
    @NotNull(groups = Modify.class, message = "价格调整不能为空")
    @ApiModelProperty(value = "价格调整")
    private BigDecimal discount;
    @ApiModelProperty(value = "用户下单备注")
    private String userNote;
    @ApiModelProperty(value = "管理员处理订单备注")
    private String adminNote;
    @ApiModelProperty(value = "父单单号")
    private String parentSn;
    @ApiModelProperty(value = "店铺表主键")
    private Integer storeId;
    @ApiModelProperty(value = "记录是属于哪个店铺下的订单，store_id是属于哪个d店铺发货")
    private Integer orderStoreId;
    @ApiModelProperty(value = "记是否评价（0：未评价；1：已评价）")
    private Integer isComment;
    @ApiModelProperty(value = "门店表主键")
    private Integer shopId;
    @ApiModelProperty(value = "用户假删除标识,1:删除,0未删除")
    private Integer deleted;
    @ApiModelProperty(value = "结算表主键，0为未结算")
    private Integer orderStatisId;

    @ApiModelProperty(value = "下一个购买订单的id")
    private Integer nextOrderId;

    /**
     * 售出价格
     */
    private BigDecimal sellPrice;
    private Long sellTime;
    /**
     * 申请提货时间
     */
    private Long applyDelivery;

    //用于前端UI显示订单5个状态进度. 1: 提交订单;2:订单支付; 3: 商家发货; 4: 确认收货; 5: 订单完成
    @TableField(exist = false)
    private Integer showStatus;
    public Integer getShowStatus(){
        if(isComment != null && isComment == 1){
            return 5;
        }else if(orderStatus != null && orderStatus == 2){
            return 4;
        }else if(shippingStatus != null && shippingStatus == 1
                && orderStatus != null && orderStatus == 1){
            return 3;
        }else if(((payStatus != null && payStatus == 1) || "cod".equals(payCode))
                && shippingStatus != null && shippingStatus != 1
                && (orderStatus != null && (orderStatus == 0 || orderStatus == 1))){
            return 2;
        }
//        else if(payStatus != null && payStatus == 0
//                && orderStatus != null && orderStatus == 0
//                && !payCode.equals("cod")){
//            return 1;
//        }
        return 1;
    }

    @TableField(exist = false)
    private String shippingTimeDesc;
    public String getShippingTimeDesc(){
        if (shippingTime != null) {
            return TimeUtil.transForDateStr(shippingTime, "yyyy_MM-dd HH:mm:ss");
        }
        return shippingTimeDesc;
    }

    @TableField(exist = false)
    private String confirmTimeDesc;
    public String getConfirmTimeDesc(){
        if (confirmTime != null) {
            return TimeUtil.transForDateStr(confirmTime, "yyyy-MM-dd HH:mm:ss");
        }
        return confirmTimeDesc;
    }

    @TableField(exist = false)
    private String consigneeDesc;
    public String getConsigneeDesc(){
        if (consignee != null) {
            return consignee + ":" + mobile;
        }
        return consigneeDesc;
    }

    @TableField(exist = false)
    private String shippingStatusDetail;
    public String getShippingStatusDetail(){
        if(null != shippingStatus){
            switch (this.shippingStatus){
                case 0:
                    shippingStatusDetail = "未发货";
                    break;
                case 1:
                    shippingStatusDetail = "已发货";
                    break;
                case 2:
                    shippingStatusDetail = "部分发货";
                    break;
            }
        }
        return shippingStatusDetail;
    }

    @TableField(exist = false)
    private String promTypeDesc;
    public String getPromTypeDesc(){
        if (promType != null){
            if (promType == 4){
                return "预售订单";
            }else if (promType == 5){
                return "虚拟订单";
            }else if (promType == 6){
                return "拼团订单";
            }else{
                return "普通订单";
            }
        }
        return null;
    }

    @TableField(exist = false)
    private String addTimeDetail;
    public String getAddTimeDetail(){
        if (addTime != null) {
            return TimeUtil.transForDateStr(addTime, "yyyy-MM-dd HH:mm:ss");
        }
        return addTimeDetail;
    }

    @TableField(exist = false)
    private String payStatusDetail;
    public String getPayStatusDetail(){
        if(null != payStatus){
            switch (this.payStatus) {
                case 0:
                    payStatusDetail = "待支付";
                    break;
                case 1:
                    payStatusDetail = "已支付";
                    break;
                case 2:
                    payStatusDetail = "部分支付";
                    break;
                case 3:
                    payStatusDetail = "已退款";
                    break;
                case 4:
                    payStatusDetail = "拒绝退款";
                    break;
            }
        }
        return payStatusDetail;
    }

    @TableField(exist = false)
    private String payStatusRefundDesc;
    public String getPayStatusRefundDesc(){
        if (payStatus != null){
            switch (payStatus){
                case 1:
                case 2:
                    return "待处理";
                case 3:
                    return "已退款";
                case 4:
                    return "拒绝退款";
            }
        }
        return payStatusRefundDesc;
    }

    @TableField(exist = false)
    private String payTimeDetail;
    public String getPayTimeDetail(){
        if(null != payTime){
            payTimeDetail = TimeUtil.transForDateStr(this.payTime, "yyyy-MM-dd HH:mm:ss");
        }
        return payTimeDetail;
    }

    @TableField(exist = false)
    private String shippingTimeDetail;
    public String getShippingTimeDetail(){
        if (shippingTime != null) {
            return TimeUtil.transForDateStr(shippingTime, "yyyy-MM-dd HH:mm:ss");
        }
        return shippingTimeDetail;
    }

    @ApiModelProperty(value = "子订单列表节点")
    @TableField(exist = false)
    private List<Order> orderList;

    @TableField(exist = false)
    private Integer goodsNum;

    @TableField(exist = false)
    private Integer orderGoodsCount;
    public Integer getOrderGoodsCount(){
        if(orderGoods != null){
            orderGoodsCount = orderGoods.size();
        }
        return orderGoodsCount;
    }

    @ApiModelProperty(value = "优惠金额")
    @TableField(exist = false)
    private BigDecimal cutFee;

    @ApiModelProperty(value = "订单支付密码")
    @TableField(exist = false)
    private String payPwd;

    @ApiModelProperty(value = "订单列表备注")
    @TableField(exist = false)
    private Map<Integer, String> userNotes;

    @ApiModelProperty(value = "订单商品列表节点")
    @TableField(exist = false)
    private List<OrderGoods> orderGoods;

    @ApiModelProperty(value = "存证信息")
    @TableField(exist = false)
    private List<DepositCertificate> depositCertificates;

    @ApiModelProperty(value = "订单三级地址列表节点")
    @TableField(exist = false)
    private List<Region> regions;

    @ApiModelProperty(value = "店铺对象")
    @TableField(exist = false)
    private Store store;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private String storeName;

    @TableField(exist = false)
    private String orderStatusDetail;
    public String getOrderStatusDetail(){
        if(payStatus != null && orderStatus != null && shippingStatus != null){
            orderStatusDetail = "待处理";
            if(orderStatus == 0){
                if(payStatus == 0){
                    orderStatusDetail = "待支付";
                }
                if(payStatus == 1){
                    orderStatusDetail = "待发货";
                }
                if(payStatus == 2){
                    orderStatusDetail = "已付订金";
                }
            }
            if(orderStatus == 1){
                if(shippingStatus == 0){
                    orderStatusDetail = "待发货";
                }
                if(shippingStatus == 1){
                    orderStatusDetail = "待收货";
                }
            }
            if(orderStatus == 2){
                orderStatusDetail = "待评价";
            }
            if(orderStatus == 3){
                orderStatusDetail = "已取消";
                if(payStatus == 3){
                    orderStatusDetail = "已取消(已退款)";
                }
            }
            if(orderStatus == 4){
                orderStatusDetail = "已完成";
            }
            if(orderStatus == 5){
                orderStatusDetail = "已关闭";
            }
        }
        return orderStatusDetail;
    }

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
                    return "已收货";
                case 3:
                    return "已取消";
                case 4:
                    return "已完成";
                case 5:
                    return "已关闭";
            }
        }
        return "";
    }

    @TableField(exist = false)
    private Boolean isAblePay;//用户是否可以支付 预售的需要和预售活动数据才能得出。
    public Boolean getIsAblePay(){
        if(payStatus != null && orderStatus != null){
            isAblePay = orderStatus == 0 && payStatus == 0;
        }
        if(addTime != null && ((System.currentTimeMillis() / 1000) - addTime) > 86400 * 90){
            isAblePay = false;
        }
        return isAblePay;
    }

    @TableField(exist = false)
    private Boolean isAbleCancel;//用户是否可以取消订单
    public Boolean getIsAbleCancel(){
        if(orderStatus != null && shippingStatus != null && promType != null){
            isAbleCancel = false;
            //预售订单不能取消订单，支付订金，支付尾款都不行
            if((orderStatus == 0 || orderStatus == 1) && (shippingStatus == 0 || shippingStatus == 1) && promType != 4){
                isAbleCancel = true;
            }
        }
        if(addTime != null && ((System.currentTimeMillis() / 1000) - addTime) > 86400 * 90){
            isAbleCancel = false;
        }
        return isAbleCancel;
    }

    @TableField(exist = false)
    private Boolean isAbleCancelPay;//商家 平台是否可以退款取消订单
    public Boolean getIsAbleCancelPay(){
        if(orderStatus != null && shippingStatus != null && promType != null){
            isAbleCancel = false;
            //预售订单不能取消订单，支付订金，支付尾款都不行
            if((orderStatus == 0 || orderStatus == 1) && (shippingStatus == 0 || shippingStatus == 1) && promType != 4){
                isAbleCancel = true;
            }
        }
        if(addTime != null && ((System.currentTimeMillis() / 1000) - addTime) > 86400 * 90){
            isAbleCancel = false;
        }
        return isAbleCancel;
    }

    @TableField(exist = false)
    private Boolean isAbleReceive;//用户是否可以收货
    public Boolean getIsAbleReceive(){
        if(payStatus != null && orderStatus != null && shippingStatus != null){
            isAbleReceive = payStatus == 1 && orderStatus == 1 && shippingStatus == 1;
        }
        if(addTime != null && ((System.currentTimeMillis() / 1000) - addTime) > 86400 * 90){
            isAbleReceive = false;
        }
        return isAbleReceive;
    }

    @TableField(exist = false)
    private Boolean isAbleComment;//用户是否可以评价
    public Boolean getIsAbleComment(){
        if(orderStatus != null && isComment != null){
            isAbleComment = orderStatus == 2 && isComment == 0;
        }
        if(addTime != null && ((System.currentTimeMillis() / 1000) - addTime) > 86400 * 90){
            isAbleComment = false;
        }
        return isAbleComment;
    }

    @TableField(exist = false)
    private Boolean isAbleShipping;//用户是否可以查看物流
    public Boolean getIsAbleShipping(){
        if(shippingStatus != null && orderStatus != null){
            isAbleShipping = shippingStatus > 0 && orderStatus == 1;
        }
        if(addTime != null && ((System.currentTimeMillis() / 1000) - addTime) > 86400 * 90){
            isAbleShipping = false;
        }
        return isAbleShipping;
    }

    @TableField(exist = false)
    private Boolean isAbleReturn;//用户是否可以退货
    public Boolean getIsAbleReturn(){
        if(orderStatus != null && shippingStatus != null){
            isAbleReturn = orderStatus == 1 && (shippingStatus == 1 || shippingStatus == 2);
            if(orderStatus == 2 || orderStatus == 4){
                isAbleReturn = true;
            }
        }
        if(addTime != null && ((System.currentTimeMillis() / 1000) - addTime) > 86400 * 90){
            isAbleReturn = false;
        }
        return isAbleReturn;
    }

    @TableField(exist = false)
    private Boolean isAbleInvalid;//卖家是否可以无效订单
    public Boolean getIsAbleInvalid(){
        if(orderStatus != null && payStatus != null){
            isAbleInvalid = orderStatus != 5 && payStatus != 1;
        }
        if(addTime != null && ((System.currentTimeMillis() / 1000) - addTime) > 86400 * 90){
            isAbleInvalid = false;
        }
        return isAbleInvalid;
    }

    @TableField(exist = false)
    private Boolean isAbleRefund;//卖家是否可以退款订单
    public Boolean getIsAbleRefund(){
        if(promType != null && orderStatus != null && payStatus != null){
            isAbleRefund = (promType != 4 && promType != 6) && orderStatus == 0 && payStatus == 1;
        }
        if(addTime != null && ((System.currentTimeMillis() / 1000) - addTime) > 86400 * 90){
            isAbleRefund = false;
        }
        return isAbleRefund;
    }

    @TableField(exist = false)
    private Boolean isAbleAdminRefund;//平台是否可以退款订单
    public Boolean getIsAbleAdminRefund(){
        if(promType != null && orderStatus != null && payStatus != null){
            isAbleRefund = (promType != 4 && promType != 6) && orderStatus == 3 && payStatus == 1;
        }
        if(addTime != null && ((System.currentTimeMillis() / 1000) - addTime) > 86400 * 90){
            isAbleRefund = false;
        }
        return isAbleRefund;
    }

    @TableField(exist = false)
    private Boolean isAbleConfirm;//卖家是否可以确认订单
    public Boolean getIsAbleConfirm(){
        if(promType != null && orderStatus != null && payStatus != null){
            isAbleConfirm = (promType != 4 && promType != 6) && orderStatus == 0 && payStatus == 1;
        }
        if(addTime != null && ((System.currentTimeMillis() / 1000) - addTime) > 86400 * 90){
            isAbleConfirm = false;
        }
        return isAbleConfirm;
    }

    @TableField(exist = false)
    private Boolean isAbleCancelConfirm;//卖家是否可以取消确认订单
    public Boolean getIsAbleCancelConfirm(){
        if(orderStatus != null && payStatus != null && shippingStatus != null && promType != null){
            isAbleCancelConfirm = promType != 6 && orderStatus == 1 && payStatus == 1 && shippingStatus == 0;
        }
        if(addTime != null && ((System.currentTimeMillis() / 1000) - addTime) > 86400 * 90){
            isAbleCancelConfirm = false;
        }
        return isAbleCancelConfirm;
    }

    @TableField(exist = false)
    private Boolean isAbleDelivery;//卖家是否可以发货
    public Boolean getIsAbleDelivery(){
        if(orderStatus != null && payStatus != null && shippingStatus != null){
            isAbleDelivery = orderStatus == 1 && payStatus == 1 && (shippingStatus == 0 || shippingStatus == 2);
        }
        if(addTime != null && ((System.currentTimeMillis() / 1000) - addTime) > 86400 * 90){
            isAbleDelivery = false;
        }
        return isAbleDelivery;
    }

    @TableField(exist = false)
    private Boolean isAbleModify;//卖家是否可以修改订单
    public Boolean getIsAbleModify(){
        if(orderStatus != null && payStatus != null && shippingStatus != null){
            isAbleModify = shippingStatus == 0 && payStatus == 0 && (orderStatus == 0 || orderStatus == 1);
        }
        if(addTime != null && ((System.currentTimeMillis() / 1000) - addTime) > 86400 * 90){
            isAbleModify = false;
        }
        return isAbleModify;
    }

    @TableField(exist = false)
    private Boolean isAbleRefundBack;//是否支持原路退回
    public Boolean getIsAbleRefundBack(){
        if(null != payCode){
            isAbleRefundBack = OrderConstant.getThirdPayCode().contains(payCode);
        }
        if(addTime != null && ((System.currentTimeMillis() / 1000) - addTime) > 86400 * 90){
            isAbleRefundBack = false;
        }
        return isAbleRefundBack;
    }

    @ApiModelProperty(value = "尾款金额（预售）")
    @TableField(exist = false)
    private BigDecimal tailAmount;
    public BigDecimal getTailAmount(){
        if(promType != null && payStatus != null && totalAmount != null && paidMoney != null && orderAmount != null){
            if(promType == 4){
                if(payStatus == 2){
                    //支付订金
                    tailAmount = totalAmount.subtract(paidMoney);
                }
                if(payStatus == 0){
                    tailAmount = orderAmount;
                }
                if(payStatus == 1){
                    tailAmount = orderAmount.add(userMoney);
                }
            }
        }
        return tailAmount;
    }

    //省市区地址
    @TableField(exist = false)
    private String provinceName;
    @TableField(exist = false)
    private String cityName;
    @TableField(exist = false)
    private String districtName;
    @TableField(exist = false)
    private String twonName;

    @TableField(exist = false)
    private DeliveryDoc deliveryDoc;//订单物流信息
    @TableField(exist = false)
    private Integer totalGoodsCount;//所有商品数量
    @TableField(exist = false)
    private Integer unsend;//是否申请退换货
    @TableField(exist = false)
    private String invoiceNo;//接收订单配送单号
    @TableField(exist = false)
    private Integer sellerId;//用于消息队列，订单日志（表设计的辣鸡）

    /**
     * 出售人，没有店铺的出售者
     */
    @TableField(exist = false)
    private Integer sellUserId;

    /**
     * 出售后，购买的订单
     */
    @TableField(exist = false)
    private Order nextOrder;

    private static final long serialVersionUID = 1L;
}