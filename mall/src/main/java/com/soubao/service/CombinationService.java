package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Combination;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.User;

import java.util.List;

/**
 * <p>
 * 组合促销表 服务类
 * </p>
 *
 * @author dyr
 * @since 2020-05-19
 */
public interface CombinationService extends IService<Combination> {

    void withStore(List<Combination> records);

    IPage<Combination> getCombinationPage(Page<Combination> combinationPage, QueryWrapper<Combination> wrapper);

    Combination getCombination(Integer combinationId);

    void addCart(User user, Combination combination, Integer num);

    void withCombinationGoods(List<Combination> records);

    void schedule();

    void deleteTask();

    void isMaster(Combination combination);
}
