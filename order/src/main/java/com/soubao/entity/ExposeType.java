package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 举报类型表
 * </p>
 *
 * @author dyr
 * @since 2020-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("expose_type")
public class ExposeType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 举报类型id
     */
    @TableId(value = "expose_type_id", type = IdType.AUTO)
    private Integer exposeTypeId;

    /**
     * 举报类型名称 
     */
    @NotBlank(message = "举报类型不能为空")
    private String exposeTypeName;

    /**
     * 举报类型描述
     */
    @NotBlank(message = "描述不能为空")
    private String exposeTypeDesc;

    /**
     * 举报类型状态(1有效/2失效)
     */
    private Integer exposeTypeState;

    @TableField(exist = false)
    private String exposeTypeStateDetail;

    public String getExposeTypeStateDetail() {
        if (exposeTypeState != null) {
            if (this.exposeTypeState == 1){
                return "有效";
            }
            if (this.exposeTypeState == 2){
                return "失效";
            }
        }
        return exposeTypeStateDetail;
    }


}
