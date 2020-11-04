package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.Article;
import com.soubao.entity.ArticleCat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Brand;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyr
 * @since 2020-03-02
 */
public interface ArticleCatService extends IService<ArticleCat> {
    //添加文章分类
    void saveArticleCat(ArticleCat articleCat);

    //更新文章分类
    void updateArticleCat(ArticleCat articleCat);

    //删除文章分类
    void removeArticleCat(Integer catId);

    List<ArticleCat> listToTree(List<ArticleCat> list);

    void withArticle(List<ArticleCat> list, QueryWrapper<Article> queryWrapper);
}
