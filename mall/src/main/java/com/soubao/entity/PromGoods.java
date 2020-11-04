package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.utils.TimeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author dyr
 * @since 2019-08-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("prom_goods")
public class PromGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 促销活动名称
     */
    @NotBlank(message = "活动标题不能为空")
    private String title;

    /**
     * 促销类型:0直接打折,1减价优惠,2固定金额出售,3买就赠优惠券
     */
    @NotNull(message = "请选择促销活动类型")
    private Integer type;

    /**
     * 优惠体现
     */
    @NotBlank(message = "请填写优惠")
    private String expression;

    /**
     * 活动描述
     */
    private String description;

    /**
     * 活动开始时间
     */
    @NotNull(message = "活动开始时间必须")
    private Long startTime;

    /**
     * 活动结束时间
     */
    @NotNull(message = "活动结束时间必须")
    private Long endTime;

    /**
     * 1正常，0管理员关闭
     */
    private Integer status;

    /**
     * 是否已结束
     */
    private Integer isEnd;

    /**
     * 适用范围
     */
    @TableField("`group`")
    private String group;

    /**
     * 商家店铺id
     */
    private Integer storeId;

    /**
     * 排序
     */
    private Integer orderby;

    /**
     * 活动宣传图片
     */
    @NotBlank(message = "图片必须")
    private String promImg;

    /**
     * 是否推荐
     */
    private Integer recommend;

    /**
     * 每人限购数
     */
    @NotNull(message = "限购数量不能为空")
    @Min(value = 0,message = "限购数量必须大于0")
    private Integer buyLimit;

    /**
     * 是否删除,1是 ，0否
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    @TableField(exist = false)
    private String typeDesc;

    public String getTypeDesc() {
        if (type != null) {
            switch (type) {
                case 0:
                    return "直接打折";
                case 1:
                    return "减价优惠";
                case 2:
                    return "固定金额出售";
                case 3:
                    return "买就赠优惠券";
                default:
                    return "";
            }
        }
        return typeDesc;
    }

    @TableField(exist = false)
    private String statusDesc;

    public String getStatusDesc() {
        if(this.status != null){
            if(this.status == 0){
                return "管理员关闭";
            }
            if(this.isEnd == 1){
                return "已结束";
            }
            if(this.startTime != null && this.startTime > System.currentTimeMillis() / 1000){
                return "未开始";
            }
            if(status == 1 && isEnd == 0){
                return "进行中";
            }
        }
        return "";
    }

    @TableField(exist = false)
    private String startTimeShow;

    public String getStartTimeShow() {
        if (startTime != null) {
            return TimeUtil.transForDateStr(startTime, "yyyy-MM-dd");
        }
        return startTimeShow;
    }

    @TableField(exist = false)
    private String endTimeShow;

    public String getEndTimeShow() {
        if (endTime != null) {
            return TimeUtil.transForDateStr(endTime, "yyyy-MM-dd");
        }
        return endTimeShow;
    }

    @TableField(exist = false)
    @Valid // 嵌套验证必须用@Valid
    @NotNull(message = "请选择促销商品")
    @Size(min = 1, message = "请选择促销商品")
    private List<SpecGoodsPrice> specGoodsPriceList;

}
