package com.soubao.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class UserLowerVo implements Serializable {
    private Long regTime;
    private String nickname;
    private BigDecimal goodsPrice;
    private Integer orderCount;
    private Integer buyUserId;
}
