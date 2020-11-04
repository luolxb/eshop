package com.soubao.vo;

import com.soubao.entity.StoreGrade;
import lombok.Data;

@Data
public class StoreReopenInfoVO {
    private String reEndTime;
    private Integer earlier = 30;
    private String startApplyTime;
    private boolean applyStatus;
    private String storeGradeName;
    private StoreGrade storeGrade;
}
