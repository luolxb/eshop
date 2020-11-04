package com.soubao.vo;

import com.soubao.entity.StoreSales;
import lombok.Data;

import java.util.List;

/**
 * 店铺客服对象
 */
@Data
public class StoreSalesVo {
    private List<StoreSales> preSalesList;     //售前客服
    private List<StoreSales> afterSalesList;   //售后客服
    private String workingTime; //服务时间
}
