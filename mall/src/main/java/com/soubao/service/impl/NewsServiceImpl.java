package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.News;
import com.soubao.entity.NewsCat;
import com.soubao.entity.Store;
import com.soubao.entity.User;
import com.soubao.service.NewsCatService;
import com.soubao.service.NewsService;
import com.soubao.service.SellerService;
import com.soubao.service.UserService;
import com.soubao.dao.NewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-02-28
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {
    @Autowired
    private NewsCatService newsCatService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private UserService userService;

    @Override
    public IPage<News> page(Page<News> page, Integer userId, Integer catId, String title) {
        QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();
        if (userId != null){
            newsQueryWrapper.eq("user_id",userId);
        }
        if (catId != null) {
            newsQueryWrapper.eq("cat_id", catId);
        }
        if (!StringUtils.isEmpty(title)) {
            newsQueryWrapper.like("title", title);
        }
        return page(page, newsQueryWrapper.orderByDesc("article_id"));
    }

    @Override
    public void withNewsCat(List<News> newsList) {
        Set<Integer> catIds = newsList.stream().map(News::getCatId).collect(Collectors.toSet());
        if (!catIds.isEmpty()){
            Map<Integer, NewsCat> newsCatMap = newsCatService.listByIds(catIds).stream().collect(Collectors.toMap(NewsCat::getCatId, newsCat -> newsCat));
            for (News news : newsList) {
                news.setNewsCat(newsCatMap.get(news.getCatId()));
            }
        }
    }

    @Override
    public void withSource(List<News> newsList) {
        Set<Integer> userIds = newsList.stream().filter(news -> news.getUserId() != 0).map(News::getUserId).collect(Collectors.toSet());
        Map<Integer, Store> storeMap = new HashMap<>();
        Map<Integer, User> userMap = new HashMap<>();
        if (!userIds.isEmpty()){
            storeMap = sellerService.getStoreListByUserIds(userIds).stream().collect(Collectors.toMap(Store::getUserId, store -> store));
            userMap = userService.listByIds(userIds).stream().collect(Collectors.toMap(User::getUserId, user -> user));
        }
        for (News news : newsList) {
            if (news.getSource() == 0) {
                news.setSourceDesc("总平台");
            } else if (news.getSource() == 1) {
                news.setSourceDesc("店铺：" + storeMap.get(news.getUserId()).getStoreName());
            } else if (news.getSource() == 2) {
                User user = userMap.get(news.getUserId());
                String sourceDesc = StringUtils.isEmpty(user.getRealname()) ? user.getMobile() : user.getRealname();
                news.setSourceDesc("用户：" + sourceDesc);
            }
        }
    }

    @Override
    public boolean saveNews(News news) {
        news.setAddTime(System.currentTimeMillis() / 1000);
        news.setClick(new Random().nextInt(1300 - 1000 + 1) + 1000);
        return save(news);
    }
}
