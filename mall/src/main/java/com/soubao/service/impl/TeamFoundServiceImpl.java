package com.soubao.service.impl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.*;
import com.soubao.dao.PreSellMapper;
import com.soubao.dao.TeamFoundMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 开团表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-10-30
 */
@Slf4j
@Service("teamFoundService")
public class TeamFoundServiceImpl extends ServiceImpl<TeamFoundMapper, TeamFound> implements TeamFoundService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private TeamActivityService teamActivityService;
    @Autowired
    private TeamFollowService teamFollowService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${spring.datasource.name}")
    private String dataBaseName;
    @Autowired
    private PreSellMapper preSellMapper;
    @Autowired
    private SellerService sellerService;

    @Override
    public void withStore(List<TeamFound> records) {
        if (records.size() == 0) {
            return;
        }
        Set<Integer> storeIds = records.stream().map(TeamFound::getStoreId).collect(Collectors.toSet());
        List<Store> storeList = sellerService.getStoreListByIds(storeIds);
        Map<Integer, Store> storeMap = storeList.stream().collect(Collectors.toMap(Store::getStoreId, store -> store));
        for (TeamFound teamFound : records) {
            teamFound.setStore(storeMap.get(teamFound.getStoreId()));
        }
    }

    @Override
    public void withOrder(List<TeamFound> records) {
        if (records.size() == 0) {
            return;
        }
        Set<Integer> orderIds = records.stream().map(TeamFound::getOrderId).collect(Collectors.toSet());
        List<Order> orderList = orderService.getOrderListByIds(orderIds);
        Map<Integer, Order> orderMap = orderList.stream().collect(Collectors.toMap(Order::getOrderId, order -> order));
        for (TeamFound teamFound : records) {
            teamFound.setOrder(orderMap.get(teamFound.getOrderId()));
        }
        regionService.inOrder(orderList);
    }

    @Override
    public void withSellerOrder(List<TeamFound> records) {
        if (records.size() == 0) {
            return;
        }
        Set<Integer> orderIds = records.stream().map(TeamFound::getOrderId).collect(Collectors.toSet());
        List<Order> orderList = orderService.getSellerOrderListByIds(orderIds);
        Map<Integer, Order> orderMap = orderList.stream().collect(Collectors.toMap(Order::getOrderId, order -> order));
        for (TeamFound teamFound : records) {
            teamFound.setOrder(orderMap.get(teamFound.getOrderId()));
        }
        regionService.inOrder(orderList);
    }


    @Override
    public void withTeamActivity(List<TeamFound> records) {
        if (records.size() == 0) {
            return;
        }
        Set<Integer> teamActivityIds = records.stream().map(TeamFound::getTeamId).collect(Collectors.toSet());
        List<TeamActivity> teamActivityList = teamActivityService.list((new QueryWrapper<TeamActivity>()).in("team_id", teamActivityIds));
        Map<Integer, TeamActivity> teamActivityMap = teamActivityList.stream().collect(Collectors.toMap(TeamActivity::getTeamId, team -> team));
        for (TeamFound teamFound : records) {
            teamFound.setTeamActivity(teamActivityMap.get(teamFound.getTeamId()));
        }
    }

    @Override
    public void withTeamFollow(List<TeamFound> records) {
        if (records.size() == 0) {
            return;
        }
        Set<Integer> teamFoundIds = records.stream().map(TeamFound::getFoundId).collect(Collectors.toSet());
        List<TeamFollow> teamFollowList = teamFollowService.list((new QueryWrapper<TeamFollow>()).in("found_id", teamFoundIds));
        Map<Integer, List<TeamFollow>> teamFollowListMap = teamFollowList.stream().collect(Collectors.groupingBy(TeamFollow::getFoundId));
        for (TeamFound teamFound : records) {
            teamFound.setTeamFollowList(teamFollowListMap.containsKey(teamFound.getFoundId()) ?
                    teamFollowListMap.get(teamFound.getFoundId()) : new ArrayList<>());
        }
    }

    @Override
    public void withTeamFollowOrder(List<TeamFound> records) {
        if (records.size() == 0) {
            return;
        }
        Set<Integer> teamFollowOrderIds = new HashSet<>();
        for (TeamFound teamFound : records) {
            for (TeamFollow teamFollow : teamFound.getTeamFollowList()) {
                teamFollowOrderIds.add(teamFollow.getOrderId());
            }
        }
        if (teamFollowOrderIds.size() == 0) {
            return;
        }
        List<Order> orderList = orderService.getOrderListByIds(teamFollowOrderIds);
        Map<Integer, Order> orderMap = orderList.stream().collect(Collectors.toMap(Order::getOrderId, order -> order));
        for (TeamFound teamFound : records) {
            for (TeamFollow teamFollow : teamFound.getTeamFollowList()) {
                teamFollow.setOrder(orderMap.get(teamFollow.getOrderId()));
            }
        }
    }

    @Override
    public void withTeamFollowSellerOrder(List<TeamFound> records) {
        if (records.size() == 0) {
            return;
        }
        Set<Integer> teamFollowOrderIds = new HashSet<>();
        for (TeamFound teamFound : records) {
            for (TeamFollow teamFollow : teamFound.getTeamFollowList()) {
                teamFollowOrderIds.add(teamFollow.getOrderId());
            }
        }
        if (teamFollowOrderIds.size() == 0) {
            return;
        }
        List<Order> orderList = orderService.getSellerOrderListByIds(teamFollowOrderIds);
        Map<Integer, Order> orderMap = orderList.stream().collect(Collectors.toMap(Order::getOrderId, order -> order));
        for (TeamFound teamFound : records) {
            for (TeamFollow teamFollow : teamFound.getTeamFollowList()) {
                teamFollow.setOrder(orderMap.get(teamFollow.getOrderId()));
            }
        }
    }


    @Override
    public void withTeamFound(List<Order> records) {
        if (records.size() == 0) {
            return;
        }
        Set<Integer> orderIds = records.stream().map(Order::getOrderId).collect(Collectors.toSet());
        List<TeamFound> teamFounds = list((new QueryWrapper<TeamFound>()).in("order_id", orderIds));
        Map<Integer, TeamFound> teamFoundMap = teamFounds.stream().collect(Collectors.toMap(TeamFound::getOrderId, found -> found));
        for (Order order : records) {
            order.setTeamFound(teamFoundMap.getOrDefault(order.getOrderId(), new TeamFound()));
        }
    }

    @Override
    public void withOrderGoods(List<TeamFound> records) {
        if (records.size() == 0) {
            return;
        }
        Set<Integer> foundOrderIds = records.stream().map(TeamFound::getOrderId).collect(Collectors.toSet());
        List<OrderGoods> orderGoodsList = orderService.getOrderGoodsListByOrderIds(foundOrderIds);
        Map<Integer, List<OrderGoods>> orderGoodsListMap = orderGoodsList.stream().collect(Collectors.groupingBy(OrderGoods::getOrderId));
        for (TeamFound teamFound : records) {
            teamFound.getOrder().setOrderGoods(orderGoodsListMap.get(teamFound.getOrderId()));
        }
    }

    @Override
    public void schedule() {
        List<Object> fList = redisUtil.lGet("team_found", 0, -1);
        Long now = System.currentTimeMillis() / 1000;
        Set<Integer> timeTeamFoundIds = new HashSet<>();//到时的活动
        for (Object o : fList) {
            TeamFound teamFound = (TeamFound) o;
            if (now - teamFound.getFoundTime() > teamFound.getTimeLimit()) {
                //时间到了
                timeTeamFoundIds.add(teamFound.getFoundId());
                redisUtil.lRemove("team_found", 1, o);
            }
        }
        if (timeTeamFoundIds.size() > 0) {
            Set<Integer> updateTeamFoundIds = new HashSet<>();//要更新的团
            List<TeamFound> teamFounds = list(new QueryWrapper<TeamFound>().in("found_id", timeTeamFoundIds));
            for (TeamFound teamFound : teamFounds) {
                if (teamFound.getJoin() < teamFound.getNeed()) {
                    updateTeamFoundIds.add(teamFound.getFoundId());//人数没齐
                }
            }
            if (updateTeamFoundIds.size() > 0) {
                update(new UpdateWrapper<TeamFound>().set("status", 3).in("found_id", updateTeamFoundIds));//成团失败
                teamFollowService.update((new UpdateWrapper<TeamFollow>()).set("status", 3)
                        .in("found_id", updateTeamFoundIds).eq("status", 1)); //更新团员成团失败
            }
        }
    }

    @Override
    public void withTeamFollowOrderGoods(List<TeamFound> records) {
        if (records.size() == 0) {
            return;
        }
        Set<Integer> teamFollowOrderIds = new HashSet<>();
        for (TeamFound teamFound : records) {
            for (TeamFollow teamFollow : teamFound.getTeamFollowList()) {
                teamFollowOrderIds.add(teamFollow.getOrderId());
            }
        }
        if (!teamFollowOrderIds.isEmpty()) {
            Map<Integer, List<OrderGoods>> orderGoodsListMap = orderService.getOrderGoodsListByOrderIds(teamFollowOrderIds).stream().collect(Collectors.groupingBy(OrderGoods::getOrderId));
            for (TeamFound teamFound : records) {
                for (TeamFollow teamFollow : teamFound.getTeamFollowList()) {
                    teamFollow.getOrder().setOrderGoods(orderGoodsListMap.get(teamFollow.getOrderId()));
                }
            }
        }
    }

    @Override
    public String refund(Integer sellerId, TeamFound teamFound) {
        if (null == teamFound) {
            throw new ShopException(ResultEnum.NO_FIND_FOUND);
        }
        if (teamFound.getStatus() != 3) {
            throw new ShopException(ResultEnum.FOUND_STATUS_IS_NO_ABLE);
        }
        List<TeamFollow> teamFollowList = teamFollowService.list((new QueryWrapper<TeamFollow>())
                .eq("found_id", teamFound.getFoundId()).eq("status", 3));//成团失败的
        Set<Integer> orderId = teamFollowList.stream().map(TeamFollow::getOrderId).collect(Collectors.toSet());
        return orderService.cancelTeamOrder(orderId);
    }

    @PostConstruct
    private void initTeamFoundIsOn() {
        redisUtil.del("team_found");
        Class<?> cls = TeamFound.class;
        String tableName = cls.getAnnotation(TableName.class).value();
        int count = preSellMapper.hashTable(dataBaseName, tableName);
        log.info("表名：{}", tableName);
        if (count > 0) {
            List<TeamFound> isOnTeamFounds = list((new QueryWrapper<TeamFound>()).eq("status", 1));
            if (isOnTeamFounds.size() > 0) {
                Set<Integer> teamIds = isOnTeamFounds.stream().map(TeamFound::getTeamId).collect(Collectors.toSet());
                List<TeamActivity> teamActivityList = teamActivityService.list((new QueryWrapper<TeamActivity>()).in("team_id", teamIds));
                Map<Integer, TeamActivity> teamActivityMap = teamActivityList.stream().collect(Collectors.toMap(TeamActivity::getTeamId, team -> team));
                for (TeamFound teamFound : isOnTeamFounds) {
                    teamFound.setTimeLimit(teamActivityMap.get(teamFound.getTeamId()).getTimeLimit());
                    redisUtil.lSet("team_found", teamFound);
                }
            }
            log.info("redis放入" + isOnTeamFounds.size() + "条正在拼团的拼主记录");
        }
    }
}
