package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 商家消息模板
 * </p>
 *
 * @author dyr
 * @since 2020-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("store_msg_tpl")
public class StoreMsgTpl implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板编码
     */
    @TableId(value = "smt_code", type = IdType.AUTO)
    @NotBlank(message = "模板编号必填")
    private String smtCode;

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称必填")
    private String smtName;

    /**
     * 站内信默认开关，0关，1开
     */
    private Integer smtMessageSwitch;

    /**
     * 站内信内容
     */
    @NotBlank(message = "站内信消息内容必填")
    private String smtMessageContent;

    /**
     * 站内信强制接收，0否，1是
     */
    private Integer smtMessageForced;

    /**
     * 短信默认开关，0关，1开
     */
    private Integer smtShortSwitch;

    /**
     * 短信内容
     */
    @NotBlank(message = "短信内容必填")
    private String smtShortContent;

    /**
     * 短信强制接收，0否，1是
     */
    private Integer smtShortForced;

    /**
     * 邮件默认开，0关，1开
     */
    private Integer smtMailSwitch;

    /**
     * 邮件标题
     */
    @NotBlank(message = "邮件标题必填")
    private String smtMailSubject;

    /**
     * 邮件内容
     */
    @NotBlank(message = "邮件内容必填")
    private String smtMailContent;

    public String getSmtMailContent() {
        if (StringUtils.isNotEmpty(smtMailContent)) {
            return HtmlUtils.htmlUnescape(smtMailContent);
        }
        return null;
    }

    /**
     * 邮件强制接收，0否，1是
     */
    private Integer smtMailForced;

    /**
     * 短信签名
     */
    private String smtShortSign;

    /**
     * 短信模板ID
     */
    private String smtShortCode;


}
