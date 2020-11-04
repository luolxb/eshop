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
 * 
 * </p>
 *
 * @author dyr
 * @since 2020-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("reply")
public class Reply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 回复id
     */
    @TableId(value = "reply_id", type = IdType.AUTO)
    private Integer replyId;

    /**
     * 评论id：关联评论表
     */
    private Integer commentId;

    /**
     * 父类id，0为最顶级
     */
    private Integer parentId;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 回复人的昵称
     */
    private String userName;

    /**
     * 被回复人的昵称
     */
    private String toName;

    /**
     * 未删除0；删除：1
     */
    private Integer deleted;

    /**
     * 回复时间
     */
    private Long replyTime;

    @TableField(exist = false)
    private String replyTimeShow;

    public String getReplyTimeShow() {
        if(replyTime != null){
            replyTimeShow = TimeUtil.timestampToStr(replyTime);
        }
        return replyTimeShow;
    }


}
