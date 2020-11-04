package com.soubao.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.FreightTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 运费模板表 Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-08-14
 */
public interface FreightTemplateMapper extends BaseMapper<FreightTemplate> {

    IPage<FreightTemplate> selectPageWithConfig(Page page, @Param("freightTemplate") FreightTemplate freightTemplate);

    FreightTemplate selectPageWithConfig(@Param("freightTemplate") FreightTemplate freightTemplate);
}
