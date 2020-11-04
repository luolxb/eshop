package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 店铺消费者保障服务加入情况表
 * </p>
 *
 * @author dyr
 * @since 2020-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("guarantee")
public class Guarantee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 店铺ID
     */
    private Integer storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 服务项目ID
     */
    private Integer grtId;

    /**
     * 加入状态 0未申请 1已申请 2已加入
     */
    private Integer joinstate;

    /**
     * 保证金余额
     */
    private BigDecimal cost;

    /**
     * 付款凭证图
     */
    private String costimg;

    /**
     * 0关闭 1开启
     */
    private Integer isopen;

    /**
     * 申请审核状态0未审核1审核通过2审核失败3已支付保证金4保证金审核通过5保证金审核失败
     */
    private Integer auditstate;

    /**
     * 退出申请状态0未申请 1已申请 2申请失败
     */
    private Integer quitstate;


    @TableField(exist = false)
    private String grtName;//保障服务名称

    @TableField(exist = false)
    private String auditstateDesc;

    public String getAuditstateDesc() {
        if (joinstate != null && joinstate == 0) {
            return "未申请";
        }
        if (auditstate != null) {
            switch (auditstate) {
                case 0:
                    return "申请进行中（等待审核）";
                case 1:
                    return "审核通过";
                case 2:
                    return "审核失败";
                case 3:
                    return "已支付保证金，待审核";
                case 4:
                    return "保证金审核通过";
                case 5:
                    return "保证金审核失败";
            }
        }
        return null;
    }
}
