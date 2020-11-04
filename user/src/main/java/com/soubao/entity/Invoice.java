package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.soubao.common.utils.TimeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 发票信息表
 * </p>
 *
 * @author dyr
 * @since 2019-11-22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "invoice_id", type = IdType.AUTO)
    private Integer invoiceId;
    private Integer orderId;
    private String orderSn;
    private Integer userId;
    private Integer storeId;
    private Integer invoiceType;
    private BigDecimal invoiceMoney;
    private String invoiceTitle;
    private String invoiceDesc;
    private BigDecimal invoiceRate;
    private String taxpayer;
    private Integer status;
    private Long atime;
    private Long ctime;

    @TableField(exist = false)
    private String invoiceTypeDesc;

    public String getInvoiceTypeDesc() {
        if (invoiceType != null) {
            if (invoiceType == 1) {
                return "电子发票";
            } else if (invoiceType == 2) {
                return "增值税发票";
            } else {
                return "普通发票";
            }
        }
        return null;
    }

    @TableField(exist = false)
    private String statusDesc;

    public String getStatusDesc() {
        if (status != null) {
            if (status == 1) {
                return "已开";
            } else if (status == 2) {
                return "作废";
            } else {
                return "待开";
            }
        }
        return null;
    }

    @TableField(exist = false)
    private String atimeDesc;

    public String getAtimeDesc() {
        if (atime != null) {
            if (this.atime > 0) {
                return TimeUtil.transForDateStr(this.atime, "yyyy-MM-dd HH:mm:ss");
            }
        }
        return null;
    }

    @TableField(exist = false)
    private String ctimeDesc;

    public String getCtimeDesc() {
        if (ctime != null) {
            return TimeUtil.transForDateStr(this.ctime, "yyyy-MM-dd HH:mm:ss");
        }
        return ctimeDesc;
    }

    @TableField(exist = false)
    private String nickname;
    @TableField(exist = false)
    private String storeName;

}
