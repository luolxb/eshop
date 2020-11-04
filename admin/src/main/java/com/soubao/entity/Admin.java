package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author dyr
 * @since 2019-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("admin")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "admin_id", type = IdType.AUTO)
    private Integer adminId;

    /**
     * 用户名
     */
    private String userName;

    private String email;

    private String password;

    private Integer addTime;

    private Integer lastLogin;

    private String lastIp;

    private String navList;

    private String langType;

    /**
     * 支付密码
     */
    private String paypwd;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * suppliers_id
     */
    private Integer suppliersId;

    /**
     * 0没有城市分站，1有
     */
    private Integer siteId;

    private Integer openTeach;


}
