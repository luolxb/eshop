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
 * @since 2019-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oauth_users")
public class OauthUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表自增ID
     */
    @TableId(value = "tu_id", type = IdType.AUTO)
    private Integer tuId;

    /**
     * 用户表ID
     */
    private Integer userId;

    /**
     * 第三方开放平台openid
     */
    private String openid;

    /**
     * 第三方授权平台
     */
    private String oauth;

    private String unionid;

    /**
     * mp标识来自公众号, open标识来自开放平台,用于标识来自哪个第三方授权平台, 因为同是微信平台有来自公众号和开放平台
     */
    private String oauthChild;

    /**
     * 用户实体
     */
    @TableField(exist = false)
    private User user;

}
