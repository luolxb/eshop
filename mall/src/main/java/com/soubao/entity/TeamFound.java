package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("team_found")
@ApiModel("开团对象")
public class TeamFound implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("开团id")
    @TableId(value = "found_id", type = IdType.AUTO)
    private Integer foundId;

    @ApiModelProperty("开团时间")
    private Long foundTime;

    @ApiModelProperty("成团截止时间")
    private Integer foundEndTime;

    @ApiModelProperty("团长id")
    private Integer userId;

    @ApiModelProperty("拼团活动id")
    private Integer teamId;

    @ApiModelProperty("团长用户名昵称")
    private String nickname;

    @ApiModelProperty("团长头像")
    private String headPic;

    @ApiModelProperty("团长订单id")
    private Integer orderId;

    @ApiModelProperty("团长订单编号")
    private String orderSn;

    @ApiModelProperty("已参团人数")
    @TableField(value = "`join`")
    private Integer join;

    @ApiModelProperty("需多少人成团")
    private Integer need;

    @ApiModelProperty("拼团价格")
    private BigDecimal price;

    @ApiModelProperty("商品原价")
    private BigDecimal goodsPrice;

    @ApiModelProperty("拼团状态0:待开团(表示已下单但是未支付)1:已经开团(团长已支付)2:拼团成功,3拼团失败")
    private Integer status;

    @ApiModelProperty("拼团状态描述")
    @TableField(exist = false)
    private String statusDesc;
    public String getStatusDesc(){
        if(status != null){
            switch (status){
                case 0:
                    statusDesc = "待开团";
                    break;
                case 1:
                    statusDesc = "已开团";
                    break;
                case 2:
                    statusDesc = "拼团成功";
                    break;
                case 3:
                    statusDesc = "拼团失败";
                    break;
            }
        }
        return statusDesc;
    }

    @ApiModelProperty("团长佣金领取状态：0无1领取")
    private Integer bonusStatus;

    @ApiModelProperty("店铺id")
    private Integer storeId;

    @ApiModelProperty("是否已自动处理：0无1是")
    private Integer isAuto;

    @ApiModelProperty("节省多少钱")
    @TableField(exist = false)
    private BigDecimal cutPrice;

    public BigDecimal getCutPrice() {
        if (price != null && goodsPrice != null) {
            cutPrice = goodsPrice.subtract(price);
        }
        return cutPrice;
    }

    @ApiModelProperty("剩余可拼团人数")
    @TableField(exist = false)
    private Integer surplus;

    public Integer getSurplus() {
        if (need != null && join != null) {
            surplus = need - join;
        }
        return surplus;
    }

    @ApiModelProperty("店铺节点")
    @TableField(exist = false)
    private Store store;

    @ApiModelProperty("订单节点")
    @TableField(exist = false)
    private Order order;

    @ApiModelProperty("拼团活动节点")
    @TableField(exist = false)
    private TeamActivity teamActivity;

    @ApiModelProperty("团员列表节点")
    @TableField(exist = false)
    private List<TeamFollow> teamFollowList;

    @ApiModelProperty("成团有效期。单位（秒)")
    @TableField(exist = false)
    private Integer timeLimit;
}
