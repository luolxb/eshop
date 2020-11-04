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
 * 店铺消息表
 * </p>
 *
 * @author dyr
 * @since 2020-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("store_msg")
public class StoreMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺消息id
     */
    @TableId(value = "sm_id", type = IdType.AUTO)
    private Integer smId;

    /**
     * 店铺id
     */
    private Integer storeId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送时间
     */
    private Long addtime;

    /**
     * 消息是否已被查看
     */
    private Integer open;

    @TableField(exist = false)
    private String addTimeDesc;
    public String getAddTimeDesc(){
        return TimeUtil.transForDateStr(this.addtime, "yyyy-MM-dd HH:mm:ss");
    }

    @TableField(exist = false)
    private String openDesc;
    public String getOpenDesc(){
        if (this.open == 0){
            return "未阅";
        }
        return "已阅";
    }
}
