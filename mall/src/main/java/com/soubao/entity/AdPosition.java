package com.soubao.entity;

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
 * @since 2020-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ad_position")
public class AdPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "position_id", type = IdType.AUTO)
    private Integer positionId;

    /**
     * 广告位置名称
     */
    private String positionName;

    /**
     * 广告位宽度
     */
    private Integer adWidth;

    /**
     * 广告位高度
     */
    private Integer adHeight;

    /**
     * 广告描述
     */
    private String positionDesc;

    /**
     * 模板
     */
    private String positionStyle;

    /**
     * 0关闭1开启
     */
    private Boolean isOpen;


}
