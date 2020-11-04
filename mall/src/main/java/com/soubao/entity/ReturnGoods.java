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

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("return_goods")
@ApiModel("退换货对象")
public class ReturnGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("对应订单商品表ID")
    private Integer recId;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("订单编号")
    private String orderSn;

    @ApiModelProperty("商品id")
    private Integer goodsId;

    @ApiModelProperty("退货数量")
    private Integer goodsNum;

    @ApiModelProperty("0仅退款 1退货退款  2换货 3维修")
    private Integer type;

    @ApiModelProperty("退换货退款申请原因")
    private String reason;

    @ApiModelProperty("问题描述")
    @TableField("`describe`")
    private String describe;

    @ApiModelProperty("申请凭据")
    private String evidence;

    @ApiModelProperty("上传图片路径")
    private String imgs;

    @ApiModelProperty("-2用户取消-1不同意0待审核1通过2已发货3待退款4换货完成5退款完成6申诉仲裁")
    private Integer status;

    @ApiModelProperty("卖家审核进度说明")
    private String remark;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("商家店铺ID")
    private Integer storeId;

    @ApiModelProperty("商品规格")
    private String specKey;

    @ApiModelProperty("客户姓名")
    private String consignee;

    @ApiModelProperty("手机号码")
    private String mobile;

    @ApiModelProperty("退还积分")
    private Integer refundIntegral;

    @ApiModelProperty("退还预存款")
    private BigDecimal refundDeposit;

    @ApiModelProperty("退款金额")
    private BigDecimal refundMoney;

    @ApiModelProperty("0退到用户余额 1支付原路退回")
    private Integer refundType;

    @ApiModelProperty("管理员退款备注")
    private String refundMark;

    @ApiModelProperty("退款时间")
    private Integer refundTime;

    @ApiModelProperty("售后申请时间")
    private Long addtime;

    @ApiModelProperty("卖家审核时间")
    private Integer checktime;

    @ApiModelProperty("卖家收货时间")
    private Integer receivetime;

    @ApiModelProperty("用户取消时间")
    private Integer canceltime;

    @ApiModelProperty("换货服务，卖家重新发货信息")
    private String sellerDelivery;

    @ApiModelProperty("用户发货信息")
    private String delivery;

    @ApiModelProperty("退款差额")
    private BigDecimal gap;

    @ApiModelProperty("差额原因")
    private String gapReason;

    @ApiModelProperty("申请售后时是否已收货0未收货1已收货")
    private Integer isReceive;

}
