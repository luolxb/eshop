package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * 提货订单操作记录
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pick_order_action")
@ApiModel
public class PickOrderAction {

    @Id
    @TableId(value = "prick_order_action_id", type = IdType.AUTO)
    @ApiModelProperty("提货订单操作id")
    private Integer prickOrderActionId;
    /**
     * 提货订单id
     */
    @ApiModelProperty("提货订单id")
    private Integer prickOrderId;
    /**
     * 操作用户
     */
    @ApiModelProperty("操作用户")
    private Integer actionUser;

    /***
     * 提货订单状态
     */
    @ApiModelProperty("提货订单状态")
    private Integer pickOrderStatus;

    /**
     * 操作消息
     */
    @ApiModelProperty("操作消息")
    private String actionNote;
    /**
     * 日志时间
     */
    @ApiModelProperty("日志时间")
    private Long logTime;

    /**
     * 状态描述
     */
    @ApiModelProperty("状态描述")
    private String statusDesc;
    /**
     * 0管理员1商家2前台用户
     */
    @ApiModelProperty("0管理员1商家2前台用户")
    private Integer userType;
    /**
     * 商店id
     */
    @ApiModelProperty("商店id")
    private Integer storeId;

    @TableField(exist = false)
    @ApiModelProperty("操作用户描述")
    private String actionUserDesc;
}
