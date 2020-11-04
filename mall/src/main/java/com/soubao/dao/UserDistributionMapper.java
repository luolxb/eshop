package com.soubao.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.UserDistribution;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soubao.vo.UserDistributionGoodsVo;

/**
 * <p>
 * 用户选择分销商品表 Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2020-05-06
 */
public interface UserDistributionMapper extends BaseMapper<UserDistribution> {

    IPage<UserDistributionGoodsVo> selectGoodsPage(Page page, UserDistribution userDistribution);
}
