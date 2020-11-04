package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.FreightConfig;
import com.soubao.entity.Goods;
import com.soubao.entity.Region;

/**
 * <p>
 * 运费配置表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-14
 */
public interface FreightConfigService extends IService<FreightConfig> {
    //根据商品和地区返回对应的区域配置
    FreightConfig getOneByGoodsAndRegion(Goods goods, Integer regionId);
}
