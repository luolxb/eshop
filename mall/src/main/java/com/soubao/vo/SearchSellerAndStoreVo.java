package com.soubao.vo;

import com.soubao.entity.Goods;
import com.soubao.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchSellerAndStoreVo implements Serializable {

    /**
     * 商家集合
     */
    private List<Goods> goodsList;


    private List<Store> storeList;
}
