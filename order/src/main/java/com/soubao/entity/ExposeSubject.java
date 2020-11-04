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
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 举报主题表
 * </p>
 *
 * @author dyr
 * @since 2020-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("expose_subject")
public class ExposeSubject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 举报主题id
     */
    @TableId(value = "expose_subject_id", type = IdType.AUTO)
    private Integer exposeSubjectId;

    /**
     * 举报主题内容
     */
    @NotBlank(message = "主题内容不能为空")
    private String exposeSubjectContent;

    /**
     * 举报类型id
     */
    @NotNull(message = "请选择举报类型，若无举报类型请先添加举报类型")
    private Integer exposeSubjectTypeId;

    /**
     * 举报类型名称 
     */
    private String exposeSubjectTypeName;

    /**
     * 举报主题状态(1可用/2失效)
     */
    private Integer exposeSubjectState;

    @TableField(exist = false)
    private String exposeSubjectStateDetail;

    public String getExposeSubjectStateDetail() {
        if (exposeSubjectState != null) {
            if (this.exposeSubjectState == 1){
                return "可用";
            }
            if (this.exposeSubjectState == 2){
                return "失效";
            }
        }
        return exposeSubjectStateDetail;
    }


}
