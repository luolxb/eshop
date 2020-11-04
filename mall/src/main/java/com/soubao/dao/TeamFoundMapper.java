package com.soubao.dao;

import com.soubao.entity.TeamFound;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 开团表 Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-10-30
 */
public interface TeamFoundMapper extends BaseMapper<TeamFound> {

    /**
     * 拼团成功数
     * @param teamId
     * @return
     */
    int countTeamSuccessCount(Integer teamId);
}
