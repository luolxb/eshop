package com.soubao.entity;

import lombok.Data;

import java.util.*;

@Data
public class Bill {
    private List<Store> storeCartList;//店铺购物车列表
    private Order order;//主订单
}
