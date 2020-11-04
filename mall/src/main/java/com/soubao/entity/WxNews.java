package com.soubao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 微信图文
 * </p>
 *
 * @author dyr
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WxNews implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 图片素材id，一个图片为素材可包括几个子图文
     */
    private Integer materialId;

    /**
     * 作者
     */
    private String author;

    /**
     * html内容
     */
    @NotBlank(message = "图文素材正文不能为空")
    private String content;

    /**
     * 摘要
     */
    private String digest;

    /**
     * 封面链接
     */
    private String thumbUrl;

    /**
     * 封面媒体id
     */
    @NotBlank(message = "封面异常，未获取到上传到微信的封面图片media_id")
    private String thumbMediaId;

    /**
     * 原文链接
     */
    private String contentSourceUrl;

    /**
     * 是否显示封面
     */
    private Integer showCoverPic;

    /**
     * 是否打开评论，0不打开，1打开
     */
    private Integer needOpenComment;

    /**
     * 是否粉丝才可评论，0所有人可评论，1粉丝才可评论
     */
    private Integer onlyFansCanComment;

    /**
     * 更新时间
     */
    private Long updateTime;

    public String getContent(){
        if (StringUtils.isNotEmpty(content)) {
            return HtmlUtils.htmlUnescape(content);
        }
        return null;
    }

}
