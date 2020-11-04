package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.utils.TimeUtil;
import com.soubao.validation.annotation.Phone;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author dyr
 * @since 2019-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 邮件
     */
    @Email
    private String email;

    /**
     * 密码
     */
    @NotEmpty(message = "登录密码不能为空")
    private String password;

    /**
     * 0 保密 1 男 2 女
     */
    private Integer sex;

    /**
     * 生日
     */
    private Integer birthday;

    /**
     * 用户金额
     */
    private BigDecimal userMoney;

    /**
     * 冻结金额
     */
    private BigDecimal frozenMoney;

    /**
     * 累积分佣金额
     */
    private BigDecimal distributMoney;

    /**
     * 消费积分
     */
    private Integer payPoints;

    /**
     * 支付密码
     */
    private String paypwd;

    /**
     * 注册时间
     */
    private Long regTime;

    /**
     * 最后登录时间
     */
    private Long lastLogin;

    /**
     * 最后登录ip
     */
    private String lastIp;

    /**
     * QQ
     */
    private String qq;

    /**
     * 手机号码
     */
    @Phone
    private String mobile;

    /**
     * 是否验证手机
     */
    private Integer mobileValidated;

    /**
     * 头像
     */
    private String headPic;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行账号
     */
    private String bankCard;

    /**
     * 用户真实姓名
     */
    private String realname;

    /**
     * 身份证号
     */
    private String idcard;

    /**
     * 是否验证电子邮箱
     */
    private Integer emailValidated;

    /**
     * 第三方返回昵称
     */
    @NotBlank(message = "会员昵称不能为空")
    private String nickname;

    /**
     * 会员等级
     */
    private Integer level;

    /**
     * 会员折扣，默认1不享受
     */
    private BigDecimal discount;

    /**
     * 消费累计额度
     */
    private BigDecimal totalAmount;

    /**
     * 是否被锁定冻结
     */
    private Integer isLock;

    /**
     * 是否为分销商 0 否 1 是
     */
    private Integer isDistribut;

    /**
     * 第一个上级
     */
    private Integer firstLeader;

    /**
     * 第二个上级
     */
    private Integer secondLeader;

    /**
     * 第三个上级
     */
    private Integer thirdLeader;

    /**
     * 用于app 授权类似于session_id
     */
    private String token;

    /**
     * 用户下线数
     */
    @TableField(update = "%s+1")
    private Integer underlingNumber;

    /**
     * 消息掩码
     */
    private Integer messageMask;

    /**
     * 推送id
     */
    private String pushId;

    /**
     * 分销商等级
     */
    private Integer distributLevel;

    /**
     * 小程序专属二维码
     */
    private String xcxQrcode;

    /**
     * 属于哪个店铺的会员
     */
    private Integer isStoreMember;

    @TableField(exist = false)
    private User firstLeaderUser;
    @TableField(exist = false)
    private User secondLeaderUser;
    @TableField(exist = false)
    private User thirdLeaderUser;

    private Integer isEntry;
    private String manifesto;
    private BigDecimal distributWithdrawalsMoney;

    @TableField(exist = false)
    private List<GrantedAuthority> authorities;

    @TableField(exist = false)
    private Integer orderNum;

    @TableField(exist = false)
    private BigDecimal amount;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return String.valueOf(userId);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @TableField(exist = false)
    private String regTimeDesc;

    public String getRegTimeDesc() {
        if (regTime != null) {
            return TimeUtil.transForDateStr(this.regTime, "yyyy-MM-dd");
        }
        return regTimeDesc;
    }

    @ApiModelProperty("店铺id")
    @TableField(exist = false)
    private Integer storeId;
    @ApiModelProperty("店铺会员数")
    @TableField(exist = false)
    private Integer storeMemberCount;
    @ApiModelProperty("店铺营业额")
    @TableField(exist = false)
    private BigDecimal storeTurnover;

    @ApiModelProperty("一级会员数")
    @TableField(exist = false)
    private Integer firstLeaderCount;
    @ApiModelProperty("二级会员数")
    @TableField(exist = false)
    private Integer secondLeaderCount;
    @ApiModelProperty("三级会员数")
    @TableField(exist = false)
    private Integer thirdLeaderCount;

    /**
     * 下线会员总数
     */
    @TableField(exist = false)
    private Integer lowerSum;

    public Integer getLowerSum() {
        if (lowerSum == null) {
            lowerSum = 0;
            if (firstLeaderCount != null) {
                lowerSum += firstLeaderCount;
            }
            if (secondLeaderCount != null) {
                lowerSum += secondLeaderCount;
            }
            if (thirdLeaderCount != null) {
                lowerSum += thirdLeaderCount;
            }
        }
        return lowerSum;
    }
}
