package com.soubao.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("goods_category")
@ApiModel(value = "商品分类对象", description = "goods_category表")
public class GoodsCategory implements Serializable {

    @ApiModelProperty(value = "商品分类id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商品分类名称")
    @NotBlank(message = "分类名称必填")
    private String name;

    @ApiModelProperty(value = "手机端显示的商品分类名")
    @NotBlank(message = "移动端类名称")
    private String mobileName;

    @ApiModelProperty(value = "父id")
    private Integer parentId;

    @ApiModelProperty(value = "家族图谱")
    private String parentIdPath;

    @TableField(exist = false)
    private List<Integer> parentIds;


    @ApiModelProperty(value = "等级")
    private Integer level;

    @ApiModelProperty(value = "顺序排序")
    private Integer sortOrder;

    @ApiModelProperty(value = "是否显示")
    private Integer isShow;

    @ApiModelProperty(value = "分类图片")
    private String image;

    @ApiModelProperty(value = "是否推荐为热门分类")
    private Integer isHot;

    @ApiModelProperty(value = "分类分组默认0")
    private Integer catGroup;

    @ApiModelProperty(value = "佣金比例")
    private Integer commission;

    @ApiModelProperty(value = "分佣比例用于分销")
    private Integer commissionRate;

    @ApiModelProperty(value = "对应的类型id(商品模型ID)")
    private Integer typeId;

    @ApiModelProperty(value = "模型名称")
    @TableField(exist = false)
    private String goodsTypeName;

    @ApiModelProperty(value = "子类列表节点")
    @TableField(exist = false)
    List<GoodsCategory> children;

    @ApiModelProperty(value = "是否没有子节点")
    @TableField(exist = false)
    Boolean noChildren;

    @ApiModelProperty(value = "品牌列表节点")
    @TableField(exist = false)
    List<Brand> brands;

    public List<Integer> getParentIds(){
        if(StringUtils.isNotEmpty(parentIdPath)){
            parentIds = Arrays.stream(parentIdPath.split("_")).map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
            if(parentIds.size() > 0){
                parentIds.remove(0);
            }
        }
        return parentIds;
    }


    private static final long serialVersionUID = 1L;
}