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
public class WithdrawalsExcel extends BaseRowModel {
    @ExcelProperty(value = {"申请人"}, index = 0)
    private String nickname;

    @ExcelProperty(value = {"提现金额"}, index = 1)
    private String money;

    @ExcelProperty(value = {"银行名称"}, index = 2)
    private String bankName;

    @ExcelProperty(value = {"银行账号"}, index = 3)
    private String bankCard;

    @ExcelProperty(value = {"开户人姓名"}, index = 4)
    private String realname;

    private Long createTime;

    @ExcelProperty(value = {"申请时间"}, index = 5)
    private String createTimeDesc;

    public String getCreateTimeDesc() {
        if (createTime != null) {
            return TimeUtil.transForDateStr(createTime, "yyyy-MM-dd HH:mm:ss");
        }
        return createTimeDesc;
    }

    @ExcelProperty(value = {"提现备注"}, index = 6)
    private String remark;

}
