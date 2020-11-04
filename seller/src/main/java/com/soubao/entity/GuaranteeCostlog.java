package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.utils.TimeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 店铺消费者保障服务保证金日志表
 * </p>
 *
 * @author dyr
 * @since 2020-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("guarantee_costlog")
public class GuaranteeCostlog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 保障项目ID
     */
    private Integer grtId;

    /**
     * 保障项目名称
     */
    private String grtName;

    /**
     * 店铺ID
     */
    private Integer storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 操作管理员ID
     */
    private Integer adminId;

    /**
     * 操作管理员名称
     */
    private String adminName;

    /**
     * 金额
     */
    private BigDecimal price;

    /**
     * 添加时间
     */
    private Long addTime;

    /**
     * 描述
     */
    @TableField("`desc`")
    private String desc;

    @TableField(exist = false)
    private String addTimeDesc;

    public String getAddTimeDesc() {
        if (addTime != null) {
            return TimeUtil.transForDateStr(this.addTime, "yyyy-MM-dd HH:mm:ss");
        }
        return addTimeDesc;
    }

}
