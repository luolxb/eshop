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
 * 消费者保障服务申请表
 * </p>
 *
 * @author dyr
 * @since 2020-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("guarantee_apply")
public class GuaranteeApply implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 保障项目ID
     */
    private Integer grtId;

    /**
     * 店铺ID
     */
    private Integer storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 申请时间
     */
    private Long addTime;

    /**
     * 审核状态 0未审核 1审核通过 2审核失败 3保证金待审核 4保证金审核通过 5保证金审核失败
     */
    private Integer auditstate;

    /**
     * 保证金金额
     */
    private BigDecimal cost;

    /**
     * 保证金付款凭证图片
     */
    private String costimg;

    /**
     * 申请类型0退出1加入
     */
    private Integer applyType;


    @TableField(exist = false)
    private String addTimeDesc;

    public String getAddTimeDesc() {
        if (addTime != null) {
            return TimeUtil.transForDateStr(this.addTime, "yyyy-MM-dd HH:mm:ss");
        }
        return addTimeDesc;
    }

    @TableField(exist = false)
    private String grtName;//保障服务名称

    @TableField(exist = false)
    private String auditstateDesc;

    public String getAuditstateDesc() {
        if (this.auditstate != null) {
            switch (this.auditstate) {
                case 0:
                    return "等待审核";
                case 1:
                    return "审核通过，待支付保证金";
                case 2:
                    return "审核失败";
                case 3:
                    return "保证金待审核";
                case 4:
                    return "保证金审核通过";
                case 5:
                    return "保证金审核失败";
            }
        }
        return null;
    }

}
