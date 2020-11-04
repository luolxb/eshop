package com.soubao.service;

import com.soubao.entity.NewsComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyr
 * @since 2020-03-02
 */
public interface NewsCommentService extends IService<NewsComment> {

    void withUser(List<NewsComment> newsCommentList);
}
