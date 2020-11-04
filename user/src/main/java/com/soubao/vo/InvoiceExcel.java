package com.soubao.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.soubao.common.utils.TimeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class InvoiceExcel extends BaseRowModel {
    @ExcelProperty(value = {"发票编号"}, index = 0)
    private String invoiceId;

    @ExcelProperty(value = {"订单编号"}, index = 1)
    private String orderSn;

    @ExcelProperty(value = {"用户名"}, index = 2)
    private String nickname;

    @ExcelProperty(value = {"店铺"}, index = 3)
    private String storeName;

    private Integer invoiceType;

    private Integer userId;

    private Integer storeId;

    @ExcelProperty(value = {"发票类型"}, index = 4)
    private String invoiceTypeDesc;

    public String getInvoiceTypeDesc() {
        if (invoiceType != null) {
            switch (invoiceType) {
                case 1:
                    return "电子发票";
                case 2:
                    return "增值税发票";
                default:
                    return "普通发票";
            }
        }
        return invoiceTypeDesc;
    }

    @ExcelProperty(value = {"开票金额"}, index = 5)
    private String invoiceMoney;

    @ExcelProperty(value = {"抬头"}, index = 6)
    private String invoiceTitle;

    @ExcelProperty(value = {"发票内容"}, index = 7)
    private String invoiceDesc;

    @ExcelProperty(value = {"发票税率"}, index = 8)
    private String invoiceRate;

    @ExcelProperty(value = {"纳税人识别号"}, index = 9)
    private String taxpayer;

    private Integer status;

    @ExcelProperty(value = {"状态"}, index = 10)
    private String statusDesc;

    public String getStatusDesc() {
        if (status != null) {
            switch (status) {
                case 1:
                    return "已开";
                case 2:
                    return "作废";
                default:
                    return "待开";
            }
        }
        return statusDesc;
    }

    private Long atime;

    @ExcelProperty(value = {"创建时间"}, index = 11)
    private String atimeDesc;

    public String getAtimeDesc() {
        if (atime != null) {
            return TimeUtil.transForDateStr(atime, "yyyy/MM/dd HH:mm");
        }
        return atimeDesc;
    }

    private Long ctime;

    @ExcelProperty(value = {"开票时间"}, index = 12)
    private String ctimeDesc;

    public String getCtimeDesc() {
        if (ctime != null) {
            return TimeUtil.transForDateStr(this.ctime, "yyyy/MM/dd HH:mm");
        }
        return ctimeDesc;
    }

}
