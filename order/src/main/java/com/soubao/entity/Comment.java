package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soubao.common.utils.ShopStringUtil;
import com.soubao.common.utils.TimeUtil;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("comment")
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable{
    @TableId(type = IdType.AUTO)
    private Integer commentId;

    private Integer goodsId;

    private Integer orderId;

    private Integer recId;

    private Integer shopId;

    private Integer storeId;

    private Integer userId;

    private Long addTime;

    /**
     * 评论ip地址
     * <p>
     * Table:     tp_comment
     * Column:    ip_address
     * Nullable:  false
     */
    private String ipAddress;

    private Byte isShow;

    private String specKeyName;

    private BigDecimal goodsRank;

    private Integer zanNum;

    private String zanUserid;

    private Integer replyNum;

    private Integer isAnonymous;

    private String impression;

    private Integer deleted;

    private Integer parentId;

    private String orderSn;

    private String content;

    private String img;

    @TableField(exist = false)
    private List<String> imgList;

    public List<String> getImgList() {
        if (StringUtils.isNotEmpty(img)) {
            return Arrays.asList(img.split(","));
        }
        return imgList;
    }


    @TableField(exist = false)
    private String nickname;

    public String getNickname() {
        if (isAnonymous != null) {
            if (isAnonymous != 1) {
                return ShopStringUtil.getStarString(nickname, 0, 3);
            } else {
                return nickname;
            }
        }
        return nickname;
    }

    @TableField(exist = false)
    private String headPic;

    @TableField(exist = false)
    private String goodsName;

    private static final long serialVersionUID = 1L;
}