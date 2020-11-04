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
public class UserExcel extends BaseRowModel {
    @ExcelProperty(value = {"会员ID"}, index = 0)
    private String userId;

    @ExcelProperty(value = {"会员昵称"}, index = 1)
    private String nickname;

    @ExcelProperty(value = {"会员等级"}, index = 2)
    private String level;

    @ExcelProperty(value = {"手机号"}, index = 3)
    private String mobile;

    @ExcelProperty(value = {"邮箱"}, index = 4)
    private String email;

    private Long regTime;

    @ExcelProperty(value = {"注册时间"}, index = 5)
    private String regTimeDesc;

    public String getRegTimeDesc() {
        if (regTime != null) {
            return TimeUtil.transForDateStr(regTime, "yyyy/MM/dd HH:mm");
        }
        return regTimeDesc;
    }

    private Long lastLogin;

    @ExcelProperty(value = {"最后登录"}, index = 6)
    private String lastLoginDesc;

    public String getLastLoginDesc() {
        if (lastLogin != null) {
            return TimeUtil.transForDateStr(lastLogin, "yyyy/MM/dd HH:mm");
        }
        return lastLoginDesc;
    }

    @ExcelProperty(value = {"余额"}, index = 7)
    private String userMoney;

    @ExcelProperty(value = {"积分"}, index = 8)
    private String payPoints;

    @ExcelProperty(value = {"累计消费"}, index = 9)
    private String totalAmount;

}
