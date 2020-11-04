package com.soubao.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提货订单实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pick_order")
@ApiModel
public class PickOrder {
    /**
     * 提货订单id
     */
    @Id
    @TableId(value = "pick_order_id", type = IdType.AUTO)
    @ApiModelProperty("提货订单id")
    private Integer pickOrderId;

    /**
     * 购买订单id
     */
    @ApiModelProperty("购买订单id")
    private Integer orderId;

    /**
     * 购买订单
     */
    @TableField(exist = false)
    private Order order;
    /**
     * 申请提货时间
     */
    @ApiModelProperty("申请提货时间")
    private Long addTime;
    /**
     * 提货订单状态（0：待发货/提货中 1:确认发货 2：已发货/提货中 3：已完成/已提货）
     */
    @ApiModelProperty("提货订单状态（0：待发货/提货中 1:确认发货 2：已发货/提货中 3：已完成/已提货）")
    private Integer pickOrderStatus;
    /**
     * 提货人
     */
    @ApiModelProperty("提货人id")
    private Integer userId;

    /**
     * 提货人
     */
    @TableField(exist = false)
    private User user;

    @ApiModelProperty("提货人")
    private String nickname;

    /**
     * 发货人
     */
    @ApiModelProperty("发货人id")
    private Integer sellerId;

    /**
     * 发货人
     */
    @TableField(exist = false)
    private Seller seller;

    /**
     * 提货订单编码
     */
    @ApiModelProperty("提货订单编码")
    private String pickOrderSn;

    /**
     * 收货人id
     */
    @ApiModelProperty("收货人id")
    private Integer consigneeId;
    /**
     * 收货人
     */
    @TableField(exist = false)
    private User consigneeUser;

    /**
     * 国家
     */
    @ApiModelProperty("国家")
    private Integer country;
    /**
     * 省
     */
    @ApiModelProperty("省")
    private Integer province;
    /**
     * 市
     */
    @ApiModelProperty("市")
    private Integer city;
    /**
     * 区
     */
    @ApiModelProperty("区")
    private Integer district;
    /**
     * 街道
     */
    @ApiModelProperty("街道")
    private Integer twon;
    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;
    /**
     * 邮编
     */
    @ApiModelProperty("邮编")
    private String zipcode;

    /**
     * 电话
     */
    @ApiModelProperty("电话")
    private String mobile;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 物流code
     */
    @ApiModelProperty("物流code")
    private String shippingCode;

    /**
     * 物流名称
     */
    @ApiModelProperty("物流名称")
    private String shippingName;
    /**
     * 邮费
     */
    @ApiModelProperty("邮费")
    private BigDecimal shippingPrice;

    /**
     * 物流最新时间
     */
    @ApiModelProperty("物流最新时间")
    private Long shippingTime;

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Integer goodsId;

    /**
     * 商品
     */
    @TableField(exist = false)
    private List<OrderGoods> orderGoods;

    /**
     * 提货状态时间
     */
    @ApiModelProperty("提货状态时间")
    private Long updateTime;

    /**
     * 提货类型
     * 0. 手工单号
     * 1. 预约到店
     * 2. 电子面单
     * 3. 无需物流
     */
    private Integer pickOrderType;

    /**
     * 商店
     */
    @TableField(exist = false)
    private Store store;
}
