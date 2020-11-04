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
 * 投诉对话表
 * </p>
 *
 * @author dyr
 * @since 2020-02-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("complain_talk")
public class ComplainTalk implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 投诉对话id
     */
    @TableId(value = "talk_id", type = IdType.AUTO)
    private Integer talkId;

    /**
     * 投诉id
     */
    private Integer complainId;

    /**
     * 发言人id
     */
    private Integer talkMemberId;

    /**
     * 发言人名称
     */
    private String talkMemberName;

    /**
     * 发言人类型(1-投诉人/2-被投诉人/3-平台)
     */
    private String talkMemberType;

    /**
     * 发言内容
     */
    private String talkContent;

    /**
     * 发言状态(1-显示/2-不显示)
     */
    private Integer talkState;

    /**
     * 对话管理员，屏蔽对话人的id
     */
    private Integer talkAdmin;

    /**
     * 对话发表时间
     */
    private Long talkTime;

    @TableField(exist = false)
    private String talkTimeShow;

    public String getTalkTimeShow() {
        if (talkTime != null){
            return TimeUtil.transForDateStr(talkTime,"yyyy-MM-dd HH:mm:ss");
        }
        return talkTimeShow;
    }

    @TableField(exist = false)
    private String talkMemberDetail;

    public String getTalkMemberDetail() {
        if (talkMemberType != null) {
            if (this.talkMemberType.equals("accuser")){
                return "投诉人" + "(" + this.talkMemberName + ")" + "说:";
            }
            if (this.talkMemberType.equals("accused")){
                return "被投诉店铺" + "(" + this.talkMemberName + ")" + "说:";
            }
            if (this.talkMemberType.equals("admin")) {
                return "管理员" + "(" + this.talkMemberName + ")" + "说:";
            }
        }
        return talkMemberDetail;
    }


}
