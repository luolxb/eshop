package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.soubao.common.utils.TimeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author dyr
 * @since 2020-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods_consult")
public class GoodsConsult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品咨询id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 网名
     */
    private String username;

    /**
     * 咨询时间
     */
    private Long addTime;

    @TableField(exist = false)
    private String addTimeFormat;

    public String getAddTimeFormat() {
        if(addTime != null){
            addTimeFormat = TimeUtil.timestampToStr(addTime);
        }
        return addTimeFormat;
    }

    /**
     * 1 商品咨询 2 支付咨询 3 配送 4 售后
     */
    private Integer consultType;

    /**
     * 咨询内容
     */
    private String content;

    /**
     * 父id 用于管理员回复
     */
    private Integer parentId;

    /**
     * 店铺id
     */
    private Integer storeId;

    /**
     * 是否显示
     */
    private Integer isShow;

    /**
     * 管理回复状态，0未回复，1已回复
     */
    private Integer status;

    /**
     * 客户id
     */
    private Integer userId;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private String goodsName;

    @TableField(exist = false)
    private String consultTypeDetail;

    public String getConsultTypeDetail() {
        if (consultType != null) {
            if (consultType == 1) {
                return "商品咨询";
            }
            if (consultType == 2) {
                return "支付咨询";
            }
            if (consultType == 3) {
                return "配送";
            }
            if (consultType == 4) {
                return "售后";
            }
        }
        return consultTypeDetail;
    }

    @TableField(exist = false)
    private List<GoodsConsult> childrenConsults;


}
