package com.soubao.service;

import com.soubao.entity.NewsCat;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyr
 * @since 2020-02-28
 */
public interface NewsCatService extends IService<NewsCat> {
    //添加新闻分类
    void saveNewsCat(NewsCat newsCat);

    //更新新闻分类
    void updateNewsCat(NewsCat newsCat);

    //删除新闻分类
    void removeNewsCat(Integer catId);
}
