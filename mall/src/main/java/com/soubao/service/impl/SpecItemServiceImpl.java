package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.SpecGoodsPrice;
import com.soubao.entity.SpecImage;
import com.soubao.entity.SpecItem;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.SpecGoodsPriceService;
import com.soubao.service.SpecImageService;
import com.soubao.service.SpecItemService;
import com.soubao.dao.SpecItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-22
 */
@Service("specItemService")
public class SpecItemServiceImpl extends ServiceImpl<SpecItemMapper, SpecItem> implements SpecItemService {
    @Autowired
    private SpecImageService specImageService;
    @Autowired
    private SpecGoodsPriceService specGoodsPriceService;
    @Override
    public void deleteSpecItem(SpecItem specItem) {
        int specGoodsPriceCount = specGoodsPriceService.count((new QueryWrapper<SpecGoodsPrice>())
                .eq("`key`", specItem.getId()).or().like("`key`", "_" + specItem.getId() +"_")
                .or().like("`key`", "_" + specItem.getId()).or().like("`key`", specItem.getId() +"_"));
        if(specGoodsPriceCount > 0){
            throw new ShopException(ResultEnum.SPEC_HAVE_GOODS);
        }
        this.removeById(specItem.getId());
        specImageService.remove((new QueryWrapper<SpecImage>()).eq("spec_image_id", specItem.getId()));
    }

    @Override
    public void deleteSpecItemList(List<SpecItem> specItemList) {
        int specGoodsPriceCount = 0;
        Set<Integer> specItemIds = new HashSet<>();
        for (SpecItem specItem : specItemList) {
            specGoodsPriceCount += specGoodsPriceService.count((new QueryWrapper<SpecGoodsPrice>())
                    .eq("`key`", specItem.getId()).or().like("`key`", "%_" + specItem.getId() +"_%")
                    .or().like("`key`", "%_" + specItem.getId()).or().like("", specItem.getId() +"_%"));
            specItemIds.add(specItem.getId());
        }
        if(specGoodsPriceCount > 0){
            throw new ShopException(ResultEnum.SPEC_HAVE_GOODS);
        }
        this.removeByIds(specItemIds);
        specImageService.remove((new QueryWrapper<SpecImage>()).in("spec_image_id", specItemIds));
    }
}
