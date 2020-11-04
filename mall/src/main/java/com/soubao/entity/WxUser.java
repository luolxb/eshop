package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 微信公共帐号
 * </p>
 *
 * @author dyr
 * @since 2020-06-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WxUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * uid
     */
    private Integer uid;

    /**
     * 公众号名称
     */
    private String wxname;

    /**
     * aeskey
     */
    private String aeskey;

    /**
     * encode
     */
    private Integer encode;

    /**
     * appid
     */
    private String appid;

    /**
     * appsecret
     */
    private String appsecret;

    /**
     * 公众号原始ID
     */
    private String wxid;

    /**
     * 微信号
     */
    private String weixin;

    /**
     * 头像地址
     */
    private String headerpic;

    /**
     * token
     */
    private String token;

    /**
     * 交互token 与微信
     */
    private String mutualToken;

    /**
     * create_time
     */
    private Long createTime;

    /**
     * updatetime
     */
    private Integer updatetime;

    /**
     * 内容模版ID
     */
    private String tplcontentid;

    /**
     * 分享ticket
     */
    private String shareTicket;

    /**
     * share_dated
     */
    private String shareDated;

    /**
     * authorizer_access_token
     */
    private String authorizerAccessToken;

    /**
     * authorizer_refresh_token
     */
    private String authorizerRefreshToken;

    /**
     * authorizer_expires
     */
    private String authorizerExpires;

    /**
     * 类型
     */
    private Integer type;

    /**
     *  网页授权token
     */
    private String webAccessToken;

    /**
     * web_refresh_token
     */
    private String webRefreshToken;

    /**
     * 过期时间
     */
    private Long webExpires;

    /**
     * qr
     */
    private String qr;

    /**
     * 菜单
     */
    private String menuConfig;

    /**
     * 微信接入状态,0待接入1已接入
     */
    private Integer waitAccess;

    /**
     * 服务器域名
     */
    private String host;

    /**
     * 服务器地址URL
     */
    private String apiurl;

}
