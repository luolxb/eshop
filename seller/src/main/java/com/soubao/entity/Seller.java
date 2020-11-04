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
 * 卖家用户表
 * </p>
 *
 * @author dyr
 * @since 2019-11-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("seller")
public class Seller implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 卖家编号
     */
    @TableId(value = "seller_id", type = IdType.AUTO)
    private Integer sellerId;

    /**
     * 卖家用户名
     */
    private String sellerName;

    /**
     * 用户编号
     */
    private Integer userId;

    /**
     * 卖家组编号
     */
    private Integer groupId;

    /**
     * 店铺编号
     */
    private Integer storeId;

    /**
     * 是否管理员(0-不是 1-是)
     */
    private Integer isAdmin;

    /**
     * 卖家快捷操作
     */
    private String sellerQuicklink;

    /**
     * 最后登录时间
     */
    private Long lastLoginTime;

    /**
     * 激活状态 0启用1关闭
     */
    private Integer enabled;

    private Long addTime;

    private Boolean openTeach;

    /**
     * 用于app 授权类似于session_id
     */
    private String token;

    @TableField(exist = false)
    private String password;

    @TableField(exist = false)
    private String email;

    @TableField(exist = false)
    private String mobile;

    @TableField(exist = false)
    private String verification;

    @TableField(exist = false)
    private String addTimeShow;

    @TableField(exist = false)
    private String newPwd;

    @TableField(exist = false)
    private String checkNewPwd;

    public String getAddTimeShow() {
        if (addTime != null) {
            return TimeUtil.transForDateStr(addTime, "yyyy-MM-dd HH:mm:ss");
        }
        return addTimeShow;
    }

    @TableField(exist = false)
    private String enabledShow;

    public String getEnabledShow() {
        if (enabled != null){
            if (enabled == 0) {
                return "启用";
            }else if (enabled == 1) {
                return "关闭";
            }
        }
        return enabledShow;
    }

}
