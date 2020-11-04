package com.soubao.entity;

import com.soubao.validation.annotation.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.math.BigDecimal;

@Getter
@Setter
@ApiModel("用户对象")
public class User {
    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("邮件")
    @Email
    private String email;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("0 保密 1 男 2 女")
    private Integer sex;

    @ApiModelProperty("生日")
    private Integer birthday;

    @ApiModelProperty("用户金额")
    private BigDecimal userMoney;

    @ApiModelProperty("冻结金额")
    private BigDecimal frozenMoney;

    @ApiModelProperty("累积分佣金额")
    private BigDecimal distributMoney;

    @ApiModelProperty("消费积分")
    private Integer payPoints;

    @ApiModelProperty("支付密码")
    private String paypwd;

    @ApiModelProperty("注册时间")
    private Long regTime;

    @ApiModelProperty("最后登录时间")
    private Long lastLogin;

    @ApiModelProperty("最后登录ip")
    private String lastIp;

    @ApiModelProperty("QQ")
    private String qq;

    @ApiModelProperty("手机号码")
    @Phone
    private String mobile;

    @ApiModelProperty("是否验证手机")
    private Integer mobileValidated;

    @ApiModelProperty("头像")
    private String headPic;

    @ApiModelProperty("银行名称")
    private String bankName;

    @ApiModelProperty("银行账号")
    private String bankCard;

    @ApiModelProperty("用户真实姓名")
    private String realname;

    @ApiModelProperty("身份证号")
    private String idcard;

    @ApiModelProperty("是否验证电子邮箱")
    private Integer emailValidated;

    @ApiModelProperty("第三方返回昵称")
    private String nickname;

    @ApiModelProperty("会员等级")
    private Integer level;

    @ApiModelProperty("会员折扣，默认1不享受")
    private BigDecimal discount;

    @ApiModelProperty("消费累计额度")
    private BigDecimal totalAmount;

    @ApiModelProperty("是否被锁定冻结")
    private Integer isLock;

    @ApiModelProperty("是否为分销商 0 否 1 是")
    private Integer isDistribut;

    @ApiModelProperty("第一个上级")
    private Integer firstLeader;

    @ApiModelProperty("第二个上级")
    private Integer secondLeader;

    @ApiModelProperty("第三个上级")
    private Integer thirdLeader;

    @ApiModelProperty("用于app 授权类似于session_id")
    private String token;

    @ApiModelProperty("用户下线数")
    private Integer underlingNumber;

    @ApiModelProperty("消息掩码")
    private Integer messageMask;

    @ApiModelProperty("推送id")
    private String pushId;

    @ApiModelProperty("分销商等级")
    private Integer distributLevel;

    @ApiModelProperty("小程序专属二维码")
    private String xcxQrcode;

    @ApiModelProperty("属于哪个店铺的会员")
    private Integer isStoreMember;

    @ApiModelProperty("店铺id")
    private Integer storeId;

    @ApiModelProperty("店铺会员数")
    private Integer storeMemberCount;

    @ApiModelProperty("店铺营业额")
    private BigDecimal storeTurnover;

    @ApiModelProperty("一级会员数")
    private Integer firstLeaderCount;

    @ApiModelProperty("二级会员数")
    private Integer secondLeaderCount;

    @ApiModelProperty("三级会员数")
    private Integer thirdLeaderCount;

    @ApiModelProperty("下线会员总数")
    private Integer lowerSum;

    @ApiModelProperty("0平常注册，1微信注册绑定，2微信账号关联")
    private Integer isEntry;

    @ApiModelProperty("个人签名")
    private String manifesto;

    @ApiModelProperty("累积分销佣金提现")
    private BigDecimal distributWithdrawalsMoney;

    private String verification;

}
