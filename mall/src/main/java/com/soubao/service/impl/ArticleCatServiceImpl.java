package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.Article;
import com.soubao.entity.ArticleCat;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.entity.TeamFollow;
import com.soubao.entity.TeamFound;
import com.soubao.service.ArticleCatService;
import com.soubao.service.ArticleService;
import com.soubao.dao.ArticleCatMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-03-02
 */
@Service
public class ArticleCatServiceImpl extends ServiceImpl<ArticleCatMapper, ArticleCat> implements ArticleCatService {
    @Autowired
    private ArticleService articleService;

    @Override
    public void saveArticleCat(ArticleCat articleCat) {
        if (count(new QueryWrapper<ArticleCat>().eq("cat_name", articleCat.getCatName())) > 0) {
            throw new ShopException(ResultEnum.NAME_EXISTS);
        }
        save(articleCat);
    }

    @Override
    public void updateArticleCat(ArticleCat articleCat) {
        if (count(new QueryWrapper<ArticleCat>().eq("cat_name", articleCat.getCatName())
                .ne("cat_id", articleCat.getCatId())) > 0) {
            throw new ShopException(ResultEnum.NAME_EXISTS);
        }
        if (articleCat.getCatType() == 1 && articleCat.getParentId() > 1 ){
            throw new ShopException(ResultEnum.CANNOT_EDIT_SYSTEM_ARTICLE_CAT);
        }
        if (articleCat.getParentId().equals(articleCat.getCatId())){
            throw new ShopException(ResultEnum.PARENT_CANNOT_BE_HIMSELF);
        }
        //过滤-把分类设定为其子级的子级操作
        if (articleCat.getParentId() != 0){
            //获取该分类的所有子级分类id
            Set<Integer> ids = new HashSet<>();
            Set<Integer> childrenIds = new HashSet<>();
            ids.add(articleCat.getCatId());
            getChildrenIds(ids, childrenIds);
            if (childrenIds.contains(articleCat.getParentId())){
                throw new ShopException(ResultEnum.NOT_SET_TO_CHILDREN);
            }
        }
        updateById(articleCat);
    }

    private void getChildrenIds(Set<Integer> parentIds, Set<Integer> childrenIds){
        Set<Integer> cids = list(new QueryWrapper<ArticleCat>().in("parent_id", parentIds))
                .stream().map(ArticleCat::getCatId).collect(Collectors.toSet());
        if (!cids.isEmpty()){
            childrenIds.addAll(cids);
            getChildrenIds(cids, childrenIds);
        }
    }

    @Override
    public void removeArticleCat(Integer catId) {
        if (catId < 9){
            throw new ShopException(ResultEnum.CANNOT_REMOVE_SYSTEM_CAT);
        }
        if (count(new QueryWrapper<ArticleCat>().eq("parent_id", catId)) > 0){
            throw new ShopException(ResultEnum.CAT_HAVE_CHILD);
        }
        if (articleService.count(new QueryWrapper<Article>().eq("cat_id", catId)) > 0){
            throw new ShopException(ResultEnum.CAT_HAVE_ARTICLE);
        }
        removeById(catId);
    }

    @Override
    public List<ArticleCat> listToTree(List<ArticleCat> list) {
        List<ArticleCat> treeList = new ArrayList<>();
        for (ArticleCat tree : list) {
            if (tree.getParentId() == 0){
                treeList.add(findChildren(tree, list));
            }
        }
        return treeList;
    }

    @Override
    public void withArticle(List<ArticleCat> list, QueryWrapper<Article> queryWrapper) {
        if (list.size() == 0) {
            return;
        }
        Set<Integer> catIds = list.stream().map(ArticleCat::getCatId).collect(Collectors.toSet());
        List<Article> articleList = articleService.list(queryWrapper.in("cat_id", catIds));
        Map<Integer, List<Article>> articleListMap = articleList.stream().collect(Collectors.groupingBy(Article::getCatId));
        for (ArticleCat articleCat : list) {
            articleCat.setArticles(articleListMap.containsKey(articleCat.getCatId()) ?
                    articleListMap.get(articleCat.getCatId()) : new ArrayList<>());
        }
    }

    private ArticleCat findChildren(ArticleCat tree, List<ArticleCat> list) {
        for (ArticleCat node : list) {
            if (node.getParentId().equals(tree.getCatId())) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<>());
                }
                tree.getChildren().add(findChildren(node, list));
            }
        }
        return tree;
    }
}
