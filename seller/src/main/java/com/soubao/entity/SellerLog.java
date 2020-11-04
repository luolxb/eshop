package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.soubao.common.utils.TimeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 卖家日志表
 * </p>
 *
 * @author dyr
 * @since 2020-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("seller_log")
public class SellerLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志编号
     */
    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;

    /**
     * 日志内容
     */
    private String logContent;

    /**
     * 日志时间
     */
    private Long logTime;

    /**
     * 卖家编号
     */
    private Integer logSellerId;

    /**
     * 卖家帐号
     */
    private String logSellerName;

    /**
     * 店铺编号
     */
    private Integer logStoreId;

    /**
     * 卖家ip
     */
    private String logSellerIp;

    /**
     * 日志url
     */
    private String logUrl;

    /**
     * 日志状态(0-失败 1-成功)
     */
    private Integer logState;

    @TableField(exist = false)
    private String logTimeShow;

    public String getLogTimeShow() {
        if (logTime != null) {
            return TimeUtil.transForDateStr(logTime, "yyyy-MM-dd HH:mm:ss");
        }
        return logTimeShow;
    }


}
