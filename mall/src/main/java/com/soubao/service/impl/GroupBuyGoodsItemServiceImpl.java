package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.Goods;
import com.soubao.entity.GroupBuyGoodsItem;
import com.soubao.entity.SpecGoodsPrice;
import com.soubao.service.GoodsService;
import com.soubao.service.GroupBuyGoodsItemService;
import com.soubao.service.SpecGoodsPriceService;
import com.soubao.dao.GroupBuyGoodsItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 服务实现类
 *
 * @author dyr
 * @since 2019-08-19
 */
@Service("groupBuyGoodsItemService")
public class GroupBuyGoodsItemServiceImpl
        extends ServiceImpl<GroupBuyGoodsItemMapper, GroupBuyGoodsItem>
        implements GroupBuyGoodsItemService {
    @Autowired private GoodsService goodsService;
    @Autowired private SpecGoodsPriceService specGoodsPriceService;

    @Override
    public void withGoodsSku(List<GroupBuyGoodsItem> groupBuyGoodsItems) {
        if (groupBuyGoodsItems.size() > 0) {
            if (groupBuyGoodsItems.get(0).getItemId() == 0) {
                GroupBuyGoodsItem groupBuyGoodsItem = groupBuyGoodsItems.get(0);
                Goods goods = goodsService.getById(groupBuyGoodsItem.getGoodsId());
                groupBuyGoodsItem.setKeyName("--");
                groupBuyGoodsItem.setStoreCount(goods.getStoreCount());
                groupBuyGoodsItem.setGoodsPrice(goods.getShopPrice());
            } else {
                Set<Integer> itemIds =
                        groupBuyGoodsItems.stream()
                                .map(GroupBuyGoodsItem::getItemId)
                                .collect(Collectors.toSet());
                List<SpecGoodsPrice> specGoodsPriceList =
                        specGoodsPriceService.list(
                                (new QueryWrapper<SpecGoodsPrice>()).in("item_id", itemIds));
                Map<Long, SpecGoodsPrice> specGoodsPriceMap =
                        specGoodsPriceList.stream()
                                .collect(Collectors.toMap(SpecGoodsPrice::getItemId, spec -> spec));
                for (GroupBuyGoodsItem teamGoodsItem : groupBuyGoodsItems) {
                    SpecGoodsPrice specGoodsPrice =
                            specGoodsPriceMap.get(teamGoodsItem.getItemId().longValue());
                    teamGoodsItem.setKeyName(specGoodsPrice.getKeyName());
                    teamGoodsItem.setStoreCount(specGoodsPrice.getStoreCount());
                    teamGoodsItem.setGoodsPrice(specGoodsPrice.getPrice());
                }
            }
        }
    }
}
