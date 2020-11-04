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

/**
 * <p>
 * 店铺消费者保障服务日志表
 * </p>
 *
 * @author dyr
 * @since 2020-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("guarantee_log")
public class GuaranteeLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;

    /**
     * 店铺ID
     */
    private Integer logStoreid;

    /**
     * 店铺名称
     */
    private String logStorename;

    /**
     * 服务项目ID
     */
    private Integer logGrtid;

    /**
     * 服务项目名称
     */
    private String logGrtname;

    /**
     * 操作描述
     */
    private String logMsg;

    /**
     * 添加时间
     */
    private Long logAddtime;

    @TableField(exist = false)
    private String logAddTimeDesc;

    public String getLogAddTimeDesc() {
        if (logAddtime != null) {
            return TimeUtil.transForDateStr(logAddtime, "yyyy-MM-dd HH:mm:ss");
        }
        return logAddTimeDesc;
    }

    /**
     * 操作者角色 管理员为admin 商家为seller
     */
    private String logRole;

    /**
     * 操作者ID
     */
    private Integer logUserid;

    /**
     * 操作者名称
     */
    private String logUsername;


}
