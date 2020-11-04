package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.News;
import com.soubao.entity.NewsCat;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.NewsCatService;
import com.soubao.service.NewsService;
import com.soubao.dao.NewsCatMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-02-28
 */
@Service
public class NewsCatServiceImpl extends ServiceImpl<NewsCatMapper, NewsCat> implements NewsCatService {
    @Autowired
    private NewsService newsService;

    @Override
    public void saveNewsCat(NewsCat newsCat) {
        if (count(new QueryWrapper<NewsCat>()
                .eq("cat_name", newsCat.getCatName())) > 0){
            throw new ShopException(ResultEnum.NAME_EXISTS);
        }
        save(newsCat);
    }

    @Override
    public void updateNewsCat(NewsCat newsCat) {
        if (count(new QueryWrapper<NewsCat>()
                .eq("cat_name", newsCat.getCatName())
                .ne("cat_id", newsCat.getCatId())) > 0){
            throw new ShopException(ResultEnum.NAME_EXISTS);
        }
        updateById(newsCat);
    }

    @Override
    public void removeNewsCat(Integer catId) {
        if (newsService.count(new QueryWrapper<News>().eq("cat_id", catId)) > 0){
            throw new ShopException(ResultEnum.NEWS_EXISTS_CAN_NOT_REMOVE);
        }
        removeById(catId);
    }
}
