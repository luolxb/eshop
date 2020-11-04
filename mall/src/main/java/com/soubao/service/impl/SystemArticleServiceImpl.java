package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.SystemArticle;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.SystemArticleService;
import com.soubao.dao.SystemArticleMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统文章表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-28
 */
@Service
public class SystemArticleServiceImpl extends ServiceImpl<SystemArticleMapper, SystemArticle> implements SystemArticleService {

    @Override
    public boolean saveSystemArticle(SystemArticle systemArticle) {
        if (count(new QueryWrapper<SystemArticle>().eq("doc_title", systemArticle.getDocTitle())) > 0) {
            throw new ShopException(ResultEnum.NAME_EXISTS);
        }
        if (count(new QueryWrapper<SystemArticle>().eq("doc_code", systemArticle.getDocCode())) > 0) {
            throw new ShopException(ResultEnum.DOC_CODE_EXISTS);
        }
        systemArticle.setDocTime(System.currentTimeMillis() / 1000);
        return save(systemArticle);
    }

    @Override
    public boolean updateSystemArticle(SystemArticle systemArticle) {
        if (count(new QueryWrapper<SystemArticle>().eq("doc_title", systemArticle.getDocTitle())
                .ne("doc_id", systemArticle.getDocId())) > 0) {
            throw new ShopException(ResultEnum.NAME_EXISTS);
        }
        int count = count(new QueryWrapper<SystemArticle>().eq("doc_code", systemArticle.getDocCode())
                .ne("doc_id", systemArticle.getDocId()));
        if (count > 0) {
            throw new ShopException(ResultEnum.DOC_CODE_EXISTS);
        }
        systemArticle.setDocTime(System.currentTimeMillis() / 1000);
        return updateById(systemArticle);
    }
}
