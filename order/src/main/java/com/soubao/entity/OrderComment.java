package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.utils.TimeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 订单评分表
 * </p>
 *
 * @author dyr
 * @since 2019-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_comment")
public class OrderComment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单评论索引id
     */
    @TableId(value = "order_commemt_id", type = IdType.AUTO)
    private Integer orderCommentId;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 店铺id
     */
    private Integer storeId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 描述相符分数（0~5）
     */
    private BigDecimal describeScore;

    /**
     * 卖家服务分数（0~5）
     */
    private BigDecimal sellerScore;

    /**
     * 物流服务分数（0~5）
     */
    private BigDecimal logisticsScore;

    /**
     * 评分时间
     */
    @TableField("commemt_time")
    private Long commentTime;

    /**
     * 不删除0；删除：1
     */
    private Integer deleted;

    @TableField(exist = false)
    private String commentTimeDesc;
    public String getCommentTimeDesc(){
        return TimeUtil.transForDateStr(this.commentTime, "yyyy-MM-dd HH:mm:ss");
    }

    @TableField(exist = false)
    private String nickname;

    @TableField(exist = false)
    private String storeName;

    @TableField(exist = false)
    private String orderSn;

    @TableField(exist = false)
    private String commentCount;

}
