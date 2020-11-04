package com.soubao.service.impl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.common.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.soubao.common.constant.OrderConstant;
import com.soubao.entity.Order;
import com.soubao.entity.TeamFollow;
import com.soubao.service.TeamFollowService;
import com.soubao.dao.PreSellMapper;
import com.soubao.dao.TeamFollowMapper;
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
 * 参团表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-10-30
 */
@Slf4j
@Service
public class TeamFollowServiceImpl extends ServiceImpl<TeamFollowMapper, TeamFollow> implements TeamFollowService {
    @Autowired
    private RedisUtil redisUtil;
    @Value("${spring.datasource.name}")
    private String dataBaseName;
    @Autowired
    private PreSellMapper preSellMapper;
    @Override
    public void withTeamFollow(List<Order> records) {
        Set<Integer> orderIds = records.stream().map(Order::getOrderId).collect(Collectors.toSet());
        if(orderIds.size() > 0){
            List<TeamFollow> teamFollows = list((new QueryWrapper<TeamFollow>()).in("order_id", orderIds));
            Map<Integer, TeamFollow> teamFollowMap = teamFollows.stream().collect(Collectors.toMap(TeamFollow::getOrderId, follow -> follow));
            for(Order order : records){
                order.setTeamFollow(teamFollowMap.getOrDefault(order.getOrderId(), new TeamFollow()));
            }
        }
    }

    @PostConstruct
    private void initTeamFollowNoPay(){
        redisUtil.del("team_follow");
        Class<?> cls = TeamFollow.class;
        String tableName = cls.getAnnotation(TableName.class).value();
        int count = preSellMapper.hashTable(dataBaseName,tableName);
        log.info("表名：{}", tableName);
        if (count > 0) {
            List<TeamFollow> noPayTeamFollows = list((new QueryWrapper<TeamFollow>()).eq("status", 0));
            if(noPayTeamFollows.size() > 0){
                for(TeamFollow teamFollow : noPayTeamFollows){
                    redisUtil.lSet("team_follow", teamFollow);
                }
            }
            log.info("redis放入" + noPayTeamFollows.size() + "条拼团没有支付的团员记录");
        }
    }
}
