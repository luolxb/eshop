package com.soubao.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GoodsListAttrFilter {
    private String name;
    private List<GoodsListFilter> item;
}
