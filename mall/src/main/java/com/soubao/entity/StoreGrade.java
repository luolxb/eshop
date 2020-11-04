package com.soubao.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class StoreGrade {

    /**
     * 索引ID
     */
    private Integer sgId;

    /**
     * 等级名称
     */
    private String sgName;

    /**
     * 允许发布的商品数量
     */
    private Integer sgGoodsLimit;

    /**
     * 允许上传图片数量
     */
    private Integer sgAlbumLimit;

    /**
     * 上传空间大小，单位MB
     */
    private Integer sgSpaceLimit;

    /**
     * 选择店铺模板套数
     */
    private Integer sgTemplateLimit;

    /**
     * 模板内容
     */
    private String sgTemplate;

    /**
     * 开店费用(元/年)
     */
    private BigDecimal sgPrice;

    /**
     * 申请说明
     */
    private String sgDescription;

    /**
     * 附加功能
     */
    private String sgFunction;

    /**
     * 级别，数目越大级别越高
     */
    private Integer sgSort;

    /**
     * 权限
     */
    private String sgActLimits;
}
