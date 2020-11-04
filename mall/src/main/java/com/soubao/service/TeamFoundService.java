package com.soubao.service;

import com.soubao.entity.Order;
import com.soubao.entity.TeamFound;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 开团表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-10-30
 */
public interface TeamFoundService extends IService<TeamFound> {

    void withStore(List<TeamFound> records);

    void withOrder(List<TeamFound> records);

    void withSellerOrder(List<TeamFound> records);

    void withTeamActivity(List<TeamFound> records);

    void withTeamFollow(List<TeamFound> records);

    void withTeamFollowOrder(List<TeamFound> records);

    void withTeamFollowSellerOrder(List<TeamFound> records);

    void withTeamFound(List<Order> records);

    void withOrderGoods(List<TeamFound> records);

    /**
     * 定时检查拼主是否过期
     */
    void schedule();

    void withTeamFollowOrderGoods(List<TeamFound> records);

    /**
     * 拼团退款
     * @param teamFound
     */
    String refund(Integer sellerId, TeamFound teamFound);
}
