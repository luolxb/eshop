package com.soubao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class DcGoodInfo {

    @ApiModelProperty(value = "凭证ID")
    @JsonProperty(value = "deposit_certificate_id")
    private Integer depositCertificateId;

    @ApiModelProperty(value = "凭证类型")
    @JsonProperty(value = "dc_type")
    private Integer dcType;

    @ApiModelProperty(value = "凭证图片")
    @JsonProperty(value = "imagine_url")
    private String  imagineUrl;

    @ApiModelProperty(value = "凭证标题")
    @JsonProperty(value = "dc_tiTle")
    private String  dcTiTle;

    @ApiModelProperty(value = "店类型")
    @JsonProperty(value = "store_type")
    private String  storeType;

    @ApiModelProperty(value = "商品名称")
    @JsonProperty(value = "goods_name")
    private String  goodsName;

    @ApiModelProperty(value = "商品创建者")
    @JsonProperty(value = "creator")
    private String  creator;

    @ApiModelProperty(value = "当前商品持有者")
    private String  owner;


    @ApiModelProperty(value = "模型")
    private String  model;

    @ApiModelProperty(value = "容量")
    private String  volume;

    @ApiModelProperty(value = "数量")
    private String  amount;

    @ApiModelProperty(value = "凭证链接地址")
    @JsonProperty(value = "dc_url_link")
    private String  dcUrlLink;


    @ApiModelProperty(value = "是否积分价格")
    @JsonProperty("is_integral_price")
    private boolean isIntegralPrice;

    @ApiModelProperty(value = "商品价格")
    @NotNull(message = "本店售价不能为空")
    @JsonProperty("shop_price")
    private BigDecimal shopPrice;

    @ApiModelProperty(value = "邮费设置")
    @JsonProperty("post_fee_set")
    private String postFeeSet;

    @ApiModelProperty(value = "邮寄区域")
    @JsonProperty("post_zone")
    private String postZone;

    @ApiModelProperty(value = "邮寄方式")
    @JsonProperty("post_type")
    private Integer postType;

    @ApiModelProperty(value = "是否在售")
    @JsonProperty("is_on_sale")
    private Integer isOnSale;

    @ApiModelProperty(value = "是否推荐")
    @JsonProperty("is_recommend")
    private Integer isRecommend;

    @ApiModelProperty(value = "运货方式")
    @JsonProperty("freight_type")
    private Integer  freightType;

    @ApiModelProperty(value = "商品相册列表节点")
    private List<GoodsImages> goodsImages;

    @ApiModelProperty(value = "商品规格价格列表节点")
    private List<SpecGoodsPrice> specGoodsPriceList;

    @ApiModelProperty(value = "商品属性列表节点")
    private List<GoodsAttr> goodsAttrs;

}
