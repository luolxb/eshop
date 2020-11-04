package com.soubao.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("config")
public class Config implements Serializable {

    @TableId(type = IdType.AUTO)
    private Short id;

    private String name;

    private String value;

    private String incType;

    @TableField("`desc`")
    private String desc;

    private static final long serialVersionUID = 1L;
}