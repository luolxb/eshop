package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.utils.ShopStringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * <p>
 * 店铺数据表
 * </p>
 *
 * @author dyr
 * @since 2019-08-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("store")
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺索引id
     */
    @TableId(value = "store_id", type = IdType.AUTO)
    private Integer storeId;

    /**
     * 推荐人开店
     */
    private Integer inviteUserId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 店铺等级
     */
    private Integer gradeId;

    /**
     * 会员id
     */
    private Integer userId;

    /**
     * 会员名称
     */
    private String userName;

    /**
     * 店主卖家用户名
     */
    private String sellerName;

    /**
     * 店铺分类
     */
    private Integer scId;

    /**
     * 店铺公司名称
     */
    private String companyName;

    /**
     * 店铺所在省份ID
     */
    private Integer provinceId;

    /**
     * 店铺所在城市ID
     */
    private Integer cityId;

    /**
     * 店铺所在地区ID
     */
    private Integer district;

    /**
     * 详细地区
     */
    private String storeAddress;

    /**
     * 商家地址经度
     */
    private BigDecimal longitude;

    /**
     * 商家地址纬度
     */
    private BigDecimal latitude;

    @TableField(exist = false)
    private BigDecimal distance;

    /**
     * 邮政编码
     */
    private String storeZip;

    /**
     * 店铺状态，0关闭，1开启，2审核中
     */
    private Integer storeState;

    /**
     * 店铺关闭原因
     */
    private String storeCloseInfo;

    /**
     * 店铺排序
     */
    private Integer storeSort;

    /**
     * 店铺结算类型
     */
    private String storeRebatePaytime;

    /**
     * 开店时间
     */
    private Integer storeTime;

    /**
     * 店铺有效截止时间
     */
    private Integer storeEndTime;

    /**
     * 店铺logo
     */
    private String storeLogo;

    /**
     * 店铺横幅
     */
    private String storeBanner;


    /**
     * 店铺头像
     */
    private String storeAvatar;

    @TableField(exist = false)
    private String storeAvatarUrl;

    /**
     * 店铺seo关键字
     */
    private String seoKeywords;

    /**
     * 店铺seo描述
     */
    private String seoDescription;

    /**
     * 阿里旺旺
     */
    private String storeAliwangwang;

    /**
     * QQ
     */
    private String storeQq;

    /**
     * 商家电话
     */
    private String storePhone;

    /**
     * 主营商品
     */
    private String storeZy;

    /**
     * 店铺二级域名
     */
    private String storeDomain;

    /**
     * 推荐，0为否，1为是，默认为0
     */
    private Integer storeRecommend;

    /**
     * 店铺当前主题
     */
    private String storeTheme;

    /**
     * 店铺信用
     */
    private Integer storeCredit;

    /**
     * 描述相符度分数
     */
    private BigDecimal storeDesccredit;
    @TableField(exist = false)
    private String storeDesccreditDesc;
    public String getStoreDesccreditDesc(){
        return (storeDesccredit == null) ? storeDesccreditDesc : ShopStringUtil.getMarkDesc(storeDesccredit);
    }

    /**
     * 服务态度分数
     */
    private BigDecimal storeServicecredit;
    @TableField(exist = false)
    private String storeServicecreditDesc;
    public String getStoreServicecreditDesc(){
        return (storeServicecredit == null) ? storeServicecreditDesc : ShopStringUtil.getMarkDesc(storeServicecredit);
    }

    /**
     * 发货速度分数
     */
    private BigDecimal storeDeliverycredit;
    @TableField(exist = false)
    private String storeDeliverycreditDesc;
    public String getStoreDeliverycreditDesc(){
        return (storeServicecredit == null) ? storeDeliverycreditDesc : ShopStringUtil.getMarkDesc(storeDeliverycredit);
    }

    /**
     * 店铺平均分数
     */
    @TableField(exist = false)
    private BigDecimal storeAvgcredit;
    public BigDecimal getStoreAvgcredit(){
        if(storeDesccredit != null && storeServicecredit != null && storeDeliverycredit != null){
            MathContext mc = new MathContext(2, RoundingMode.UP);
            storeAvgcredit = storeDesccredit.add(storeServicecredit).add(storeDeliverycredit).divide(new BigDecimal(3), mc);
        }
        return storeAvgcredit;
    }
    @TableField(exist = false)
    private String storeAvgcreditDesc;
    public String getStoreAvgcreditDesc(){
        if(getStoreAvgcredit() != null){
            storeAvgcreditDesc = ShopStringUtil.getMarkDesc(getStoreAvgcredit());
        }
        return storeAvgcreditDesc;
    }
    /**
     * 店铺收藏数量
     */
    private Integer storeCollect;

    /**
     * 店铺幻灯片
     */
    private String storeSlide;

    /**
     * 店铺幻灯片链接
     */
    private String storeSlideUrl;

    /**
     * 打印订单页面下方说明文字
     */
    private String storePrintdesc;

    /**
     * 店铺销量
     */
    private Integer storeSales;

    /**
     * 售前客服
     */
    private String storePresales;

    /**
     * 售后客服
     */
    private String storeAftersales;

    /**
     * 工作时间
     */
    private String storeWorkingtime;

    /**
     * 超出该金额免运费，大于0才表示该值有效
     */
    private BigDecimal storeFreePrice;

    /**
     *  库存预警数
     */
    private Integer storeWarningStorage;

    /**
     * 店铺装修开关(0-关闭 装修编号-开启)
     */
    private Integer storeDecorationSwitch;

    /**
     * 开启店铺装修时，仅显示店铺装修(1-是 0-否
     */
    private Integer storeDecorationOnly;

    /**
     * 是否自营店铺 1是 0否
     */
    private Integer isOwnShop;

    /**
     * 自营店是否绑定全部分类 0否1是
     */
    private Integer bindAllGc;

    /**
     * 7天退换
     */
    private Integer qitian;

    /**
     * 正品保障
     */
    private Integer certified;

    /**
     * 退货承诺
     */
    private Integer returned;

    /**
     * 商家配送时间
     */
    private String storeFreeTime;

    /**
     * 手机店铺 轮播图链接地址
     */
    private String mbSlide;

    /**
     * 手机版广告链接
     */
    private String mbSlideUrl;

    /**
     * 店铺默认配送区域
     */
    private String deliverRegion;

    /**
     * 货到付款
     */
    private Integer cod;

    /**
     * 两小时发货
     */
    private Integer twoHour;

    /**
     * 保证服务开关
     */
    private Integer ensure;

    /**
     * 保证金额
     */
    private BigDecimal deposit;

    /**
     * 保证金显示开关
     */
    private Integer depositIcon;

    /**
     * 店铺可用资金
     */
    private BigDecimal storeMoney;

    /**
     * 待结算资金
     */
    private BigDecimal pendingMoney;

    /**
     * 未删除0，已删除1
     */
    private Integer deleted;

    /**
     * 店铺发布商品是否需要审核
     */
    private Integer goodsExamine;

    /**
     * 客户下单, 接收下单提醒短信
     */
    private String servicePhone;

    /**
     * 是否启用二级域名.1:启用;0:关闭
     */
    private Integer domainEnable;

    /**
     * 店铺类型。0普通店铺。供应商店
     */
    private Integer storeType;

    /**
     * 1为默认k一同步平台必须为自营。可以同步给线下店铺
     */
    private Integer defaultStore;

    /**
     * 店铺公告
     */
    private String storeNotice;

}
