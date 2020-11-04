package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Seo;

import java.util.List;

/**
 * <p>
 * SEO信息存放表 服务类
 * </p>
 *
 * @author dyr
 * @since 2020-02-27
 */
public interface SeoService extends IService<Seo> {

    //更新seo设置
    void updateSeo(List<Seo> seos);
}
