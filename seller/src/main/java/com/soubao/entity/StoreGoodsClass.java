package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 店铺商品分类表
 * </p>
 *
 * @author dyr
 * @since 2019-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("store_goods_class")
public class StoreGoodsClass implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 索引ID
     */
    @TableId(value = "cat_id", type = IdType.AUTO)
    private Integer catId;

    /**
     * 店铺商品分类名称
     */
    @NotBlank(message = "请填写商品分类名称")
    private String catName;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 门店id
     */
    private Integer shopId;

    /**
     * 店铺id
     */
    private Integer storeId;

    /**
     * 商品分类排序
     */
    private Integer catSort;

    /**
     * 分类显示状态
     */
    private Integer isShow;

    /**
     * 是否显示在导航栏
     */
    private Integer isNavShow;

    /**
     * 是否首页推荐
     */
    private Integer isRecommend;

    /**
     * 首页此类商品显示数量
     */
    private Integer showNum;

    @TableField(exist = false)
    private String isShowDesc;
    public String getIsShowDesc(){
        if (isShow == 1){
            return "是";
        }
        return "否";
    }

    @TableField(exist = false)
    List<StoreGoodsClass> children;//子类列表节点

}
