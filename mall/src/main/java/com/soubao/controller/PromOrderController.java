package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.PromOrder;
import com.soubao.service.PromOrderService;
import com.soubao.common.utils.TimeUtil;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("prom_order")
@Api(value = "订单优惠接口", tags = {"订单优惠相关接口"})
@RestController
public class PromOrderController {
    @Autowired
    private PromOrderService promOrderService;

    @ApiOperation(value = "订单促销分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "store_id", value = "店铺id", paramType = "query"),
            @ApiImplicitParam(name = "start_time", value = "起始时间", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "截止时间", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "活动状态：空-所有，0-管理员关闭，1-正常，2-已过期", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "活动名称", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页码", defaultValue = "1", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", defaultValue = "10", paramType = "query")
    })
    @GetMapping("page")
    public IPage<PromOrder> promOrderPage(
            @RequestParam(value = "start_time", required = false) String startTime,
            @RequestParam(value = "end_time", required = false) String endTime,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<PromOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted", 0);
        if (startTime != null) {
            wrapper.ge("start_time", startTime);
        }
        if (endTime != null) {
            wrapper.lt("end_time", endTime);
        }
        if (status != null) {
            long currentTime = System.currentTimeMillis() / 1000;
            if (status == 0){
                wrapper.eq("status", status);
            }else if (status == 1){
                wrapper.eq("status", status);
                wrapper.ge("end_time", currentTime);
            }else if (status == 2){
                wrapper.eq("status", 1);
                wrapper.le("end_time", currentTime);
            }
        }
        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
        }
        wrapper.orderByDesc("id");
        return promOrderService.page(new Page<>(page, size), wrapper);
    }

    @ApiOperation(value = "订单促销列表")
    @GetMapping("list")
    public List<PromOrder> promOrders(@ApiParam("店铺主键") @RequestParam(value = "store_id", required = false) Integer storeId) {
        QueryWrapper<PromOrder> promOrderQueryWrapper = new QueryWrapper<>();
        if (storeId != null) {
            promOrderQueryWrapper.eq("store_id", storeId);
        }
        Long now = System.currentTimeMillis() / 1000;
        promOrderQueryWrapper.lt("start_time", now);
        promOrderQueryWrapper.gt("end_time", now);
        promOrderQueryWrapper.eq("status", 1);
        promOrderQueryWrapper.eq("is_deleted", 1);
        return promOrderService.list(promOrderQueryWrapper);
    }

    @ApiOperation(value = "更新活动是否推荐")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "recommend", value = "是否推荐：1是，0否", required = true, paramType = "query")
    })
    @PutMapping("recommend")
    public SBApi recommend(@RequestBody PromOrder promOrder, SBApi sbApi) {
        promOrderService.update(new UpdateWrapper<PromOrder>().set("recommend", promOrder.getRecommend()).eq("id", promOrder.getId()));
        return sbApi;
    }

    @ApiOperation(value = "删除活动")
    @ApiImplicitParam(name = "prom_id", value = "活动id", required = true, paramType = "path")
    @DeleteMapping("{prom_id}")
    public SBApi remove(@PathVariable("prom_id") Integer promId, SBApi sbApi) {
        promOrderService.update((new UpdateWrapper<PromOrder>()).set("is_deleted", 1).eq("id", promId));
        return sbApi;
    }

    @ApiOperation(value = "关闭活动")
    @ApiImplicitParam(name = "prom_id", value = "活动id", required = true, paramType = "path")
    @PutMapping("close_prom/{prom_id}")
    public SBApi close(@PathVariable("prom_id") Integer promId, SBApi sbApi) {
        promOrderService.closeProm(promId);
        return sbApi;
    }

    @ApiOperation(value = "获取活动信息")
    @ApiImplicitParam(name = "prom_id", value = "活动id", required = true, paramType = "path")
    @GetMapping("{prom_id}")
    public PromOrder getPromOrder(@PathVariable(value = "prom_id") Integer promId) {
        return promOrderService.getById(promId);
    }

    @ApiOperation(value = "修改活动信息")
    @PutMapping
    public SBApi updatePromOrder(@Valid @RequestBody PromOrder promOrder, SBApi sbApi) {
        promOrderService.updateById(promOrder);
        return sbApi;
    }

    @ApiOperation(value = "添加活动")
    @PostMapping
    public SBApi addPromOrder(@Valid @RequestBody PromOrder promOrder, SBApi sbApi) {
        promOrder.setStartTime(TimeUtil.transForSecond(promOrder.getStartTimeShow(), "yyyy-MM-dd"));
        promOrder.setEndTime(TimeUtil.transForSecond(promOrder.getEndTimeShow(), "yyyy-MM-dd"));
        promOrderService.save(promOrder);
        return sbApi;
    }

}
