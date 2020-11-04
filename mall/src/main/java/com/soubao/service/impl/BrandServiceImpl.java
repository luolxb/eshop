package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.Brand;
import com.soubao.entity.Goods;
import com.soubao.entity.GoodsCategory;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.BrandService;
import com.soubao.service.GoodsService;
import com.soubao.dao.BrandMapper;
import com.soubao.vo.GoodsListFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-20
 */
@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private GoodsService goodsService;

    @Override
    public List<GoodsListFilter> getGoodsListFilterForBrand(String url, GoodsCategory goodsCategory) {
        List<GoodsListFilter> goodsListFilters = new ArrayList<>();
        if (goodsCategory == null) {
            return goodsListFilters;
        }
        UriComponentsBuilder parentSb = ServletUriComponentsBuilder.fromUriString(url);
        List<String> brandIdsQueryList = parentSb.build().getQueryParams().get("brand_ids");
        if (brandIdsQueryList != null && brandIdsQueryList.get(0).length() > 0) {
            return goodsListFilters;
        }
        List<Brand> brandList = brandMapper.selectAttrForGoodsList(goodsCategory);
        for (Brand brand : brandList) {
            goodsListFilters.add(new GoodsListFilter(brand.getName(),
                    parentSb.replaceQueryParam("brand_ids", brand.getId()).build().getQuery()));
        }
        return goodsListFilters;
    }

    @Override
    public void removeBrandWithIds(Set<Integer> ids) {
        //判断品牌是否有商品在使用
        List<Goods> goodsList = goodsService.list(new QueryWrapper<Goods>()
                .in("brand_id", ids)
                .groupBy("brand_id")
                .select("brand_id"));
        if (!goodsList.isEmpty()) {
            Set<Integer> idSet = goodsList.stream().map(Goods::getBrandId).collect(Collectors.toSet());
            throw new ShopException(ResultEnum.FAIL.getCode(), "ID为" + idSet + "的品牌有商品在用不得删除!");
        }
        removeByIds(ids);
    }

    @Override
    public IPage<Brand> getBrandPage(Page<Brand> page, QueryWrapper<Brand> brandQueryWrapper) {
        return brandMapper.selectBrandPage(page,brandQueryWrapper);
    }

}
