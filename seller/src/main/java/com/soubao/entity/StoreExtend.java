package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("store_extend")
public class StoreExtend implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    private Integer storeId;

    /**
     * 快递公司ID的组合
     */
    private String express;

    /**
     * 店铺统计设置的商品价格区间
     */
    private String pricerange;

    /**
     * 店铺统计设置的订单价格区间
     */
    private String orderpricerange;

    /**
     * 已上传图片数量
     */
    private Integer picNum;


}
