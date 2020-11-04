package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author dyr
 * @since 2019-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("brand")
@ApiModel(value = "品牌对象", description = "brand表")
public class Brand implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "品牌表id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "品牌名称")
    @NotBlank(message = "品牌名称不能为空")
    private String name;

    @ApiModelProperty(value = "品牌logo")
    private String logo;

    @NotBlank(message = "品牌描述不能为空")
    @ApiModelProperty(value = "品牌描述")
    @TableField("`desc`")
    private String desc;

    @ApiModelProperty(value = "品牌地址")
    private String url;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "品牌分类")
    private String catName;

    @ApiModelProperty(value = "一级分类id")
    private Integer catId1;

    @ApiModelProperty(value = "二级分类id")
    private Integer catId2;

    @ApiModelProperty(value = "三级分类id")
    private Integer catId3;

    @ApiModelProperty(value = "是否推荐")
    private Integer isHot;

    @ApiModelProperty(value = "商家ID")
    private Integer storeId;

    @ApiModelProperty(value = "0正常 1审核中 2审核失败 审核状态")
    private Integer status;

    @TableField(exist = false)
    private String statusDesc;

    public String getStatusDesc(){
        if (status != null){
            if (status == 0){
                return "正常";
            }else if (status == 1){
                return "审核中";
            }else if (status == 2){
                return "审核失败";
            }
        }
        return null;
    }

    @TableField(exist = false)
    private String cat1Name;
    @TableField(exist = false)
    private Integer typeId;
}
