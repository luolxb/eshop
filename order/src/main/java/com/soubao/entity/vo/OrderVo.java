package com.soubao.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.soubao.entity.Order;
import com.soubao.entity.PickOrder;
import com.soubao.entity.Store;
import com.soubao.validation.group.order.Modify;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderVo implements Serializable {
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
    @ApiModelProperty(value = "支付状态(0待支付，1已支付，2部分支付，3已退款，4拒绝退款)")
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
    @ApiModelProperty(value = "第三方平台流水号")
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

//
//    @ApiModelProperty(value = "订单商品列表节点")
//    @TableField(exist = false)
//    private List<OrderGoods> orderGoods;
//

    @ApiModelProperty(value = "店铺对象")
    @TableField(exist = false)
    private Store store;

    @ApiModelProperty(value = "提货订单")
    @TableField(exist = false)
    private PickOrder pickOrder;

   @ApiModelProperty(value = "卖家订单")
    @TableField(exist = false)
    private Order sellerOrder;


    @TableField(exist = false)
    private String originalImg;


    @TableField(exist = false)
    private String goodsName;

    /**
     * 存证的id
     */
    private Integer dcId;

    private String buyName;



    private static final long serialVersionUID = 1L;
}