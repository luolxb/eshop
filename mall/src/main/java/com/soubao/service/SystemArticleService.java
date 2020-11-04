package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.SystemArticle;

/**
 * <p>
 * 系统文章表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-28
 */
public interface SystemArticleService extends IService<SystemArticle> {

    //新增会员协议
    boolean saveSystemArticle(SystemArticle systemArticle);

    //更新会员协议
    boolean updateSystemArticle(SystemArticle systemArticle);
}
