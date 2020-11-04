package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.soubao.common.utils.TimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("stock_log")
@ApiModel("库存日志对象")
public class StockLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("商品ID")
    private Integer goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品规格")
    private String goodsSpec;

    @ApiModelProperty("订单编号")
    private String orderSn;

    @ApiModelProperty("商家ID")
    private Integer storeId;

    @ApiModelProperty("操作用户ID")
    private Integer muid;

    @ApiModelProperty("更改库存")
    private Integer stock;

    @ApiModelProperty("操作时间")
    private Long ctime;

    @TableField(exist = false)
    private String ctimeDetail;

    @ApiModelProperty("商家名称")
    @TableField(exist = false)
    private String storeName;

    public String getCtimeDetail() {
        return TimeUtil.transForDateStr(ctime, "yyyy-MM-dd HH:mm:ss");
    }

}
