package com.soubao.service;

import com.soubao.entity.DistributLevel;
import com.soubao.entity.Order;
import com.soubao.vo.UserOrderStatistics;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * 服务类
 *
 * @author dyr
 * @since 2019-08-15
 */
@FeignClient("order")
public interface OrderService {

    @GetMapping("/order_list")
    List<Order> getOrderListByIds(@RequestParam(value = "order_ids") Set<Integer> orderIds);

    @GetMapping("distribut/level")
    DistributLevel getDistributLevel(@RequestParam(value = "level_id") Integer levelId);

    @GetMapping("distribut/level/list")
    List<DistributLevel> distributLevelList();

    @GetMapping("rebate_log/lowers")
    String lowerUsers(
            @RequestParam(value = "user_id") Integer userId,
            @RequestParam(value = "level", defaultValue = "2") Integer level,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size);

    @GetMapping("/users/statistic")
    List<UserOrderStatistics> userOrderStatisticsList(
            @RequestParam(value = "user_ids") Set<Integer> userIds,
            @RequestParam(value = "start_time") Long startTime,
            @RequestParam(value = "end_time") Long endTime
    );

}
