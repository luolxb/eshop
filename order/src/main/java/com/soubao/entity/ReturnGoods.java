package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.soubao.common.utils.TimeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("return_goods")
public class ReturnGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 对应订单商品表ID
     */
    private Integer recId;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 商品id
     */
    private Integer goodsId;

    @TableField(exist = false)
    private String goodsName;

    @TableField(exist = false)
    private String storeName;
    /**
     * 退货数量
     */
    private Integer goodsNum;

    /**
     * 0仅退款 1退货退款  2换货 3维修
     */
    private Integer type;
    @TableField(exist = false)
    private String typeDesc;
    public String getTypeDesc(){
        if(type != null){
            if(type == 0){
                typeDesc = "仅退款";
            }else{
                typeDesc = "退货退款";
            }
        }
        return typeDesc;
    }
    /**
     * 退换货退款申请原因
     */
    private String reason;

    /**
     * 问题描述
     */
    @TableField("`describe`")
    private String describe;

    /**
     * 申请凭据
     */
    private String evidence;

    /**
     * 上传图片路径
     */
    private String imgs;

    @TableField(exist = false)
    private List<String> imgsUrl;

    public List<String> getimgsUrl(){
        if (!StringUtils.isEmpty(imgs)) {
            imgsUrl = new ArrayList<>();
            imgsUrl.addAll(Arrays.asList(imgs.split(",")));
        }
        return imgsUrl;
    }

    /**
     * -2用户取消-1不同意0待审核1通过2已发货3待退款4换货完成5退款完成6申诉仲裁
     */
    private Integer status;
    @TableField(exist = false)
    private String statusDesc;
    public String getStatusDesc(){
        if(status != null && isReceive != null){
            if(status == 3 && isReceive == 0){
                statusDesc = "同意";
            }
            switch (status){
                case -2:
                    statusDesc = "服务单取消";
                    break;
                case -1:
                    statusDesc = "审核失败";
                    break;
                case 0:
                    statusDesc = "待审核";
                    break;
                case 1:
                    statusDesc = "审核通过";
                    break;
                case 2:
                    statusDesc = "已发货";
                    break;
                case 3:
                    statusDesc = "已收货";
                    break;
                case 4:
                    statusDesc = "维修/换货完成";
                    break;
                case 5:
                    statusDesc = "退款完成";
                    break;
                case 6:
                    statusDesc = "仲裁中";
                    break;
            }
        }
        return statusDesc;
    }

    @TableField(exist = false)
    private String adminStatusDesc;
    public String getAdminStatusDesc(){
        if(status != null){
            adminStatusDesc = "无";
            if(status == 3){
                adminStatusDesc = "待处理";
            }
            if(status == 4){
                adminStatusDesc = "已完成";
            }
        }
        return adminStatusDesc;
    }
    /**
     * 卖家审核进度说明
     */
    private String remark;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 商家店铺ID
     */
    private Integer storeId;
    /**
     * 商家
     */
    @TableField(exist = false)
    private Store store;

    /**
     * 商品规格
     */
    private String specKey;

    /**
     * 客户姓名
     */
    private String consignee;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 退还积分
     */
    private Integer refundIntegral;

    /**
     * 退还预存款
     */
    private BigDecimal refundDeposit;

    /**
     * 退款金额
     */
    private BigDecimal refundMoney;

    /**
     * 0退到用户余额 1支付原路退回
     */
    private Integer refundType;

    /**
     * 管理员退款备注
     */
    private String refundMark;

    /**
     * 退款时间
     */
    private Long refundTime;

    /**
     * 售后申请时间
     */
    private Long addtime;
    @TableField(exist = false)
    private String addTimeFormat;
    public String getAddTimeFormat(){
        return TimeUtil.transForDateStr(addtime, "yyyy-MM-dd HH:mm:ss");
    }
    @TableField(exist = false)
    private Long addTimeBegin;
    @TableField(exist = false)
    private Long addTimeEnd;
    /**
     * 卖家审核时间
     */
    private Long checktime;
    @TableField(exist = false)
    private String checkTimeFormat;
    public String getCheckTimeFormat(){
        return TimeUtil.transForDateStr(checktime, "yyyy-MM-dd HH:mm:ss");
    }
    /**
     * 卖家收货时间
     */
    private Long receivetime;

    /**
     * 用户取消时间
     */
    private Long canceltime;

    /**
     * 换货服务，卖家重新发货信息
     */
    private String sellerDelivery;

    /**
     * 用户发货信息
     */
    private String delivery;

    /**
     * 退款差额
     */
    private BigDecimal gap;

    /**
     * 差额原因
     */
    private String gapReason;

    /**
     * 申请售后时是否已收货0未收货1已收货
     */
    private Integer isReceive;

    @TableField(exist = false)
    private OrderGoods orderGoods;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private Boolean isCanAgreeOrRefuse;
    public boolean getIsCanAgreeOrRefuse(){
        if(null != status){
            isCanAgreeOrRefuse = status == 0 || status == 6;
        }
        return isCanAgreeOrRefuse;
    }

    @TableField(exist = false)
    private Boolean isCanReturn;
    public boolean getIsCanReturn(){
        if(null != status && null != isReceive && null != type){
            isCanReturn = status == 1 && isReceive == 1 && type > 0;
        }
        return isCanReturn;
    }
}
