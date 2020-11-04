package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 用户消息模板
 * </p>
 *
 * @author dyr
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("member_msg_tpl")
public class MemberMsgTpl implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户消息模板编号
     */
    @TableId(value = "mmt_code", type = IdType.AUTO)
    @NotBlank(message = "模板编号必填")
    private String mmtCode;

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称必填")
    private String mmtName;

    /**
     * 站内信接收开关
     */
    private Integer mmtMessageSwitch;

    /**
     * 站内信消息内容
     */
    @NotBlank(message = "站内信消息内容必填")
    private String mmtMessageContent;

    /**
     * 短信接收开关
     */
    private Integer mmtShortSwitch;

    /**
     * 短信接收内容
     */
    @NotBlank(message = "短信内容必填")
    private String mmtShortContent;

    /**
     * 邮件接收开关
     */
    private Integer mmtMailSwitch;

    /**
     * 邮件标题
     */
    @NotBlank(message = "邮件标题必填")
    private String mmtMailSubject;

    /**
     * 邮件内容
     */
    @NotBlank(message = "邮件内容必填")
    private String mmtMailContent;

    public String getMmtMailContent(){
        if (StringUtils.isNotEmpty(mmtMailContent)) {
            return HtmlUtils.htmlUnescape(mmtMailContent);
        }
        return null;
    }

    /**
     * 短信签名
     */
    @NotBlank(message = "短信签名必填")
    private String mmtShortSign;

    /**
     * 短信模板ID
     */
    @NotBlank(message = "短信模板ID必填")
    private String mmtShortCode;

    @TableField(exist = false)
    private Integer isShow;

    public Integer getIsShow() {
        if (mmtCode != null) {
            if (mmtCode.equals("message_notice")
                    || mmtCode.equals("pay_cancel_logistics") || mmtCode.equals("refund_logistics")
                    || mmtCode.equals("withdrawals_notice")) {
                isShow = 1;
            } else {
                isShow = 0;
            }
        }
        return isShow;
    }

}
