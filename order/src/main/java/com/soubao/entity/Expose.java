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
 * 举报表
 * </p>
 *
 * @author dyr
 * @since 2020-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("expose")
public class Expose implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 举报id
     */
    @TableId(value = "expose_id", type = IdType.AUTO)
    private Integer exposeId;

    /**
     * 举报人id
     */
    private Integer exposeUserId;

    /**
     * 举报人会员名
     */
    private String exposeUserName;

    /**
     * 相关订单号
     */
    private String exposeOrderSn;

    /**
     * 被举报的商品id
     */
    private Integer exposeGoodsId;

    /**
     * 被举报的商品名称
     */
    private String exposeGoodsName;

    /**
     * 举报类型
     */
    private Integer exposeTypeId;

    /**
     * 举报类型名称
     */
    private String exposeTypeName;

    /**
     * 举报主题id
     */
    private Integer exposeSubjectId;

    /**
     * 举报主题
     */
    private String exposeSubjectName;

    /**
     * 举报信息
     */
    private String exposeContent;

    /**
     * 图片1
     */
    private String exposePic;

    /**
     * 举报时间
     */
    private Long exposeTime;

    /**
     * 被举报商品的店铺id
     */
    private Integer exposeStoreId;

    /**
     * 店铺名
     */
    private String exposeStoreName;

    /**
     * 举报状态(1未处理/2已处理)
     */
    private Integer exposeState;

    /**
     * 举报处理结果(1无效举报/2恶意举报/3有效举报)
     */
    private Integer exposeHandleType;

    /**
     * 举报处理信息
     */
    private String exposeHandleMsg;

    /**
     * 举报处理时间
     */
    private Integer exposeHandleTime;

    /**
     * 管理员id
     */
    private Integer exposeHandleAdminId;

    @TableField(exist = false)
    private String exposeTimeShow;

    public String getExposeTimeShow() {
        if (exposeTime != null){
            return TimeUtil.transForDateStr(exposeTime,"yyyy-MM-dd HH:mm:ss");
        }
        return exposeTimeShow;
    }

    @TableField(exist = false)
    private String exposeHandleTypeDetail;

    public String getExposeHandleTypeDetail() {
        if (exposeHandleType != null) {
            if (this.exposeHandleType == 1){
                return "无效举报";
            }
            if (this.exposeHandleType == 2){
                return "恶意举报";
            }
            if (this.exposeHandleType == 3){
                return "有效举报";
            }
        }
        return exposeHandleTypeDetail;
    }

    @TableField(exist = false)
    private String exposeStateDetail;

    public String getExposeStateDetail() {
        if (exposeState != null) {
            if (exposeState == 1) {
                return "未处理";
            }
            if (exposeState == 2) {
                return "已处理";
            }
        }
        return exposeStateDetail;
    }


}
