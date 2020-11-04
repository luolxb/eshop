package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.GroupBuy;
import com.soubao.entity.GroupBuyGoodsItem;
import com.soubao.service.GroupBuyGoodsItemService;
import com.soubao.service.GroupBuyService;
import com.soubao.validation.group.Insert;
import com.soubao.vo.GroupBuyGoodsVo;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("group_buy")
@RestController
@Api(
        value = "团购控制器",
        tags = {"团购相关接口"})
public class GroupBuyController {
    @Autowired
    private GroupBuyService groupBuyService;
    @Autowired
    private GroupBuyGoodsItemService groupBuyGoodsItemService;

    @ApiOperation(value = "团购活动列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "store_id", value = "店铺id", paramType = "query"),
            @ApiImplicitParam(name = "start_time", value = "起始时间", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "截止时间", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "活动状态", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "活动名称", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页码", defaultValue = "1", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", defaultValue = "10", paramType = "query")
    })
    @GetMapping("page")
    public IPage<GroupBuy> page(
            @RequestParam(value = "store_id", required = false) Integer storeId,
            @RequestParam(value = "start_time", required = false) Long startTime,
            @RequestParam(value = "end_time", required = false) Long endTime,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<GroupBuy> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted", 0);
        if (storeId != null) {
            wrapper.eq("store_id", storeId);
        }
        if (startTime != null) {
            wrapper.ge("start_time", startTime);
        }
        if (endTime != null) {
            wrapper.lt("end_time", endTime);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
        }
        wrapper.orderByDesc("id");
        return groupBuyService.page(new Page<>(page, size), wrapper);
    }

    @ApiOperation(value = "更新活动是否推荐")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动id", required = true, paramType = "query"),
            @ApiImplicitParam(
                    name = "recommend",
                    value = "是否推荐：1是，0否",
                    required = true,
                    paramType = "query")
    })
    @PutMapping("recommend")
    public SBApi recommend(@RequestBody GroupBuy groupBuy, SBApi sbApi) {
        groupBuyService.update(
                new UpdateWrapper<GroupBuy>()
                        .set("recommend", groupBuy.getRecommend())
                        .eq("id", groupBuy.getId()));
        return sbApi;
    }

    @ApiOperation(value = "活动审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动id", required = true, paramType = "query"),
            @ApiImplicitParam(
                    name = "status",
                    value = "审核状态：1通过，2拒绝",
                    required = true,
                    paramType = "query")
    })
    @PutMapping("status")
    public SBApi status(@RequestBody GroupBuy groupBuy, SBApi sbApi) {
        groupBuyService.updateGroupBuyStatus(groupBuy);
        return sbApi;
    }

    @ApiOperation(value = "删除活动")
    @ApiImplicitParam(name = "prom_id", value = "活动id", required = true, paramType = "path")
    @DeleteMapping("{prom_id}")
    public SBApi remove(@PathVariable("prom_id") Integer promId, SBApi sbApi) {
        groupBuyService.removeGroupBuy(promId);
        return sbApi;
    }

    @ApiOperation(value = "关闭活动")
    @ApiImplicitParam(name = "prom_id", value = "活动id", required = true, paramType = "path")
    @PutMapping("close_prom/{prom_id}")
    public SBApi close(@PathVariable("prom_id") Integer promId, SBApi sbApi) {
        groupBuyService.closeProm(promId);
        return sbApi;
    }

    @ApiOperation(value = "获取活动")
    @GetMapping("/{id}")
    public GroupBuy getGroupBuy(@PathVariable(value = "id") Integer groupbuyId) {
        GroupBuy groupBuy = groupBuyService.getById(groupbuyId);
        List<GroupBuyGoodsItem> groupBuyGoodsItems =
                groupBuyGoodsItemService.list(
                        new QueryWrapper<GroupBuyGoodsItem>().eq("group_buy_id", groupBuy.getId()));
        groupBuyGoodsItemService.withGoodsSku(groupBuyGoodsItems);
        groupBuy.setGroupBuyGoodsItem(groupBuyGoodsItems);
        return groupBuy;
    }

    @PostMapping
    public SBApi addGroupBuy(@Valid @RequestBody GroupBuy groupBuy) {
        groupBuyService.saveGroupBuy(groupBuy);
        return new SBApi();
    }

    @PutMapping
    public SBApi updateGroupBuy(@Valid @RequestBody GroupBuy groupBuy) {
        groupBuyService.updateGroupBuy(groupBuy);
        return new SBApi();
    }

    @ApiOperation(value = "活动商品列表分页")
    @GetMapping("/goods/page")
    public IPage<GroupBuyGoodsVo> goodsPage(
            @ApiParam("商品分类id") @RequestParam(value = "cat_id", required = false) Integer catId,
            @ApiParam("活动标题") @RequestParam(value = "title", required = false) String title,
            @ApiParam("排序字段") @RequestParam(value = "order_by", required = false) String orderBy,
            @ApiParam("排序方式") @RequestParam(value = "sort", defaultValue = "false") Boolean isAsc,
            @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
            @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<GroupBuy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("b.status", 1).eq("b.is_end", 0).eq("g.is_on_sale", 1)
                .eq("b.recommend", 1);
        queryWrapper.apply("b.start_time <= unix_timestamp(now()) AND b.end_time >= unix_timestamp(now())");
        queryWrapper.groupBy("i.group_buy_id");
        String orderByStr;
        if("start_time".equals(orderBy)){
            orderByStr = "b.start_time";
        }else if("comment_count".equals(orderBy)){
            orderByStr = "g.comment_count";
        }else{
            orderByStr = "b.id";
        }
        if(isAsc){
            queryWrapper.orderByAsc(orderByStr);
        }else{
            queryWrapper.orderByDesc(orderByStr);
        }
        if(null != catId){
            queryWrapper.and(wrapper -> wrapper.eq("g.cat_id1", catId).or().eq("g.cat_id2", catId).or().eq("g.cat_id3", catId));
        }
        if(StringUtils.isNotEmpty(title)){
            queryWrapper.like("title", title);
        }
        return groupBuyService.getGroupBuyGoodsPage(new Page<>(page, size),queryWrapper);
    }
}