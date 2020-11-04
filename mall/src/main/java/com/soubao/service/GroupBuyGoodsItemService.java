package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.GroupBuyGoodsItem;

import java.util.List;

/**
 * 服务类
 *
 * @author dyr
 * @since 2019-08-19
 */
public interface GroupBuyGoodsItemService extends IService<GroupBuyGoodsItem> {

    void withGoodsSku(List<GroupBuyGoodsItem> groupBuyGoodsItems);
}
