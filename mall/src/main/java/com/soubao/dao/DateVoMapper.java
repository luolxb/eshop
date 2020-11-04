package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.GoodsVisit;
import com.soubao.entity.Store;
import com.soubao.entity.User;
import com.soubao.vo.DateVo;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author dyr
 * @since 2019-08-13
 */
public interface DateVoMapper extends BaseMapper<Store> {
    //查询浏览记录根据日期分组
    Page<DateVo> selectVisitByDateGroup(Page page, @Param(Constants.WRAPPER) QueryWrapper<GoodsVisit> wrapper);
}
