package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author dyr
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("suppliers")
public class Suppliers implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商ID
     */
    @TableId(value = "suppliers_id", type = IdType.AUTO)
    private Integer suppliersId;

    /**
     * 供应商名称
     */
    @NotEmpty(message = "供应商名称不能为空")
    private String suppliersName;

    /**
     * 供应商描述
     */
    @NotEmpty(message = "供应商描述不能为空")
    private String suppliersDesc;

    /**
     * 供应商状态
     */
    private Integer isCheck;

    /**
     * 供应商联系人
     */
    @NotEmpty(message = "供应商联系人不能为空")
    private String suppliersContacts;

    /**
     * 供应商电话
     */
    @NotEmpty(message = "供应商电话不能为空")
    private String suppliersPhone;

    /**
     * 所属商家id
     */
    private Integer storeId;

}
