package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.utils.TimeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("store_reopen")
public class StoreReopen implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "re_id", type = IdType.AUTO)
    private Integer reId;


    /**
     * 店铺等级ID
     */
    private Integer reGradeId;

    /**
     * 等级名称
     */
    private String reGradeName;

    /**
     * 等级收费(元/年)
     */
    private BigDecimal reGradePrice;

    /**
     * 续签时长(年)
     */
    private Integer reYear;

    /**
     * 应付总金额
     */
    private BigDecimal rePayAmount;

    /**
     * 店铺名字
     */
    private String reStoreName;

    /**
     * 店铺ID
     */
    private Integer reStoreId;

    /**
     * 状态0默认，未上传凭证1审核中2审核通过
     */
    private Integer reState;

    /**
     * 有效期开始时间
     */
    private Long reStartTime;

    /**
     * 有效期结束时间
     */
    private Long reEndTime;

    /**
     * 记录创建时间
     */
    private Long reCreateTime;

    /**
     * 付款凭证
     */
    @NotBlank(message = "请上传支付凭证")
    private String rePayCert;

    /**
     * 付款凭证说明
     */
    private String rePayCertExplain;

    /**
     * 审核备注
     */
    private String adminNote;

    /**
     * 有效期起止时间描述
     */
    @TableField(exist = false)
    private String reCreateTimeDesc;

    public String getReCreateTimeDesc() {
        return TimeUtil.transForDateStr(this.reCreateTime, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 有效期起止时间描述
     */
    @TableField(exist = false)
    private String reStartToEndTimeDesc;

    public String getReStartToEndTimeDesc() {
        return TimeUtil.transForDateStr(this.reStartTime, "yyyy-MM-dd") + " 至 " + TimeUtil.transForDateStr(this.reEndTime, "yyyy-MM-dd");
    }

    /**
     * 状态描述
     */
    @TableField(exist = false)
    private String reStateDesc;

    public String getReStateDesc() {
        //状态0默认，未上传凭证1审核中2审核通过
        if (this.reState != null) {
            switch (this.reState) {
                case -1:
                    return "审核拒绝";
                case 0:
                    return "未上传凭证";
                case 1:
                    return "审核中";
                case 2:
                    return "审核通过";
            }
        }
        return reStateDesc;
    }


}
