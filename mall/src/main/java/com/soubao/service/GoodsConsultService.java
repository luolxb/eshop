package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.GoodsConsult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyr
 * @since 2019-11-27
 */
public interface GoodsConsultService extends IService<GoodsConsult> {
    IPage<GoodsConsult> selectConsultPage(Page<GoodsConsult> page, QueryWrapper<GoodsConsult> queryWrapper);

    void withUser(List<GoodsConsult> goodsConsultList);

    IPage<GoodsConsult> userConsultPage(Page<GoodsConsult> goodsConsultPage, User user);

    void withChildren(List<GoodsConsult> records);
}
