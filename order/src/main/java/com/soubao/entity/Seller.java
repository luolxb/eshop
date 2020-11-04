package com.soubao.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 卖家用户表
 *
 * @author dyr
 * @since 2019-11-14
 */
@Getter
@Setter
public class Seller {

    /** 卖家编号 */
    private Integer sellerId;

    /** 卖家用户名 */
    private String sellerName;

    /** 用户编号 */
    private Integer userId;

    /** 卖家组编号 */
    private Integer groupId;

    /** 店铺编号 */
    private Integer storeId;

    /** 是否管理员(0-不是 1-是) */
    private Integer isAdmin;

    /** 卖家快捷操作 */
    private String sellerQuicklink;

    /** 最后登录时间 */
    private Long lastLoginTime;

    /** 激活状态 0启用1关闭 */
    private Boolean enabled;

    private Integer addTime;

    /** 用于app 授权类似于session_id */
    private String token;

    private String password;

    private String verification;
}
