package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Brand;
import com.soubao.entity.GoodsCategory;
import com.soubao.vo.GoodsListFilter;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-20
 */
public interface BrandService extends IService<Brand> {
    //商品列表页的品牌筛选链接组
    List<GoodsListFilter> getGoodsListFilterForBrand(String url, GoodsCategory goodsCategory);

    //根据id集合删除品牌
    void removeBrandWithIds(Set<Integer> ids);

    IPage<Brand> getBrandPage(Page<Brand> page, QueryWrapper<Brand> brandQueryWrapper);
}
