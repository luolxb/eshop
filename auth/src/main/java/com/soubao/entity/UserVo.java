package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
public class UserVo{

    private static final long serialVersionUID = 1L;

    /**
     * 表id
     */
    private Integer userId;

    /**
     * 邮件
     */

    private String email;

    /**
     * 密码
     */
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


    private Integer isEntry;
    private String manifesto;
    private BigDecimal distributWithdrawalsMoney;






}
