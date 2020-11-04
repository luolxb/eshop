package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 
 * </p>
 *
 * @author dyr
 * @since 2019-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GoodsAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 属性id
     */
    @TableId(value = "attr_id", type = IdType.AUTO)
    private Integer attrId;

    /**
     * 属性名称
     */
    private String attrName;

    /**
     * 属性分类id
     */
    private Integer typeId;

    /**
     * 0不需要检索 1关键字检索
     */
    private Integer attrIndex;

    /**
     * 下拉框展示给商家选择
     */
    private Integer attrType;

    /**
     * 2多行文本框,平台属性录入方式
     */
    private Integer attrInputType;

    /**
     * 可选值列表
     */
    private String attrValues;

    @TableField(exist = false)
    private List<String> attrValueList;
    public List<String> getAttrValueList(){
        if(attrValues != null){
            attrValueList = Arrays.stream(attrValues.split(",")).map(String::trim).collect(Collectors.toList());
        }
        return attrValueList;
    }

    /**
     * 属性排序
     */
    @TableField(value = "`order`")
    private Integer order;
    @TableField(exist = false)
    private String attrValue;
}
