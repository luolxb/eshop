package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods_images")
@ApiModel(value = "商品图片对象", description = "goods_images表")
public class GoodsImages implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "商品图片表主键")
    @TableId(value = "img_id", type = IdType.AUTO)
    private Integer imgId;
    @ApiModelProperty(value = "商品表主键")
    private Integer goodsId;
    @ApiModelProperty(value = "图片地址")
    private String imageUrl;
    @ApiModelProperty(value = "商品排序,最小的拍最前面")
    private Boolean imgSort;
    @ApiModelProperty(value = "相册表ID")
    private Integer albumId;
}
