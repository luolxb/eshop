package com.soubao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.News;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyr
 * @since 2020-02-28
 */
public interface NewsService extends IService<News> {
    //新闻分页
    IPage<News> page(Page<News> page, Integer userId, Integer catId, String title);

    void withNewsCat(List<News> newsList);

    void withSource(List<News> newsList);

    //添加新闻
    boolean saveNews(News news);
}
