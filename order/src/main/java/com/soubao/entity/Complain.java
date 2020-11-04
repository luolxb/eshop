package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.soubao.common.utils.TimeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 投诉表
 * </p>
 *
 * @author dyr
 * @since 2020-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("complain")
public class Complain implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 投诉id
     */
    @TableId(value = "complain_id", type = IdType.AUTO)
    private Integer complainId;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 订单商品ID
     */
    private Integer orderGoodsId;

    /**
     * 原告id
     */
    private Integer userId;

    /**
     * 原告名称
     */
    private String userName;

    /**
     * 原告联系方式
     */
    private String userContact;

    /**
     * 被告id
     */
    private Integer storeId;

    /**
     * 被告名称
     */
    private String storeName;

    /**
     * 投诉主题
     */
    private String complainSubjectName;

    /**
     * 投诉主题id
     */
    private Integer complainSubjectId;

    /**
     * 投诉内容
     */
    private String complainContent;

    /**
     * 投诉图片
     */
    private String complainPic;

    /**
     * 投诉时间
     */
    private Long complainTime;

    /**
     * 投诉处理时间
     */
    private Integer complainHandleTime;

    /**
     * 投诉处理人id
     */
    private Integer complainHandleAdminId;

    /**
     * 申诉内容
     */
    private String appealMsg;

    /**
     * 申诉时间
     */
    private Long appealTime;

    /**
     * 申诉图片
     */
    private String appealPic;

    /**
     * 最终处理意见
     */
    private String finalHandleMsg;

    /**
     * 最终处理时间
     */
    private Long finalHandleTime;

    /**
     * 最终处理人id
     */
    private Integer finalHandleAdminId;

    /**
     * 投诉状态(1待处理2对话中3待仲裁4已完成)
     */
    private Integer complainState;

    /**
     * 用户提交仲裁时间
     */
    private Integer userHandleTime;

    @TableField(exist = false)
    private String complainStateDetail;

    public String getComplainStateDetail() {
        if (this.complainState != null) {
            if (this.complainState == 1) {
                complainStateDetail = "待处理";
            }
            if (this.complainState == 2) {
                complainStateDetail = "对话中";
            }
            if (this.complainState == 3) {
                complainStateDetail = "待仲裁";
            }
            if (this.complainState == 4) {
                complainStateDetail = "已完成";
            }
        }
        return complainStateDetail;
    }

    @TableField(exist = false)
    private OrderGoods orderGoods;

    @TableField(exist = false)
    private Order order;

    @TableField(exist = false)
    private String goodsName;

    @TableField(exist = false)
    private String complainTimeShow;

    public String getComplainTimeShow() {
        return TimeUtil.transForDateStr(this.complainTime,"yyyy-MM-dd HH:mm:ss");
    }

    @TableField(exist = false)
    private String appealTimeShow;

    public String getAppealTimeShow() {
        return TimeUtil.transForDateStr(this.appealTime,"yyyy-MM-dd HH:mm:ss");
    }

    @TableField(exist = false)
    private String finalHandleTimeShow;

    public String getFinalHandleTimeShow() {
        return TimeUtil.transForDateStr(this.finalHandleTime,"yyyy-MM-dd HH:mm:ss");
    }


}
