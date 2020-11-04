package com.soubao.service;

import com.soubao.entity.Order;
import com.soubao.entity.TeamFollow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 参团表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-10-30
 */
public interface TeamFollowService extends IService<TeamFollow> {

    void withTeamFollow(List<Order> records);

}
