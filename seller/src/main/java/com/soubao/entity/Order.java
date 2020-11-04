package com.soubao.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Order {
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

    //用于前端UI显示订单5个状态进度. 1: 提交订单;2:订单支付; 3: 商家发货; 4: 确认收货; 5: 订单完成
    private Integer showStatus;

    private String shippingTimeDesc;

    private String confirmTimeDesc;

    private String consigneeDesc;

    private String shippingStatusDetail;

    private String promTypeDesc;

    private String addTimeDetail;

    private String payStatusDetail;

    private String payStatusRefundDesc;

    private String payTimeDetail;

    private String shippingTimeDetail;

    @ApiModelProperty(value = "子订单列表节点")
    private List<Order> orderList;

    private Integer goodsNum;

    private Integer orderGoodsCount;

    @ApiModelProperty(value = "优惠金额")
    private BigDecimal cutFee;

    @ApiModelProperty(value = "订单支付密码")
    private String payPwd;

    @ApiModelProperty(value = "订单列表备注")
    private Map<Integer, String> userNotes;

//    @ApiModelProperty(value = "订单商品列表节点")
//    private List<OrderGoods> orderGoods;

    @ApiModelProperty(value = "订单三级地址列表节点")
    private List<Region> regions;

    @ApiModelProperty(value = "店铺对象")
    private Store store;

    private User user;

    private String storeName;

    private String orderStatusDetail;

    private String orderStatusDesc;

    private Boolean isAblePay;//用户是否可以支付 预售的需要和预售活动数据才能得出。

    private Boolean isAbleCancel;//用户是否可以取消

    private Boolean isAbleReceive;//用户是否可以收货

    private Boolean isAbleComment;//用户是否可以评价

    private Boolean isAbleShipping;//用户是否可以查看物流

    private Boolean isAbleReturn;//用户是否可以退货

    private Boolean isAbleInvalid;//卖家是否可以无效订单

    private Boolean isAbleRefund;//卖家是否可以退款订单

    private Boolean isAbleConfirm;//卖家是否可以确认订单

    private Boolean isAbleCancelConfirm;//卖家是否可以取消确认订单

    private Boolean isAbleDelivery;//卖家是否可以发货

    private Boolean isAbleModify;//卖家是否可以修改订单

    @ApiModelProperty(value = "尾款金额（预售）")
    private BigDecimal tailAmount;

    //省市区地址
    private String provinceName;
    private String cityName;
    private String districtName;
    private String twonName;

    private DeliveryDoc deliveryDoc;//订单物流信息
    private Integer totalGoodsCount;//所有商品数量
    private Integer unsend;//是否申请退换货
    private String invoiceNo;//接收订单配送单号
}