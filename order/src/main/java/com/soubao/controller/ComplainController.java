package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Complain;
import com.soubao.entity.Order;
import com.soubao.entity.OrderGoods;
import com.soubao.service.ComplainService;
import com.soubao.service.MallService;
import com.soubao.service.OrderGoodsService;
import com.soubao.service.OrderService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

/**
 * 投诉表 前端控制器
 *
 * @author dyr
 * @since 2020-02-28
 */
@RestController
@RequestMapping("complain")
public class ComplainController {
    @Autowired
    private ComplainService complainService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MallService mallService;

    @GetMapping("page")
    public IPage<Complain> getComplains(
            @RequestParam(value = "start_time", required = false) Long startTime,
            @RequestParam(value = "end_time", required = false) Long endTime,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "store_name", required = false) String storeName,
            @RequestParam(value = "complain_id", required = false) String complainId,
            @RequestParam(value = "complain_subject_name", required = false)
                    String complainSubjectName,
            @RequestParam(value = "p", defaultValue = "1") Integer p,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Complain> queryWrapper = new QueryWrapper<>();
        if (startTime != null && endTime != null) {
            queryWrapper.between("complain_time", startTime, endTime);
        }
        if (state != null) {
            queryWrapper.eq("complain_state", state);
        }
        if (complainId != null) {
            queryWrapper.eq("complain_id", complainId);
        }
        if (!StringUtils.isEmpty(username)) {
            queryWrapper.like("user_name", username);
        }
        if (!StringUtils.isEmpty(storeName)) {
            queryWrapper.like("store_name", storeName);
        }
        if (!StringUtils.isEmpty(complainSubjectName)) {
            queryWrapper.like("complain_subject_name", complainSubjectName);
        }
        return complainService.getComplainPage(new Page<>(p, size), queryWrapper);
    }

    @GetMapping
    public Complain getComplain(@RequestParam(value = "complain_id") Integer complainId) {
        Complain complain = complainService.getById(complainId);
        OrderGoods orderGoods = orderGoodsService.getById(complain.getOrderGoodsId());
        Order order = orderService.getById(complain.getOrderId());
        Set<Integer> regionIds = new HashSet<>();
        regionIds.add(order.getProvince());
        regionIds.add(order.getCity());
        regionIds.add(order.getDistrict());
        regionIds.add(order.getTwon());
        order.setRegions(mallService.regions(regionIds));
        complain.setOrder(order);
        complain.setOrderGoods(orderGoods);
        return complain;
    }

    @PutMapping
    public SBApi updateComplain(@RequestBody Complain complain) {
        complainService.updateById(complain);
        return new SBApi();
    }

    @GetMapping("count")
    public Integer getCount(@RequestParam(value = "complain_state", required = false) Integer status) {
        QueryWrapper<Complain> wrapper = new QueryWrapper<>();
        if(status != null){
            wrapper.in("complain_state", status);
        }
        return complainService.count(wrapper);
    }
}
