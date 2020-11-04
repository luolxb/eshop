package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.User;
import com.soubao.entity.UserSign;
import com.soubao.vo.UserSignStatisticsVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2020-04-08
 */
public interface UserSignService extends IService<UserSign> {

    UserSignStatisticsVo getStatisticsBySign(UserSign sign);

    /**
     * 用户签到
     *
     * @param user
     */
    void sign(User user);

    IPage<UserSign> getUserSignPage(Page page, QueryWrapper<UserSign> wrapper);
}
