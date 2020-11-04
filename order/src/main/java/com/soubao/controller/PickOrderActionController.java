package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.OrderAction;
import com.soubao.entity.PickOrderAction;
import com.soubao.entity.Seller;
import com.soubao.entity.User;
import com.soubao.service.PickOrderActionService;
import com.soubao.service.SellerService;
import com.soubao.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 提货订单操作记录
 */
@RestController
@Slf4j
@RequestMapping("pickOrderAction")
public class PickOrderActionController {

    @Autowired
    private PickOrderActionService pickOrderActionService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private UserService userService;

    /**
     * 提货订单操作记录
     *
     * @param prickOrderId
     * @param startTime
     * @param endTime
     * @param actionUser
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("提货订单操作记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "prick_order_id", value = "提货订单id", dataType = "Integer", paramType = "Integer"),
            @ApiImplicitParam(name = "start_time", value = "开始时间", dataType = "Long", paramType = "Long"),
            @ApiImplicitParam(name = "end_time", value = "结束时间", dataType = "Long", paramType = "Long"),
            @ApiImplicitParam(name = "action_user", value = "操作人", dataType = "Integer", paramType = "Integer")})
    public IPage<PickOrderAction> orderActionPage(
            @ApiParam("提货订单id") @RequestParam(value = "prick_order_id", required = false) Integer prickOrderId,
            @ApiParam("开始时间") @RequestParam(value = "start_time", required = false) Long startTime,
            @ApiParam("结束时间") @RequestParam(value = "end_time", required = false) Long endTime,
            @ApiParam("操作人") @RequestParam(value = "action_user", required = false) Integer actionUser,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<PickOrderAction> actionQueryWrapper = new QueryWrapper<>();
        if (null != startTime && null != endTime) {
            actionQueryWrapper.between("log_time", startTime, endTime);
        }
        if (null != actionUser) {
            actionQueryWrapper.eq("action_user", actionUser);
        }
        if (null != prickOrderId) {
            actionQueryWrapper.eq("prick_order_id", prickOrderId);
        }
        IPage<PickOrderAction> pickOrderActionPage = pickOrderActionService.page(new Page<>(page, size), actionQueryWrapper);
        withUser(pickOrderActionPage.getRecords());
        return pickOrderActionPage;
    }

    public void withUser(List<PickOrderAction> records) {
        Set<Integer> sellerIds = new HashSet<>();
        Set<Integer> userIds = new HashSet<>();
        for (PickOrderAction poa : records) {
            Integer userType = poa.getUserType();
            if (userType == 1) {
                sellerIds.add(poa.getActionUser());
            } else if (userType == 2) {
                userIds.add(poa.getActionUser());
            }
        }
        List<Seller> sellers;
        List<User> users;
        Map<Integer, Seller> sellerMap = new HashMap<>();
        Map<Integer, User> userMap = new HashMap<>();
        if (!sellerIds.isEmpty()) {
            sellers = sellerService.sellers(sellerIds);
            sellerMap = sellers.stream().collect(Collectors.toMap(Seller::getSellerId, seller -> seller));
        }
        if (!userIds.isEmpty()) {
            users = userService.usersByIds(userIds);
            userMap = users.stream().collect(Collectors.toMap(User::getUserId, user -> user));
        }
        for (PickOrderAction poa : records) {
            Integer userType = poa.getUserType();
            if (userType == 1) {
                if (sellerMap.containsKey(poa.getActionUser())) {
                    poa.setActionUserDesc("商家(" + sellerMap.get(poa.getActionUser()).getSellerName() + ")");
                }
            } else if (userType == 2) {
                if (userMap.containsKey(poa.getActionUser())) {
                    poa.setActionUserDesc("用户(" + userMap.get(poa.getActionUser()).getNickname() + ")");
                }
            } else {
                poa.setActionUserDesc("平台管理员");
            }
        }
    }
}
