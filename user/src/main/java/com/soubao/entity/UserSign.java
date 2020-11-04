package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("user_sign")
public class UserSign implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 累计签到天数
     */
    private Integer signTotal;

    /**
     * 连续签到天数
     */
    private Integer signCount;

    /**
     * 最后签到时间，时间格式20170907
     */
    private String signLast;

    /**
     * 历史签到时间，以逗号隔开
     */
    private String signTime;

    /**
     * 用户累计签到总积分
     */
    private Integer cumtrapz;

    /**
     * 本月累计积分
     */
    private Integer thisMonth;

    @TableField(exist = false)
    private String nickname;
    @TableField(exist = false)
    private String mobile;
}
