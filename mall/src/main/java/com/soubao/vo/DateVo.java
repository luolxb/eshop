package com.soubao.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.soubao.entity.GoodsVisit;
import lombok.Data;

import java.util.Calendar;
import java.util.List;

@Data
public class DateVo {
    private String date;

    @TableField(exist = false)
    private List<GoodsVisit> visitList;

    public String getDate() {
        String currYear = Calendar.getInstance().get(Calendar.YEAR) + "";
        if (currYear.equals(date.substring(0, 4))) {//今年
            return date.substring(5);
        }
        return date;
    }
}
