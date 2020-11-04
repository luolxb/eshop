package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.FreightConfig;
import com.soubao.entity.FreightRegion;
import com.soubao.entity.FreightTemplate;
import com.soubao.service.FreightConfigService;
import com.soubao.service.FreightRegionService;
import com.soubao.service.FreightTemplateService;
import com.soubao.dao.FreightTemplateMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 运费模板表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-14
 */
@Service("freightTemplateService")
public class FreightTemplateServiceImpl extends ServiceImpl<FreightTemplateMapper, FreightTemplate> implements FreightTemplateService {
    @Autowired
    private FreightTemplateMapper freightTemplateMapper;
    @Autowired
    private FreightRegionService freightRegionService;
    @Autowired
    private FreightConfigService freightConfigService;
    @Override
    public IPage<FreightTemplate> getPageWithConfig(Page page, Integer storeId) {
        FreightTemplate freightTemplate = new FreightTemplate();
        freightTemplate.setStoreId(storeId);
        return freightTemplateMapper.selectPageWithConfig(page, freightTemplate);
    }

    public FreightTemplate getOneWithConfigById(Integer templateId) {
        FreightTemplate freightTemplate = new FreightTemplate();
        freightTemplate.setTemplateId(templateId);
        return freightTemplateMapper.selectPageWithConfig(freightTemplate);
    }

    @Override
    public void updateWithConfigAndRegion(FreightTemplate freightTemplate) {
        updateById(freightTemplate);
        List<FreightConfig> freightConfigList = freightTemplate.getFreightConfigList();
        if(freightConfigList == null || freightConfigList.size() == 0){
            //模板下没有配送区域配置，就删除该模板
            removeById(freightTemplate.getTemplateId());
            freightConfigService.remove((new QueryWrapper<FreightConfig>()).eq("template_id", freightTemplate.getTemplateId()));
            freightRegionService.remove((new QueryWrapper<FreightRegion>()).in("template_id", freightTemplate.getTemplateId()));
        }else{
            Set<Integer> configIds = new HashSet<>();
            for (FreightConfig freightConfig : freightConfigList) {
                freightConfig.setTemplateId(freightTemplate.getTemplateId());
                if(freightConfig.getConfigId() != null){
                    configIds.add(freightConfig.getConfigId());
                }
            }
            List<FreightConfig> exFreightConfigList = freightConfigService.list((new QueryWrapper<FreightConfig>())
                    .eq("template_id", freightTemplate.getTemplateId()));
            if(exFreightConfigList.size() > 0){
                Set<Integer> exConfigIds = exFreightConfigList.stream().map(FreightConfig::getConfigId).collect(Collectors.toSet());
                exConfigIds.removeAll(configIds);
                if(exConfigIds.size() > 0){
                    freightConfigService.remove((new QueryWrapper<FreightConfig>()).in("config_id", exConfigIds));
                    freightRegionService.remove((new QueryWrapper<FreightRegion>()).in("config_id", exConfigIds));
                }
            }
            freightConfigService.saveOrUpdateBatch(freightConfigList);
            freightRegionService.remove((new QueryWrapper<FreightRegion>()).in("template_id", freightTemplate.getTemplateId()));
            List<FreightRegion> newFreightRegionList = new ArrayList<>();
            for (FreightConfig freightConfig : freightConfigList) {
                for(FreightRegion freightRegion : freightConfig.getFreightRegionList()){
                    freightRegion.setConfigId(freightConfig.getConfigId());
                    freightRegion.setTemplateId(freightTemplate.getTemplateId());
                    newFreightRegionList.add(freightRegion);
                }
            }
            freightRegionService.saveBatch(newFreightRegionList);
        }
    }

}
