package com.soubao.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UserLowerVo implements Serializable {
    private Long regTime;
    private String nickname;
    private BigDecimal goodsPrice;
    private Integer orderCount;
    private Integer buyUserId;
}
