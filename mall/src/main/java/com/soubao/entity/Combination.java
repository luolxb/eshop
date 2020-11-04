package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.List;

import com.soubao.common.utils.TimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>
 * 组合促销表
 * </p>
 *
 * @author dyr
 * @since 2020-05-19
 */
@ApiModel(value = "组合促销对象", description = "combination")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Combination implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @TableId(value = "combination_id", type = IdType.AUTO)
    private Integer combinationId;
    @NotEmpty(message = "搭配购标题必须")
    @ApiModelProperty(value = "标题")
    private String title;
    @NotEmpty(message = "搭配购描述必须")
    @ApiModelProperty(value = "描述")
    @TableField(value = "`desc`")
    private String desc;
    @ApiModelProperty(value = "上下架，0下，1上")
    private Integer isOnSale;
    @NotNull(message = "请选择活动开始时间")
    @ApiModelProperty(value = "活动有效起始时间")
    private Long startTime;
    @NotNull(message = "请选择活动截止时间")
    @ApiModelProperty(value = "活动有效截止时间")
    private Long endTime;
    @ApiModelProperty(value = "审核状态,0待审核;1审核通过;2审核拒绝")
    private Integer status;
    @ApiModelProperty(value = "店铺id")
    private Integer storeId;
    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;

    @NotNull(message = "请选择搭配购商品")
    @Size(min=2, message = "请选择两件以上(含两件)商品")
    @ApiModelProperty(value = "搭配购商品列表")
    @TableField(exist = false)
    private List<CombinationGoods> combinationGoods;

    @ApiModelProperty(value = "搭配购商品名称")
    @TableField(exist = false)
    private String goodsName;

    @ApiModelProperty(value = "搭配购店铺名称")
    @TableField(exist = false)
    private String storeName;

    @ApiModelProperty(value = "套餐内商品数量")
    @TableField(exist = false)
    private Integer goodsCount;

    @TableField(exist = false)
    private String startTimeShow;

    public String getStartTimeShow() {
        return TimeUtil.transForDateStr(startTime, "yyyy-MM-dd HH:mm:ss");
    }

    @TableField(exist = false)
    private String endTimeShow;

    public String getEndTimeShow() {
        return TimeUtil.transForDateStr(endTime, "yyyy-MM-dd HH:mm:ss");
    }

    @TableField(exist = false)
    private String statusDetail;

    public String getStatusDetail() {
        switch (status) {
            case 0:
                return "待审核";
            case 1:
                return "审核通过";
            case 2:
                return "审核拒绝";
        }
        return statusDetail;
    }
}
