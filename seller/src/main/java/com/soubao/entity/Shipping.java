package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("shipping")
@ApiModel("物流对象")
public class Shipping implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("物流公司id")
    @TableId(value = "shipping_id", type = IdType.AUTO)
    private Integer shippingId;

    @ApiModelProperty("物流公司名称")
    @NotBlank(message = "物流公司名称不能为空")
    private String shippingName;

    @NotBlank(message = "物流公司编码不能为空")
    @ApiModelProperty("物流公司编码")
    private String shippingCode;

    @ApiModelProperty("物流描述")
    private String shippingDesc;

    @NotBlank(message = "请上传物流公司logo")
    @ApiModelProperty("物流公司logo")
    private String shippingLogo;

    @ApiModelProperty("运单模板宽度")
    private Integer templateWidth;

    @ApiModelProperty("运单模板高度")
    private Integer templateHeight;

    @ApiModelProperty("运单模板左偏移量")
    private Integer templateOffsetX;

    @ApiModelProperty("运单模板上偏移量")
    private Integer templateOffsetY;

    @NotBlank(message = "请上传运单模板")
    @ApiModelProperty("运单模板图片")
    private String templateImg;

    @ApiModelProperty("打印项偏移校正")
    private String templateHtml;

    @TableField(exist = false)
    private String htmlEncode;

    public String getHtmlEncode() {
        if (StringUtils.isNotEmpty(htmlEncode)) {
            return htmlEncode;
        }
        if (null != templateHtml) {
            return HtmlUtils.htmlUnescape(templateHtml);
        }
        return htmlEncode;
    }


}
