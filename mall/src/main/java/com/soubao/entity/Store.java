package com.soubao.entity;

import java.math.BigDecimal;

import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import com.soubao.common.utils.ShopStringUtil;
import com.soubao.common.utils.TimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 店铺数据表
 * </p>
 *
 * @author dyr
 * @since 2019-08-13
 */
@Getter
@Setter
@ApiModel(value = "店铺对象", description = "store")
public class Store {

    @ApiModelProperty(value = "店铺主键")
    private Integer storeId;
    @ApiModelProperty(value = "推荐人开店，用户表主键")
    private Integer inviteUserId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "店铺等级表主键")
    private Integer gradeId;
    @ApiModelProperty(value = "用户表主键")
    private Integer userId;
    @ApiModelProperty(value = "会员名称")
    private String userName;
    @ApiModelProperty(value = "店主卖家用户名")
    private String sellerName;
    @ApiModelProperty(value = "店铺分类表主键")
    private Integer scId;
    @ApiModelProperty(value = "店铺公司名称")
    private String companyName;
    @ApiModelProperty(value = "店铺所在省份ID")
    private Integer provinceId;
    @ApiModelProperty(value = "店铺所在城市ID")
    private Integer cityId;
    @ApiModelProperty(value = "店铺所在地区ID")
    private Integer district;
    @ApiModelProperty(value = "详细地区")
    private String storeAddress;
    @ApiModelProperty(value = "商家地址经度")
    private BigDecimal longitude;
    @ApiModelProperty(value = "商家地址纬度")
    private BigDecimal latitude;
    @ApiModelProperty(value = "邮政编码")
    private String storeZip;
    @ApiModelProperty(value = "店铺状态，0关闭，1开启，2审核中")
    private Integer storeState;
    @ApiModelProperty(value = "店铺关闭原因")
    private String storeCloseInfo;
    @ApiModelProperty(value = "店铺排序")
    private Integer storeSort;
    @ApiModelProperty(value = "店铺结算类型")
    private String storeRebatePaytime;
    @ApiModelProperty(value = "开店时间")
    private Long storeTime;
    @ApiModelProperty(value = "店铺有效截止时间")
    private Long storeEndTime;
    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;
    @ApiModelProperty(value = "店铺横幅")
    private String storeBanner;
    @ApiModelProperty(value = "店铺头像")
    private String storeAvatar;
    @ApiModelProperty(value = "店铺seo关键字")
    private String seoKeywords;
    @ApiModelProperty(value = "店铺seo描述")
    private String seoDescription;
    @ApiModelProperty(value = "阿里旺旺")
    private String storeAliwangwang;
    @ApiModelProperty(value = "QQ")
    private String storeQq;
    @ApiModelProperty(value = "商家电话")
    private String storePhone;
    @ApiModelProperty(value = "主营商品")
    private String storeZy;
    @ApiModelProperty(value = "店铺二级域名")
    private String storeDomain;
    @ApiModelProperty(value = "推荐，0为否，1为是，默认为0")
    private Integer storeRecommend;
    @ApiModelProperty(value = "店铺当前主题")
    private String storeTheme;
    @ApiModelProperty(value = "店铺信用")
    private Integer storeCredit;
    @ApiModelProperty(value = "描述相符度分数")
    private BigDecimal storeDesccredit;
    @ApiModelProperty(value = "店铺收藏数量")
    private Integer storeCollect;
    @ApiModelProperty(value = "店铺幻灯片")
    private String storeSlide;
    @ApiModelProperty(value = "店铺幻灯片链接")
    private String storeSlideUrl;
    @ApiModelProperty(value = "打印订单页面下方说明文字")
    private String storePrintdesc;
    @ApiModelProperty(value = "店铺销量")
    private Integer storeSales;
    @ApiModelProperty(value = "售前客服")
    private String storePresales;
    @ApiModelProperty(value = "售后客服")
    private String storeAftersales;
    @ApiModelProperty(value = "工作时间")
    private String storeWorkingtime;
    @ApiModelProperty(value = "超出该金额免运费，大于0才表示该值有效")
    private BigDecimal storeFreePrice;
    @ApiModelProperty(value = "库存预警数")
    private Integer storeWarningStorage;
    @ApiModelProperty(value = "店铺装修开关(0-关闭 装修编号-开启)")
    private Integer storeDecorationSwitch;
    @ApiModelProperty(value = "开启店铺装修时，仅显示店铺装修(1-是 0-否")
    private Integer storeDecorationOnly;
    @ApiModelProperty(value = "是否自营店铺 1是 0否")
    private Integer isOwnShop;
    @ApiModelProperty(value = "自营店是否绑定全部分类 0否1是")
    private Integer bindAllGc;
    @ApiModelProperty(value = "7天退换")
    private Integer qitian;
    @ApiModelProperty(value = "正品保障")
    private Integer certified;
    @ApiModelProperty(value = "退货承诺")
    private Integer returned;
    @ApiModelProperty(value = "商家配送时间")
    private String storeFreeTime;
    @ApiModelProperty(value = "手机店铺 轮播图链接地址")
    private String mbSlide;
    @ApiModelProperty(value = "手机版广告链接")
    private String mbSlideUrl;
    @ApiModelProperty(value = "店铺默认配送区域")
    private String deliverRegion;
    @ApiModelProperty(value = "货到付款")
    private Integer cod;
    @ApiModelProperty(value = "两小时发货")
    private Integer twoHour;
    @ApiModelProperty(value = "保证服务开关")
    private Integer ensure;
    @ApiModelProperty(value = "保证金额")
    private BigDecimal deposit;
    @ApiModelProperty(value = "保证金显示开关")
    private Integer depositIcon;
    @ApiModelProperty(value = "店铺可用资金")
    private BigDecimal storeMoney;
    @ApiModelProperty(value = "待结算资金")
    private BigDecimal pendingMoney;
    @ApiModelProperty(value = "未删除0，已删除1")
    private Integer deleted;
    @ApiModelProperty(value = "店铺发布商品是否需要审核")
    private Integer goodsExamine;
    @ApiModelProperty(value = "客户下单, 接收下单提醒短信")
    private String servicePhone;
    @ApiModelProperty(value = "是否启用二级域名.1:启用;0:关闭")
    private Integer domainEnable;
    @ApiModelProperty(value = "1旗舰店,2专卖店,3专营店")
    private Integer storeType;
    @ApiModelProperty(value = " 1为默认可以同步平台必须为自营。可以同步给线下店铺")
    private Integer defaultStore;
    @ApiModelProperty(value = "服务态度分数")
    private BigDecimal storeServicecredit;
    @ApiModelProperty(value = "发货速度分数")
    private BigDecimal storeDeliverycredit;
    @ApiModelProperty(value = "店铺公告")
    private String storeNotice;


    @ApiModelProperty(value = "店铺商品列表节点")
    private List<Goods> goodsList;

    private String storeTimeDesc;
    public String getStoreTimeDesc(){
        if (storeTime != null) {
            return TimeUtil.transForDateStr(this.storeTime, "yyyy-MM-dd");
        }
        return storeTimeDesc;
    }

    private String storeEndTimeDesc;
    public String getStoreEndTimeDesc(){
        if (storeEndTime != null){
            if (storeEndTime == 0){
                return "长期";
            }
            return TimeUtil.transForDateStr(storeEndTime, "yyyy-MM-dd");
        }
        return null;
    }

    private String storeStateDesc;
    public String getStoreStateDesc(){
        if(storeState != null){
            if(storeState == 0){
                return "关闭";
            }else if(storeState == 1){
                return "开启";
            }else if(storeState == 2){
                return "审核中";
            }
        }
        return null;
    }

    private Seller seller;  //所属商家

    private User inviteUser;
    private String inviteUserName;  //推荐人名称或手机号
    public String getInviteUsername(){
        if (inviteUser != null){
            if (!StringUtils.isEmpty(inviteUser.getNickname())){
                return inviteUser.getNickname();
            }
            if(!StringUtils.isEmpty(inviteUser.getMobile())){
                return inviteUser.getMobile();
            }
        }
        return "无";
    }

    private BigDecimal storeOrderSum;   //店铺营业额
    public BigDecimal getStoreOrderSum(){
        if(storeOrderSum == null){
            storeOrderSum = BigDecimal.ZERO;
        }
        return storeOrderSum;
    }

    private Integer storeMemberCount;   //店铺会员数

    private String nickname;  //用户昵称

    private String mobile;  //用户手机号

    private Long regTime;  //用户注册时间

    private Long regTimeDesc;  //用户注册时间描述

    public String getRegTimeDesc(){
        return TimeUtil.transForDateStr(this.regTime, "yyyy-MM-dd");
    }

    @ApiModelProperty(value = "店铺描述评分")
    private String storeDesccreditDesc;
    public String getStoreDesccreditDesc(){
        return (storeDesccredit == null) ? storeDesccreditDesc : ShopStringUtil.getMarkDesc(storeDesccredit);
    }

    @ApiModelProperty(value = "服务态度评分")
    private String storeServicecreditDesc;
    public String getStoreServicecreditDesc(){
        return (storeServicecredit == null) ? storeServicecreditDesc : ShopStringUtil.getMarkDesc(storeServicecredit);
    }

    @ApiModelProperty(value = "发货速度评分")
    private String storeDeliverycreditDesc;
    public String getStoreDeliverycreditDesc(){
        return (storeServicecredit == null) ? storeDeliverycreditDesc : ShopStringUtil.getMarkDesc(storeDeliverycredit);
    }

    @ApiModelProperty(value = "店铺平均分数")
    private BigDecimal storeAvgcredit;
    public BigDecimal getStoreAvgcredit(){
        if(storeDesccredit != null && storeServicecredit != null && storeDeliverycredit != null){
            MathContext mc = new MathContext(2, RoundingMode.UP);
            storeAvgcredit = storeDesccredit.add(storeServicecredit).add(storeDeliverycredit).divide(new BigDecimal(3), mc);
        }
        return storeAvgcredit;
    }

    @ApiModelProperty(value = "店铺平均分数评分")
    private String storeAvgcreditDesc;
    public String getStoreAvgcreditDesc(){
        if(getStoreAvgcredit() != null){
            storeAvgcreditDesc = ShopStringUtil.getMarkDesc(getStoreAvgcredit());
        }
        return storeAvgcreditDesc;
    }

    @ApiModelProperty(value = "距离")
    private BigDecimal distance;

    @ApiModelProperty(value = "省")
    private String provinceName;

    @ApiModelProperty(value = "市")
    private String cityName;

    @ApiModelProperty(value = "区/县")
    private String districtName;

    @ApiModelProperty(value = "店铺购物列表节点")
    private List<Cart> cartList;

    @ApiModelProperty(value = "店铺商品总数")
    private Integer goodsCount;

    @ApiModelProperty(value = "店铺订单商品总数")
    private List<OrderGoods> OrderGoodsList;

    @ApiModelProperty(value = "店铺订单总价")
    private BigDecimal storeTotalPrice;
    private BigDecimal getStoreTotalPrice(){
        storeTotalPrice = new BigDecimal(BigInteger.ZERO);
        if(cartList != null){
            for(Cart cart : cartList){
                storeTotalPrice = storeTotalPrice.add(cart.getTotalFee());
            }
        }
        return storeTotalPrice;
    }

    @ApiModelProperty(value = "店铺商品总价")
    private BigDecimal storeGoodsPrice;
    private BigDecimal getStoreGoodsPrice(){
        storeGoodsPrice = new BigDecimal(BigInteger.ZERO);
        if(cartList != null){
            for(Cart cart : cartList){
                storeGoodsPrice = storeTotalPrice.add(cart.getGoodsFee());
            }
        }
        return storeGoodsPrice;
    }

    @ApiModelProperty(value = "店铺订单优惠总价")
    private BigDecimal storeCutPrice;
    private BigDecimal getStoreCutPrice(){
        storeCutPrice = new BigDecimal(BigInteger.ZERO);
        if(cartList != null){
            for(Cart cart : cartList){
                storeCutPrice = storeCutPrice.add(cart.getCutFee());
            }
        }
        return storeCutPrice;
    }

    @ApiModelProperty(value = "店铺商品交易数量")
    private Integer transactionNum;

    @ApiModelProperty(value = "店铺商品成交率")
    private double turnoverRate ;

}
