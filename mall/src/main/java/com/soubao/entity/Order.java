package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.soubao.validation.group.order.Cart;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Setter
@Getter
@ApiModel(value = "订单对象", description = "order表")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;//mq要序列化
    @ApiModelProperty(value = "订单主键")
    private Integer orderId;
    @ApiModelProperty(value = "订单编号")
    private String orderSn;
    @ApiModelProperty(value = "用户主键")
    private Integer userId;
    @ApiModelProperty(value = "主订单编号")
    private String masterOrderSn;
    @ApiModelProperty(value = "订单状态(0待确认，1已确认，2已收货，3已取消，4已完成，5已作废)")
    private Integer orderStatus;
    @ApiModelProperty(value = "支付状态(0待支付，1已支付，2部分支付，3已退款，4拒绝退款)")
    private Integer payStatus;
    @ApiModelProperty(value = "发货状态(0未发货，1已发货，2部分发货)")
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
    /**
     * 以下是非数据库字段
     */
    @ApiModelProperty(value = "尾款金额（预售）")
    private BigDecimal tailAmount;

    @ApiModelProperty(value = "订单优惠描述")
    private String orderPromTitle;

    @ApiModelProperty(value = "子订单列表节点")
    private List<Order> orderList;

    @ApiModelProperty(value = "用户收货地址表主键")
//    @NotNull(groups = {Cart.class}, message = "请选择收货地址")
//    @Min(value = 0, groups = {Cart.class}, message = "请选择有效的收货地址")
    private Integer addressId;

    @ApiModelProperty(value = "优惠金额")
    private BigDecimal cutFee;

    @ApiModelProperty(value = "订单商品列表节点")
    private List<OrderGoods> orderGoods;

    @ApiModelProperty(value = "订单三级地址列表节点")
    private List<Region> regions;

    @ApiModelProperty(value = "订单三级地址描述")
    private String regionName;

    @ApiModelProperty(value = "订单商品总数")
    private Integer goodsNum;

    public Integer getGoodsNum() {
        if (goodsNum != null) {
            return goodsNum;
        }
        if (orderList != null && orderList.size() > 0 && orderList.get(0).getGoodsNum() != null) {
            goodsNum = orderList.stream().mapToInt(item -> item.getGoodsNum() == null ? 0 : item.getGoodsNum()).sum();
        }
        if (orderGoods != null && orderGoods.size() > 0 && orderGoods.get(0).getGoodsNum() != null) {
            goodsNum = orderGoods.stream().mapToInt(item -> item.getGoodsNum() == null ? 0 : item.getGoodsNum()).sum();
        }
        return goodsNum;
    }

    @ApiModelProperty(value = "订单支付密码")
    private String payPwd;

    @ApiModelProperty(value = "订单列表备注")
    private Map<Integer, String> userNotes;

    @ApiModelProperty(value = "用户地址对象")
    private UserAddress userAddress;

    @ApiModelProperty(value = "店铺对象")
    private Store store;

    @ApiModelProperty(value = "订单状态描述")
    private String orderStatusDetail;

    @ApiModelProperty(value = "订单状态描述:后台展示")
    @TableField(exist = false)
    private String orderStatusDesc;

    @ApiModelProperty(value = "用户是否可以支付")
    private Boolean isAblePay;

    @ApiModelProperty(value = "用户是否可以取消")
    private Boolean isAbleCancel;

    @ApiModelProperty(value = "用户是否可以收货")
    private Boolean isAbleReceive;

    @ApiModelProperty(value = "用户是否可以评价")
    private Boolean isAbleComment;

    @ApiModelProperty(value = "用户是否可以查看物流")
    private Boolean isAbleShipping;

    @ApiModelProperty(value = "用户是否可以退货")
    private Boolean isAbleReturn;

    @ApiModelProperty(value = "卖家是否可以无效订单")
    private Boolean isAbleInvalid;

    @ApiModelProperty(value = "卖家是否可以退款订单")
    private Boolean isAbleRefund;

    @ApiModelProperty(value = "卖家是否可以确认订单")
    private Boolean isAbleConfirm;

    @ApiModelProperty(value = "卖家是否可以取消确认订单")
    private Boolean isAbleCancelConfirm;

    @ApiModelProperty(value = "卖家是否可以发货")
    private Boolean getIsAbleDelivery;

    @ApiModelProperty(value = "订单可用的优惠券列表")
    private List<Coupon> usableCoupons;
    @ApiModelProperty(value = "订单优惠券列表，使用条件不够的")
    private List<Coupon> disableCoupons;
    @ApiModelProperty(value = "订单使用的优惠券id集合，逗号分隔")
    private String orderUseCouponIds;
    @ApiModelProperty(value = "拼团团长表主键")
    private Integer foundId;

    @ApiModelProperty(value = "支付状态描述")
    private String payStatusDetail;
    @ApiModelProperty(value = "发货状态描述")
    private String shippingStatusDetail;
    @ApiModelProperty(value = "下单时间起始")
    private Long addTimeStart;
    @ApiModelProperty(value = "下单时间截止")
    private Long addTimeEnd;
    @ApiModelProperty(value = "店铺id组")
    private Set<Integer> storeIds;
    @ApiModelProperty(value = "订单id组")
    private Set<Integer> orderIds;
    @ApiModelProperty(value = "拼团活动")
    private TeamActivity teamActivity;
    @ApiModelProperty(value = "拼团团员")
    private TeamFollow teamFollow;
    @ApiModelProperty(value = "拼团团长")
    private TeamFound teamFound;
    @ApiModelProperty(value = "优惠券id")
    private Integer couponListId;//坑，订单记录没有优惠券id，用于订单扣除

}