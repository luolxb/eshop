package com.soubao.dto;

import com.soubao.entity.Cart;
import com.soubao.entity.GoodsSku;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("添加组合套餐到购物车对象")
public class CombinationCartDto {
    @NotNull(message = "参数错误")
    private Integer combinationId;
    @NotNull(message = "参数错误")
    private Integer goodsNum;
    private List<Cart> cartList;
}
