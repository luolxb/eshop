package com.soubao.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.soubao.common.utils.TimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2020-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("withdrawals")
@ApiModel("提现申请对象")
public class Withdrawals implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主键")
    @TableId(type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("提现金额")
    @NotNull(message = "提现金额不能为空")
    @Min(value = 1, message = "提现金额不能小于1")
    private BigDecimal money;

    @ApiModelProperty("申请时间")
    private Long createTime;

    @ApiModelProperty("审核时间")
    private Long checkTime;

    @ApiModelProperty("支付时间")
    private Long payTime;

    @ApiModelProperty("拒绝时间")
    private Long refuseTime;

    @ApiModelProperty("银行名称 如支付宝 微信 中国银行 农业银行等")
    @NotBlank
    private String bankName;

    @ApiModelProperty("银行账号或支付宝账号")
    @NotBlank
    private String bankCard;

    @ApiModelProperty("提款账号真实姓名")
    @NotBlank
    private String realname;

    @ApiModelProperty("提现备注")
    private String remark;

    @ApiModelProperty("税收手续费")
    private BigDecimal taxfee;

    @ApiModelProperty("状态：-2删除作废-1审核失败0申请中1审核通过2付款成功3付款失败")
    private Integer status;

    @ApiModelProperty("提现类型 : 0余额提现,1佣金提现")
    private Integer type;

    @ApiModelProperty("付款对账流水号")
    private String payCode;

    @ApiModelProperty("付款失败错误代码")
    private String errorCode;


    @ApiModelProperty("当天已提现额度")
    @TableField(exist = false)
    private BigDecimal todayMoney;

    @ApiModelProperty("当天已申请提现次数")
    @TableField(exist = false)
    private Integer todayWithdrawalCount;

    @ApiModelProperty("用户昵称")
    @TableField(exist = false)
    private String nickname;

    @TableField(exist = false)
    private String createTimeDetail;

    public String getCreateTimeDetail() {
        if (createTime != null) {
            return TimeUtil.transForDateStr(createTime, "yyyy-MM-dd HH:mm:ss");
        }
        return createTimeDetail;
    }

    @TableField(exist = false)
    private String checkTimeDetail;

    public String getCheckTimeDetail() {
        if (checkTime != null) {
            return TimeUtil.transForDateStr(checkTime, "yyyy-MM-dd HH:mm:ss");
        }
        return checkTimeDetail;
    }

    @TableField(exist = false)
    private String typeDetail;

    public String getTypeDetail() {
        if (type == 0) {
            return "余额提现";
        }
        if (type == 1) {
            return "佣金提现";
        }
        return typeDetail;
    }

    @TableField(exist = false)
    private String statusDetail;

    public String getStatusDetail() {
        if (status == -2) {
            statusDetail = "无效作废";
        }
        if (status == -1) {
            statusDetail = "申请失败";
        }
        if (status == 0) {
            statusDetail = "待审核";
        }
        if (status == 1) {
            statusDetail = "审核通过";
        }
        return statusDetail;
    }

    @TableField(exist = false)
    private BigDecimal userMoney;

    @TableField(exist = false)
    private BigDecimal frozenMoney;


}
