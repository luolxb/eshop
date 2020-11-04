package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.utils.TimeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author dyr
 * @since 2019-12-06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("store_apply")
public class StoreApply implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("索引id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("推荐人开店")
    private Integer inviteUserId;

    @ApiModelProperty("申请人会员ID")
    private Integer userId;

    @ApiModelProperty("联系人姓名")
    private String contactsName;

    @ApiModelProperty("联系人手机")
    private String contactsMobile;

    @ApiModelProperty("联系人邮箱")
    private String contactsEmail;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("公司性质")
    private Integer companyType;

    @ApiModelProperty("公司网址")
    private String companyWebsite;

    @ApiModelProperty("公司所在省份")
    private Integer companyProvince;

    @ApiModelProperty("公司所在城市")
    private Integer companyCity;

    @ApiModelProperty("公司所在地区")
    private Integer companyDistrict;

    @ApiModelProperty("公司详细地址")
    private String companyAddress;

    @ApiModelProperty("固定电话")
    private String companyTelephone;

    @ApiModelProperty("电子邮箱")
    private String companyEmail;

    @ApiModelProperty("传真")
    private String companyFax;

    @ApiModelProperty("邮政编码")
    private String companyZipcode;

    @ApiModelProperty("营业执照注册号/统一社会信用代码")
    private String businessLicenceNumber;

    @ApiModelProperty("营业执照电子版")
    private String businessLicenceCert;

    @ApiModelProperty("是否为一证一码商家")
    private Boolean threeinone;

    @ApiModelProperty("注册资金")
    private String regCapital;

    @ApiModelProperty("法人代表")
    private String legalPerson;

    @ApiModelProperty("法人身份证照片")
    private String legalIdentityCert;

    @ApiModelProperty("法人身份证号")
    private String legalIdentity;

    @ApiModelProperty("营业执照有效期起始时间")
    private String businessDateStart;

    @ApiModelProperty("营业执照有效期截至时间")
    private String businessDateEnd;

    @ApiModelProperty("组织机构代码")
    private String orgnizationCode;

    @ApiModelProperty("组织机构代码证")
    private String orgnizationCert;

    @ApiModelProperty("纳税人识别号")
    private String attachedTaxNumber;

    @ApiModelProperty("纳税类型税码")
    private Integer taxRate;

    @ApiModelProperty("纳税人类型")
    private Boolean taxpayer;

    @ApiModelProperty("一般纳税人资格证书")
    private String taxpayerCert;

    @ApiModelProperty("营业执照经营范围")
    private String businessScope;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("卖家账号")
    private String sellerName;

    @ApiModelProperty("1旗舰店,2专卖店,3专营店")
    private Integer storeType;

    @ApiModelProperty("店铺地址")
    private String storeAddress;

    @ApiModelProperty("店铺负责人姓名")
    private String storePersonName;

    @ApiModelProperty("店铺负责人手机")
    private String storePersonMobile;

    @ApiModelProperty("店铺联系人QQ")
    private String storePersonQq;

    @ApiModelProperty("店铺负责人邮箱")
    private String storePersonEmail;

    @ApiModelProperty("店铺负责人身份证照片")
    private String storePersonCert;

    @ApiModelProperty("店铺负责人身份证号")
    private String storePersonIdentity;

    @ApiModelProperty("结算银行名称")
    private String bankAccountName;

    @ApiModelProperty("结算银行账号")
    private String bankAccountNumber;

    @ApiModelProperty("开户银行支行名称")
    private String bankBranchName;

    @ApiModelProperty("开户银行支行所在地")
    private Integer bankProvince;

    @ApiModelProperty("开户银行支行所在城市")
    private Integer bankCity;

    @ApiModelProperty("近一年主营渠道")
    private Boolean mainChannel;

    @ApiModelProperty("近一年销售额")
    private String salesVolume;

    @ApiModelProperty("可网售商品数量")
    private Integer skuNum;

    @ApiModelProperty("同类电子商务网站经验")
    private Boolean ecExperience;

    @ApiModelProperty("预计平均客单价")
    private BigDecimal avgPrice;

    @ApiModelProperty("仓库情况")
    private Boolean wareHouse;

    @ApiModelProperty("有无实体店")
    private Boolean entityShop;

    @ApiModelProperty("店铺分类名称")
    private String scName;

    @ApiModelProperty("店铺分类编号")
    private Integer scId;

    @ApiModelProperty("店铺分类保证金")
    private Integer scBail;

    @ApiModelProperty("店铺等级编号")
    private Integer sgId;

    @ApiModelProperty("店铺等级名称")
    private String sgName;

    @ApiModelProperty("申请分类佣金信息")
    private String storeClassIds;

    @ApiModelProperty("付款金额")
    private BigDecimal payingAmount;

    @ApiModelProperty("付款凭证")
    private String payingAmountCert;

    @ApiModelProperty("店铺申请状态 0未审核，1通过，2拒绝")
    private Integer applyState;

    @ApiModelProperty("管理员审核信息")
    private String reviewMsg;

    @ApiModelProperty("提交申请时间")
    private Long addTime;

    @ApiModelProperty("申请类型默认0企业1个人")
    private Integer applyType;

    @ApiModelProperty("店铺经营类目id集合")
    @TableField(exist = false)
    private Set<String> storeClassIdList;

    @ApiModelProperty("店铺省地址")
    @TableField(exist = false)
    private String province;

    @ApiModelProperty("店铺市地址")
    @TableField(exist = false)
    private String city;

    @ApiModelProperty("店铺区地址")
    @TableField(exist = false)
    private String district;

    @ApiModelProperty("入驻流程填表阶段")
    @TableField(exist = false)
    private Integer status;

    @ApiModelProperty("店铺类目列表")
    @TableField(exist = false)
    private List<StoreClass> storeClass;

    @ApiModelProperty("组合经营类目")
    @TableField(exist = false)
    private String bindClassList;

    @TableField(exist = false)
    private String applyStateDesc;

    public String getApplyStateDesc() {
        if (applyState != null) {
            if (applyState == 1) {
                return "开店成功";
            } else if (applyState == 2) {
                return "未通过";
            } else {
                return "新申请";
            }
        }
        return null;
    }

    @TableField(exist = false)
    private String addTimeDesc;

    public String getAddTimeDesc() {
        if (addTime != null) {
            return TimeUtil.transForDateStr(this.addTime, "yyyy-MM-dd");
        }
        return addTimeDesc;
    }

    @TableField(exist = false)
    private String provinceName;//公司所在省份

    @TableField(exist = false)
    private String cityName;//公司所在城市

    @TableField(exist = false)
    private String districtName;//公司所在地区

    @TableField(exist = false)
    private String bankProvinceName;//开户银行支行所在地

    @TableField(exist = false)
    private String bankCityName;//开户银行支行所在城市

    @TableField(exist = false)
    private User inviteUser;
    @TableField(exist = false)
    private String inviteUserName;  //推荐人名称或手机号

    public String getInviteUsername() {
        if (inviteUser != null) {
            if (!StringUtils.isEmpty(inviteUser.getNickName())) {
                return inviteUser.getNickName();
            }
            if (!StringUtils.isEmpty(inviteUser.getMobile())) {
                return inviteUser.getMobile();
            }
        }
        return "无";
    }

    @TableField(exist = false)
    private List<String> companys;
    public List<String> getCompanys(){
        companys = new ArrayList<>();
        companys.add("股份有限公司");
        companys.add("个人独立企业");
        companys.add("有限责任公司");
        companys.add("外资");
        companys.add("中外合资");
        companys.add("国企");
        companys.add("合伙制企业");
        companys.add("其它");
        return companys;
    }

    @TableField(exist = false)
    private String companyTypeDesc;

    public String getCompanyTypeDesc() {
        if (companyType != null) {
            return companys.get(companyType);
        }
        return null;
    }

    @TableField(exist = false)
    private Integer storeEndTime;//接收页面开店年限

}
