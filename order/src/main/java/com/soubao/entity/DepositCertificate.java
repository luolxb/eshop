package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("deposit_certificate")
public class DepositCertificate implements Serializable{


    private static final long serialVersionUID = 1L;

    /**
     * 存证ID
     */
    //@TableId(value = "id", type = IdType.AUTO)
    private Long Id;

    /**
     * 存证名称
     */
    private String certificateName;

    /**
     * 存证角色
     */
    private String certificateRole;


    /**
     * 存证logo
     */
    private Long certificateLogo;

    /**
     * 存证用户ID   注：这个值就是 DepositCertificatePublisher.userID
     */
    private Long userId;

    /**
     * 初始商家ID
     */
    private Long sellerId;

    /**
     * 产品规格
     */
    private String specification;

    /**
     * 产品箱号
     */
    private String caseNumber;

    /**
     * 产品批次号
     */
    private String batchNumber;

    /**
     * 产品数量
     */
    private Long amount;

    /**
     * 采购企业
     */
    private String purchasingCompany;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;


    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 哈希值
     */
    private String hasdValue;

    /**
     * 哈希值
     */
    private String blockHeight;

    /**
     * 模板id
     */

    private Long templateId;

    /**
     * 申请时间
     */
    private Date appliTime;

    /**
     * 上链时间
     */
    private Date onChainTime;

    /**
     * 上链转态1:未上链  2：已上链 3：上链中 4：上链失败
     */
    private Integer onChainStatus;

    /**
     * 审核备注
     */
    private String auditRemark;


    /**
     * 审核状态
     */
    private boolean auditStatus;

    /**
     * 审核人
     */
    private String auditBy;

    /**
     * 扩充参数
     */
    private String parameter;

    /**
     * 是否已发送
     */
    private boolean sendStatus;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 是否已设置成商品
     */
    private boolean saleStatus;

    /**
     * 凭证令牌
     */
    private String certificate;


    /**
     * 凭证价格
     */
    private BigDecimal shopPrice;

}
