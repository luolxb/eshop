package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.GroupBuy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soubao.vo.GroupBuyGoodsVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 团购商品表 Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-08-19
 */
public interface GroupBuyMapper extends BaseMapper<GroupBuy> {

    IPage<GroupBuyGoodsVo> selectGroupBuyGoodsPage(Page<GroupBuy> page, @Param(Constants.WRAPPER) QueryWrapper<GroupBuy> queryWrapper);
}
