package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.utils.TimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_distribution")
@ApiModel("用户选择分销商品对象")
public class UserDistribution implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分销商品索引")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("分销会员id")
    private Integer userId;

    @ApiModelProperty("会员昵称")
    private String userName;

    @ApiModelProperty("商品id")
    private Integer goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品分类ID")
    private Integer catId;

    @ApiModelProperty("商品品牌")
    private Integer brandId;

    @ApiModelProperty("分享次数")
    private Integer shareNum;

    @ApiModelProperty("分销销量")
    private Integer salesNum;

    @ApiModelProperty("加入个人分销库时间")
    private Long addtime;

    @ApiModelProperty("商品对应的店铺ID")
    private Integer storeId;

    @ApiModelProperty("商品加入分销时间")
    @TableField(exist = false)
    private String addTimeDesc;
    public String getAddTimeDesc(){
        return TimeUtil.transForDateStr(addtime, "yyyy-MM-dd HH:mm:ss");
    }

}
