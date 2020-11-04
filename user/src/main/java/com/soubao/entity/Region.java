package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("地址对象")
public class Region implements Serializable {
    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "深度")
    private Byte level;
    @ApiModelProperty(value = "上级id")
    private Integer parentId;
    @ApiModelProperty(value = "0普通，1热门城市")
    private Byte isHot;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty("娃娃")
    @TableField(exist = false)
    private List<Region> children;
    private static final long serialVersionUID = 1L;
}