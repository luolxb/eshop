package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 卖家店铺导航信息表
 * </p>
 *
 * @author dyr
 * @since 2020-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("store_navigation")
public class StoreNavigation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 导航ID
     */
    @TableId(value = "sn_id", type = IdType.AUTO)
    private Integer snId;

    /**
     * 导航名称
     */
    @NotBlank(message = "请填写导航名称")
    private String snTitle;

    /**
     * 卖家店铺ID
     */
    private Integer snStoreId;

    /**
     * 导航内容
     */
    private String snContent;

    public String getSnContent() {
        if (!StringUtils.isEmpty(this.snContent)) {
            return HtmlUtils.htmlUnescape(this.snContent);
        }
        return null;
    }

    /**
     * 导航排序
     */
    @Max(value = 255, message = "排序最大值为255")
    private Integer snSort;

    /**
     * 导航是否显示
     */
    private Integer snIsShow;

    /**
     * 添加时间
     */
    private Long snAddTime;

    /**
     * 店铺导航的外链URL
     */
    private String snUrl;

    /**
     * 店铺导航外链是否在新窗口打开：0不开新窗口1开新窗口，默认是0
     */
    private Integer snNewOpen;


}
