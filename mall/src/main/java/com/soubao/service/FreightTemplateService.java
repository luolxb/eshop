package com.soubao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.FreightTemplate;

/**
 * <p>
 * 运费模板表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-14
 */
public interface FreightTemplateService extends IService<FreightTemplate> {

    /**
     * 获取模板列表分页
     * @return
     */
    IPage<FreightTemplate> getPageWithConfig(Page<FreightTemplate> page, Integer storeId);

    /**
     * 获取模板
     * @param templateId
     * @return
     */
    FreightTemplate getOneWithConfigById(Integer templateId);

    /**
     * 更新模板
     * @param freightTemplate
     */
    void updateWithConfigAndRegion(FreightTemplate freightTemplate);
}
