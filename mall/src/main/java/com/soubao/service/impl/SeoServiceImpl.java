package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.Seo;
import com.soubao.service.SeoService;
import com.soubao.dao.SeoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * SEO信息存放表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-02-27
 */
@Service
public class SeoServiceImpl extends ServiceImpl<SeoMapper, Seo> implements SeoService {

    @Override
    public void updateSeo(List<Seo> seos) {
        for (Seo seo:seos) {
            if (count(new QueryWrapper<Seo>().eq("type", seo.getType())) > 0){
                updateById(seo);
            }else{
                save(seo);
            }
        }
    }
}
