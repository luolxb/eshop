package com.soubao.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.TeamActivity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 拼团活动表 Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-10-30
 */
public interface TeamActivityMapper extends BaseMapper<TeamActivity> {

    IPage<TeamActivity> selectGoodsPage(Page page, @Param("teamActivity") TeamActivity teamActivity);

    /**
     * 查找活动附上店铺和拼团熟练
     * @param page
     * @param teamActivity
     * @return
     */
    IPage<TeamActivity> selectPageWithStore(Page page, TeamActivity teamActivity);
}
