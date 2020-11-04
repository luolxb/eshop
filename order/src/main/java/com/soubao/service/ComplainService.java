package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Complain;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 投诉表 服务类
 * </p>
 *
 * @author dyr
 * @since 2020-02-28
 */
public interface ComplainService extends IService<Complain> {

    IPage<Complain> getComplainPage(Page<Complain> page, QueryWrapper<Complain> queryWrapper);
}
