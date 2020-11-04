package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 店铺分类表
 * </p>
 *
 * @author dyr
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("store_class")
public class StoreClass implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 索引ID
     */
    @TableId(value = "sc_id", type = IdType.AUTO)
    private Integer scId;

    /**
     * 分类名称
     */
    @NotEmpty(message = "名称必须")
    private String scName;

    /**
     * 保证金数额
     */
    @NotNull(message = "保证金额度必须")
    private Integer scBail;

    /**
     * 排序
     */
    @NotNull(message = "排序必须")
    private Integer scSort;


}
