package com.soubao.entity.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.soubao.entity.Order;
import com.soubao.entity.Seller;
import com.soubao.entity.Store;
import com.soubao.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 提货订单实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickOrderVo {
    /**
     * 提货订单id
     */
    private Integer pickOrderId;

    /**
     * 购买订单id
     */
    private Integer orderId;

    /**
     * 购买订单
     */
    @TableField(exist = false)
    private Order order;
    /**
     * 申请提货时间
     */
    private Long addTime;
    /**
     * 提货订单状态（0：待发货/提货中 1:确认发货 2：已发货/提货中 3：已完成/已提货）
     */
    private Integer pickOrderStatus;
    /**
     * 提货人
     */
    private Integer userId;

    /**
     * 提货人
     */
    @TableField(exist = false)
    private User user;

    private String nickname;

    /**
     * 发货人
     */
    private Integer sellerId;

    /**
     * 发货人
     */
    @TableField(exist = false)
    private Seller seller;

    /**
     * 提货订单编码
     */
    private String pickOrderSn;

    /**
     * 收货人id
     */
    private String consigneeId;
    /**
     * 收货人
     */
    @TableField(exist = false)
    private User consigneeUser;

    /**
     * 国家
     */
    private Integer country;
    /**
     * 省
     */
    private Integer province;
    /**
     * 市
     */
    private Integer city;
    /**
     * 区
     */
    private Integer district;
    /**
     * 街道
     */
    private Integer twon;
    /**
     * 地址
     */
    private String address;
    /**
     * 邮编
     */
    private String zipcode;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 物流code
     */
    private String shippingCode;

    /**
     * 物流名称
     */
    private String shippingName;
    /**
     * 邮费
     */
    private BigDecimal shippingPrice;

    /**
     * 物流最新时间
     */
    private Integer shippingTime;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品图片
     */
    private String originalImg;
    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;
    /**
     * 存证id
     */
    private Integer dcId;

    /**
     * 提货状态时间
     */
    private Long updateTime;
    /**
     * 商店
     */
    private Store store;

}
