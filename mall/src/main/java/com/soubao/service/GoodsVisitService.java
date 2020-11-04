package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Goods;
import com.soubao.entity.GoodsVisit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.User;
import com.soubao.vo.DateVo;

import java.util.List;


/**
 * <p>
 * 商品浏览历史表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-15
 */
public interface GoodsVisitService extends IService<GoodsVisit> {
    //用户访问商品,记录日志
    void loggingGoods(User user, Goods goods);
    //获取浏览记录根据日期分组
    Page<DateVo> getVisitByDateGroup(Page<DateVo> page, QueryWrapper<GoodsVisit> wrapper);

    List<GoodsVisit> getVisitCatCount(User user);
}
