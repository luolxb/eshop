package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.*;
import com.soubao.service.FreightConfigService;
import com.soubao.service.FreightRegionService;
import com.soubao.service.FreightTemplateService;
import com.soubao.service.RegionService;
import com.soubao.dao.FreightConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 运费配置表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-14
 */
@Slf4j
@Service("freightConfigService")
public class FreightConfigServiceImpl extends ServiceImpl<FreightConfigMapper, FreightConfig> implements FreightConfigService {
    @Autowired
    private RegionService regionService;
    @Autowired
    private FreightRegionService freightRegionService;
    @Autowired
    private FreightTemplateService freightTemplateService;

    @Override
    public FreightConfig getOneByGoodsAndRegion(Goods goods, Integer regionId) {
        //获取区域配置
        FreightRegion freightRegion = freightRegionService.getOne((new QueryWrapper<FreightRegion>()).
                eq("template_id",goods.getTemplateId()).eq("region_id", regionId).last("limit 1"));
        if(freightRegion == null){
            List<Region> regionList = regionService.selectParentListById(regionId);
            Set<Integer> regionIdSet = regionList.stream().map(Region::getId).collect(Collectors.toSet());
            if (regionIdSet.size() > 0) {
                regionIdSet.remove(regionId);
                freightRegion = freightRegionService.getOne((new QueryWrapper<FreightRegion>())
                        .eq("template_id", goods.getTemplateId())
                        .in("region_id", regionIdSet)
                        .orderByAsc("region_id").last("limit 1"));
            }
        }
        //区域配置没有找到，就去看下模板是否启用默认配置
        if(freightRegion == null){
            FreightTemplate freightTemplate = freightTemplateService.getById(goods.getTemplateId());
            if (freightTemplate != null && freightTemplate.getIsEnableDefault().equals(1)) {
                return getOne((new QueryWrapper<FreightConfig>())
                        .eq("template_id", goods.getTemplateId())
                        .eq("is_default", 1));
            } else {
                return null;
            }
        }
        //区域配置找到，就返回区域对应的配置
        return getOne((new QueryWrapper<FreightConfig>()).eq("config_id", freightRegion.getConfigId()));
    }
}
