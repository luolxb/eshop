package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 店铺可发布商品类目表
 * </p>
 *
 * @author dyr
 * @since 2019-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("store_bind_class")
public class StoreBindClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "bid", type = IdType.AUTO)
    private Integer bid;

    /**
     * 店铺ID
     */
    private Integer storeId;

    /**
     * 佣金比例
     */
    private Integer commisRate;

    /**
     * 一级分类
     */
    @TableField("`class_1`")
    private Integer class1;

    /**
     * 二级分类
     */
    @TableField("`class_2`")
    private Integer class2;

    /**
     * 三级分类
     */
    @TableField("`class_3`")
    private Integer class3;

    /**
     * 状态0审核中1审核通过 2审核失败
     */
    private Integer state;

    @TableField(exist = false)
    private String stateDesc;
    public String getStateDesc(){
        if (state != null){
            if (state == 0){
                return "待审核";
            }else if (state == 1){
                return "已通过";
            }else if (state == 2){
                return "被拒绝";
            }
        }
        return null;
    }

    @TableField(exist = false)
    private String classOneName;

    @TableField(exist = false)
    private String classTwoName;

    @TableField(exist = false)
    private String classThreeName;

    @TableField(exist = false)
    private String fullClassName;

    @TableField(exist = false)
    private String storeName;

    @TableField(exist = false)
    private String sellerName;

}
