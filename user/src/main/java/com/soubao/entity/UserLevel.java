package com.soubao.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-10-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_level")
public class UserLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "level_id", type = IdType.AUTO)
    private Integer levelId;

    /**
     * 头衔名称
     */
    @NotBlank(message = "等级名称不能为空")
    private String levelName;

    /**
     * 等级必要金额
     */
    @NotNull(message = "消费额度不能为空")
    private BigDecimal amount;

    /**
     * 折扣
     */
    private Integer discount;

    @TableField(value = "`describe`")
    private String describe;


}
