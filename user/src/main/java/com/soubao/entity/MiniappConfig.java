package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2019-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("miniapp_config")
public class MiniappConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 小程序appid
     */
    private String appId;

    /**
     * 微信支付的商户id
     */
    private String mchId;

    /**
     * 微信支付的商户密钥
     */
    @TableField(value = "`key`")
    private String key;

    /**
     * 小程序secret
     */
    private String appSecret;

    /**
     * 支付商户证书
     */
    private String apiclientCert;

    /**
     * 支付商户证书密钥
     */
    private String apiclientKey;


}
