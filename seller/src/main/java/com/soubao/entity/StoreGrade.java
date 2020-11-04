package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 店铺等级表
 *
 * @author dyr
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("store_grade")
public class StoreGrade implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 索引ID */
    @TableId(value = "sg_id", type = IdType.AUTO)
    private Integer sgId;

    /** 等级名称 */
    @NotBlank(message = "等级名称不能为空")
    private String sgName;

    /** 允许发布的商品数量 */
    private Integer sgGoodsLimit;

    /** 允许上传图片数量 */
    private Integer sgAlbumLimit;

    /** 上传空间大小，单位MB */
    private Integer sgSpaceLimit;

    /** 选择店铺模板套数 */
    private Integer sgTemplateLimit;

    /** 模板内容 */
    private String sgTemplate;

    /** 开店费用(元/年) */
    private BigDecimal sgPrice;

    /** 申请说明 */
    private String sgDescription;

    /** 附加功能 */
    private String sgFunction;

    /** 级别，数目越大级别越高 */
    private Integer sgSort;

    /** 权限 */
    private String sgActLimits;
}
