package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Order;
import com.soubao.entity.OrderAction;
import com.soubao.service.OrderActionService;
import com.soubao.common.vo.SBApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping(value = "/order_action")
public class OrderActionController {
    @Autowired
    private OrderActionService orderActionService;

    @GetMapping("{order_id}")
    public List<OrderAction> orderAction(@PathVariable("order_id") Integer orderId) {
        List<OrderAction> orderActions = orderActionService.list(new QueryWrapper<OrderAction>()
                .eq("order_id", orderId).orderByDesc("log_time"));
        orderActionService.withUser(orderActions);
        return orderActions;
    }

    @GetMapping("/page")
    public IPage<OrderAction> orderActionPage(
            @RequestParam(value = "start_time", required = false)Long startTime,
            @RequestParam(value = "end_time", required = false)Long endTime,
            @RequestParam(value = "action_user", required = false)Integer actionUser,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "order_by", defaultValue = "action_id") String orderBy,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<OrderAction> actionQueryWrapper = new QueryWrapper<>();
        actionQueryWrapper.orderByDesc(orderBy);
        if(null != startTime && null != endTime){
            actionQueryWrapper.between("log_time", startTime, endTime);
        }
        if (null != actionUser && actionUser > 0) {
            actionQueryWrapper.eq("action_user", actionUser);
        }
        IPage<OrderAction> orderActionIPage = orderActionService.page(new Page<>(page, size), actionQueryWrapper);
        orderActionService.withUser(orderActionIPage.getRecords());
        return orderActionIPage;
    }

    @DeleteMapping
    public SBApi delete(@RequestParam("ids") Set<Integer> ids) {
        orderActionService.removeByIds(ids);
        return new SBApi();
    }

}
