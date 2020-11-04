package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.Article;
import com.soubao.entity.ArticleCat;
import com.soubao.service.ArticleCatService;
import com.soubao.service.ArticleService;
import com.soubao.dao.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;


@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private ArticleCatService articleCatService;

    @Override
    public IPage<Article> page(Page<Article> page, Integer catId, String title) {
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        if (catId != null) {
            articleQueryWrapper.eq("cat_id", catId);
        }
        if (!StringUtils.isEmpty(title)) {
            articleQueryWrapper.like("title", title);
        }
        return page(page, articleQueryWrapper.orderByDesc("article_id"));
    }

    @Override
    public void withArticleCat(List<Article> records) {
        Set<Integer> catIds = records.stream().map(Article::getCatId).collect(Collectors.toSet());
        if (!catIds.isEmpty()){
            Map<Integer, ArticleCat> articleCatMap = articleCatService.listByIds(catIds).stream()
                    .collect(Collectors.toMap(ArticleCat::getCatId, articleCat -> articleCat));
            for (Article article:records) {
                article.setArticleCat(articleCatMap.get(article.getCatId()));
            }
        }
    }

    @Override
    public boolean saveArticle(Article article) {
        article.setAddTime(System.currentTimeMillis() / 1000);
        article.setClick(new Random().nextInt(1300 - 1000 + 1) + 1000);
        return save(article);
    }
}
