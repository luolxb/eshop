package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
    private String title;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 商品ID
     */
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

}
