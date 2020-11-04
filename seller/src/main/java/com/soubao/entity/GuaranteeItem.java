package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 消费者保障服务项目表
 * </p>
 *
 * @author dyr
 * @since 2020-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("guarantee_item")
public class GuaranteeItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "grt_id", type = IdType.AUTO)
    private Integer grtId;

    /**
     * 保障项目名称
     */
    @NotEmpty(message = "名称不能为空")
    @Length(max = 50, message = "名称不能大于50个字符")
    private String grtName;

    /**
     * 保障项目描述
     */
    @NotEmpty(message = "保障描述不能为空")
    private String grtDescribe;

    /**
     * 保证金
     */
    private BigDecimal grtCost;

    /**
     * 图标
     */
    @NotEmpty(message = "请上传图标")
    private String grtIcon;

    /**
     * 内容介绍文章地址
     */
    private String grtDescurl;

    /**
     * 状态 0关闭 1开启
     */
    private Integer grtState;

    /**
     * 排序
     */
    private Integer grtSort;

    /**
     * 收费规则
     */
    private String grtCharge;

    /**
     * 理赔规则
     */
    private String grtCompensate;

    /**
     * 赔付金额
     */
    private BigDecimal grtMoney;

    /**
     * 扣除分数
     */
    private Integer grtScore;


    @TableField(exist = false)
    private String grtStateDesc;

    public String getGrtStateDesc() {
        if (grtState != null) {
            if (grtState == 0) {
                return "关闭";
            } else if (grtState == 1) {
                return "开启";
            }
        }
        return null;
    }

    @TableField(exist = false)
    public Integer joinState;   //加入状态

    @TableField(exist = false)
    public Integer auditState;  //申请审核状态

    @TableField(exist = false)
    public Integer quitState;   //退出申请状态

}
