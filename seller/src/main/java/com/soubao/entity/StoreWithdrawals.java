package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.utils.TimeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("store_withdrawals")
public class StoreWithdrawals implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商家id
     */
    private Integer storeId;

    /**
     * 申请日期
     */
    private Long createTime;

    /**
     * 拒绝时间
     */
    private Long refuseTime;

    /**
     * 支付时间
     */
    private Long payTime;

    /**
     * 审核时间
     */
    private Long checkTime;

    /**
     * 提现金额
     */
    @NotNull(message = "提现金额必须")
    @Min(value = 100,message = "提现金额必须大于100")
    private BigDecimal money;

    /**
     * 银行名称 如支regexp付宝 微信 中国银行 农业银行等
     */
    @NotBlank(message = "银行名称必须")
    private String bankName;

    /**
     * 银行账号
     */
    @NotBlank(message = "银行账号必须")
    private String bankCard;

    /**
     * 银行账户名 可以是支付宝可以其他银行
     */
    @NotBlank(message = "开户名必填")
    private String realname;

    /**
     * 提现备注
     */
    private String remark;

    /**
     * 状态：-2删除作废-1审核失败0申请中1审核通过2已转款完成
     */
    private Integer status;

    /**
     * 付款对账流水号
     */
    private String payCode;

    /**
     * 手续费
     */
    private BigDecimal taxfee;

    /**
     * 转款失败错误代码
     */
    private String errorCode;

    @TableField(exist = false)
    private String createTimeDesc;

    public String getCreateTimeDesc() {
        if (createTime != null) {
            return TimeUtil.transForDateStr(createTime, "yyyy-MM-dd");
        }
        return createTimeDesc;
    }

    @TableField(exist = false)
    private String statusDesc;

    public String getStatusDesc() {
        switch (status){
            case -2:
                return "无效作废";
            case -1:
                return "审核失败";
            case 0:
                return "待审核";
            case 1:
                return "审核通过";
            case 2:
                return "转款完成";
            default:
                return "";
        }
    }

    @TableField(exist = false)
    private String storeName;   //店铺名称

    @TableField(exist = false)
    private String storeMoney;  //店铺可用资金

}
