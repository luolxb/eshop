package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "广告对象", description = "ad表")
public class Ad implements Serializable {
    @ApiModelProperty(value = "广告id")
    @TableId(value = "ad_id", type = IdType.AUTO)
    private Integer adId;

    @ApiModelProperty(value = "广告位置ID")
    @NotNull(message = "请选择广告位置")
    private Integer pid;

    @ApiModelProperty(value = "广告类型")
    private Byte mediaType;

    @ApiModelProperty(value = "广告名称")
    @NotBlank(message = "广告名称不能为空")
    private String adName;

    @ApiModelProperty(value = "链接地址")
    @NotBlank(message = "广告链接不能为空")
    private String adLink;

    public String getAdLink() {
        if (mediaType != null && mediaType == 4) {
            //如果是分类, 截取最后一个分类
            if (!StringUtils.isEmpty(adLink) && adLink.indexOf("_") > 0) {
                return adLink.substring(adLink.lastIndexOf("_") + 1);
            }
        }
        return adLink;
    }

    @ApiModelProperty(value = "投放时间")
    private Long startTime;

    @ApiModelProperty(value = "结束时间")
    private Long endTime;

    @ApiModelProperty(value = "添加人")
    private String linkMan;

    @ApiModelProperty(value = "添加人邮箱")
    private String linkEmail;

    @ApiModelProperty(value = "添加人联系电话")
    private String linkPhone;

    @ApiModelProperty(value = "点击量")
    private Integer clickCount;

    @ApiModelProperty(value = "是否显示")
    private Byte enabled;

    @ApiModelProperty(value = "排序")
    private Short orderby;

    @ApiModelProperty(value = "是否开启浏览器新窗口")
    private Integer target;

    @ApiModelProperty(value = "背景颜色")
    @NotBlank(message = "请选择背景颜色")
    private String bgcolor;

    @ApiModelProperty(value = "APP端广告位key,该值固定, APP端通过该key索引对应广告位")
    private String adKey;

    @ApiModelProperty(value = "图片地址")
    @NotBlank(message = "广告图片不能为空")
    private String adCode;

    @ApiModelProperty("广告位置对象")
    @TableField(exist = false)
    private AdPosition adPosition;

    private static final long serialVersionUID = 1L;
}