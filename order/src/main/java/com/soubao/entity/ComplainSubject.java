package com.soubao.entity;

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
 * 投诉主题表
 * </p>
 *
 * @author dyr
 * @since 2020-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("complain_subject")
public class ComplainSubject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 投诉主题id
     */
    @TableId(value = "subject_id", type = IdType.AUTO)
    private Integer subjectId;

    /**
     * 投诉主题
     */
    @NotBlank(message = "主题不能为空")
    private String subjectName;

    /**
     * 投诉主题描述
     */
    @NotBlank(message = "描述不能为空")
    private String subjectDesc;

    /**
     * 投诉主题状态(1-有效/2-失效)
     */
    private Integer subjectState;


}
