package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.utils.TimeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author dyr
 * @since 2019-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("rebate_log")
public class RebateLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增id")
    private Integer id;

    @ApiModelProperty("获佣用户")
    private Integer userId;

    @ApiModelProperty("购买人id")
    private Integer buyUserId;

    @ApiModelProperty("购买人名称")
    private String nickname;

    @ApiModelProperty("订单编号")
    private String orderSn;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("订单商品总额")
    private BigDecimal goodsPrice;

    @ApiModelProperty("获佣金额")
    private BigDecimal money;

    @ApiModelProperty("获佣用户级别")
    private Integer level;

    @ApiModelProperty("分成记录生成时间")
    private Long createTime;

    @ApiModelProperty("确定收货时间")
    private Integer confirm;

    @ApiModelProperty("0未付款,1已付款, 2等待分成(已收货) 3已分成, 4已取消")
    private Integer status;

    @ApiModelProperty("确定分成或者取消时间")
    private Long confirmTime;

    @ApiModelProperty("如果是取消, 有取消备注")
    private String remark;

    @ApiModelProperty("店铺id")
    private Integer storeId;

    @ApiModelProperty("分成日志状态描述")
    @TableField(exist = false)
    private String statusDesc;

    public String getStatusDesc() {
        if (this.status != null) {
            switch (this.status) {
                case 0:
                    return "未付款";
                case 1:
                    return "已付款";
                case 2:
                    return "等待分成";
                case 3:
                    return "已分成";
                case 4:
                    return "已取消";
            }
        }
        return null;
    }

    @ApiModelProperty("分销商等级描述")
    @TableField(exist = false)
    private String levelDesc;

    public String getLevelDesc() {
        if (this.level != null) {
            if (this.level == 1) {
                return "一级分销商";
            } else if (this.level == 2) {
                return "二级分销商";
            } else {
                return "三级分销商";
            }
        }
        return null;
    }

    @ApiModelProperty("记录生成时间描述")
    @TableField(exist = false)
    private String createTimeDesc;

    public String getCreateTimeDesc() {
        return TimeUtil.transForDateStr(this.createTime, "yyyy-MM-dd HH:mm");
    }

    @ApiModelProperty("确定分成时间描述")
    @TableField(exist = false)
    private String confirmTimeDesc;

    public String getConfirmTimeDesc() {
        return TimeUtil.transForDateStr(this.confirmTime, "yyyy-MM-dd HH:mm");
    }

    @ApiModelProperty("分销记录所属用户")
    @TableField(exist = false)
    private User user;

    @ApiModelProperty("注册时间")
    @TableField(exist = false)
    private long regTime;

    @TableField(exist = false)
    private List<OrderGoods> orderGoodsList;

}
