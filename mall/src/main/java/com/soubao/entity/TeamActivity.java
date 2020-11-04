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
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("team_activity")
@ApiModel("拼团活动对象")
public class TeamActivity implements Serializable, GoodsProm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("拼团活动id")
    @TableId(value = "team_id", type = IdType.AUTO)
    private Integer teamId;

    @NotBlank(message = "拼团标题必须")
    @ApiModelProperty("拼团活动标题")
    private String actName;

    @ApiModelProperty("拼团活动类型,0分享团1佣金团2抽奖团")
    private Integer teamType;

    @NotNull(message = "成团有效期必须")
    @ApiModelProperty("成团有效期。单位（秒)")
    private Integer timeLimit;

    @ApiModelProperty("拼团价")
    private BigDecimal teamPrice;

    @NotNull(message = "需要成团人数必须")
    @ApiModelProperty("需要成团人数")
    private Integer needer;

    @NotBlank(message = "请选择参与拼团的商品")
    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("团长佣金")
    private BigDecimal bonus;

    @ApiModelProperty("抽奖限量")
    private Integer stockLimit;

    @ApiModelProperty("商品id")
    private Integer goodsId;

    @ApiModelProperty("单次团购买限制数0为不限制")
    private Integer buyLimit;

    @ApiModelProperty("已拼多少件")
    private Integer salesSum;

    @ApiModelProperty("虚拟销售基数")
    private Integer virtualNum;

    @NotBlank(message = "分享标题必须")
    @ApiModelProperty("分享标题")
    private String shareTitle;

    @NotBlank(message = "分享描述必须")
    @ApiModelProperty("分享描述")
    private String shareDesc;

    //@NotBlank(message = "分享图片必须")
    @ApiModelProperty("分享图片")
    private String shareImg;

    @ApiModelProperty("商家id")
    private Integer storeId;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("是否推荐")
    private Integer isRecommend;

    @ApiModelProperty("0待审核1正常2拒绝3关闭")
    private Integer status;

    @ApiModelProperty("是否已经抽奖.1是，0否")
    private Integer isLottery;

    @ApiModelProperty("创建时间")
    private Long addTime;

    @ApiModelProperty("软删除")
    private Integer deleted;

    @ApiModelProperty("商品规格id")
    private Integer itemId;

    @ApiModelProperty("拼团成员")
    @TableField(exist = false)
    private List<TeamFollow> teamFollows;

    @ApiModelProperty("拼团商品规格")
    @TableField(exist = false)
    private List<TeamGoodsItem> teamGoodsItem;

    @Override
    public BigDecimal getShopPrice() {
        return teamPrice;
    }

    @Override
    public BigDecimal getPayMoney() {
        return null;
    }

    @Override
    public Integer getStock() {
        return null;
    }

    @Override
    public String getPromTypeDesc() {
        return "拼团";
    }

    @Override
    public Long getPromStartTime() {
        return null;
    }

    @Override
    public Long getPromEndTime() {
        return null;
    }

    @ApiModelProperty("活动是否正在进行")
    @TableField(exist = false)
    private boolean isOn;

    @Override
    public boolean getIsOn() {
        if (status != null && isLottery != null && deleted != null) {
            if (status == 1 && isLottery == 0 && deleted == 0) {
                isOn = true;
            }
        }
        return isOn;
    }

    @ApiModelProperty("显示的拼团数量")
    @TableField(exist = false)
    private Integer realSaleSum;
    public Integer getRealSaleSum(){
        if (salesSum != null && virtualNum != null) {
            realSaleSum = salesSum + virtualNum;
        }
        return realSaleSum;
    }

    @ApiModelProperty("店铺节点")
    @TableField(exist = false)
    private Store store;

    @ApiModelProperty("拼团成功数")
    @TableField(exist = false)
    private Integer successCount;
}
