package com.soubao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Article;

import java.util.List;

public interface ArticleService extends IService<Article> {
    IPage<Article> page(Page<Article> page, Integer catId, String title);

    void withArticleCat(List<Article> records);

    //添加文章
    boolean saveArticle(Article article);
}
