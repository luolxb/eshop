package com.soubao.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.*;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.utils.RedisUtil;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api(value = "拼团接口", tags = {"拼团相关接口"})
@RequestMapping("/team")
@RestController
@Slf4j
public class TeamController {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CartService cartService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private TeamActivityService teamActivityService;
    @Autowired
    private TeamFoundService teamFoundService;
    @Autowired
    private TeamFollowService teamFollowService;
    @Autowired
    private TeamGoodsItemService teamGoodsItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @ApiOperation("拼团活动分页")
    @GetMapping("/activity/page")
    public IPage<TeamActivity> getPage(
            @ApiParam("拼团活动类型,0分享团1佣金团2抽奖团") @RequestParam(value = "team_type", required = false) Integer teamType,
            @ApiParam("活动标题") @RequestParam(value = "act_name", required = false) String actName,
            @ApiParam("活动状态") @RequestParam(value = "status", required = false) Integer status,
            @ApiParam("店铺id") @RequestParam(value = "store_id", required = false) Integer storeId,
            @ApiParam("页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        TeamActivity teamActivityQuery = new TeamActivity();
        if (storeId != null) {
            teamActivityQuery.setStoreId(storeId);
        }
        if (!StringUtils.isEmpty(actName)) {
            teamActivityQuery.setActName("%" + actName + "%");
        }
        if (teamType != null) {
            teamActivityQuery.setTeamType(teamType);
        }
        if (status != null) {
            teamActivityQuery.setStatus(status);
        }
        IPage<TeamActivity> teamActivityIPage = teamActivityService.selectPageWithStore(new Page<>(page, size), teamActivityQuery);
        teamActivityService.withStore(teamActivityIPage.getRecords());
        return teamActivityIPage;
    }

    @ApiOperation("拼团活动")
    @GetMapping("activity/{team_id}")
    public TeamActivity getTeamActivity(@PathVariable(value = "team_id") Integer teamId) {
        TeamActivity teamActivity = teamActivityService.getById(teamId);
        List<TeamGoodsItem> teamGoodsItems = teamGoodsItemService.list((new QueryWrapper<TeamGoodsItem>()).eq("team_id", teamId));
        teamActivity.setStore(sellerService.getStoreById(teamActivity.getStoreId()));
        teamGoodsItemService.withGoodsSku(teamGoodsItems);
        teamActivity.setTeamGoodsItem(teamGoodsItems);
        return teamActivity;
    }

    @ApiOperation("拼团活动")
    @GetMapping("/activity")
    public TeamActivity getOne(@RequestParam(value = "team_id") Integer teamId) {
        return teamActivityService.getById(teamId);
    }

    @PostMapping("activity")
    public SBApi addTeamActivity(@Valid @RequestBody TeamActivity teamActivity, SBApi sbApi) {
        teamActivity.setAddTime(System.currentTimeMillis() / 1000);
        teamActivityService.save(teamActivity);
        teamGoodsItemService.saveByTeam(teamActivity.getTeamGoodsItem(), teamActivity);
        return sbApi;
    }

    @PutMapping("activity")
    public SBApi updateTeamActivity(@Valid @RequestBody TeamActivity teamActivity, SBApi sbApi) {
        teamActivity.setStatus(0);
        teamActivityService.updateById(teamActivity);
        teamGoodsItemService.remove((new QueryWrapper<TeamGoodsItem>()).eq("team_id", teamActivity.getTeamId()));
        teamActivityService.restoreGoodsSku(teamActivity);
        teamGoodsItemService.saveByTeam(teamActivity.getTeamGoodsItem(), teamActivity);
        return sbApi;
    }

    @PutMapping("activity/status")
    public SBApi status(@RequestBody TeamActivity teamActivity, SBApi sbApi) {
        teamActivityService.updateById(teamActivity);
        if (teamActivity.getStatus() == 3) {
            teamActivityService.restoreGoodsSku(teamActivity);
        }
        return sbApi;
    }

    @ApiOperation("删除拼团活动")
    @DeleteMapping("/activity/{team_id}")
    public SBApi deleteTeamActivity(@PathVariable(value = "team_id") Integer teamId) {
        List<Order> orders = orderService.teamOrderList(teamId);
        if (orders.size() > 0) {
            throw new ShopException(ResultEnum.TEAM_HAVE_FOUND_ORDER);
        }
        TeamActivity teamActivity = teamActivityService.getById(teamId);
        teamActivity.setDeleted(1);
        teamActivityService.updateById(teamActivity);
        teamGoodsItemService.update((new UpdateWrapper<TeamGoodsItem>()).set("deleted", 1).eq("team_id", teamId));
        teamActivityService.restoreGoodsSku(teamActivity);
        return new SBApi();
    }

    @ApiOperation("拼团活动商品列表分页")
    @GetMapping("/goods/page")
    public IPage<TeamActivity> getTeamActivityGoods(@ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
                                                    @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "6") Integer size) {
        TeamActivity teamActivity = new TeamActivity();
        teamActivity.setDeleted(0);
        teamActivity.setStatus(1);
        return teamActivityService.getGoodsPage(new Page<>(page, size), teamActivity);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/found/order/page")
    public IPage<TeamFound> getTeamFoundOrderPage(
            @ApiParam("活动id") @RequestParam(value = "team_id", required = false) Integer teamId,
            @ApiParam("订单编号") @RequestParam(value = "order_sn", required = false) String orderSn,
            @ApiParam("开团id") @RequestParam(value = "found_id", required = false) Integer foundId,
            @ApiParam("开团时间起始") @RequestParam(value = "found_time_start", required = false) Long foundTimeStart,
            @ApiParam("开团时间截止") @RequestParam(value = "found_time_end", required = false) Long foundTimeEnd,
            @ApiParam("店铺名称") @RequestParam(value = "store_name", required = false) String storeName,
            @ApiParam("0:待开团;1:待成团;2:已成团;3未成团") @RequestParam(value = "status", required = false) Integer status,
            @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
            @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "6") Integer size) {
        QueryWrapper<TeamFound> teamFoundQueryWrapper = new QueryWrapper<>();
        if (status != null) {
            teamFoundQueryWrapper.eq("status", status);
        }
        if (teamId != null) {
            teamFoundQueryWrapper.eq("team_id", teamId);
        }
        if (foundId != null) {
            teamFoundQueryWrapper.eq("found_id", foundId);
        }
        if (foundTimeStart != null && foundTimeEnd != null) {
            teamFoundQueryWrapper.between("found_time", foundTimeStart, foundTimeEnd);
        }
        if (StringUtils.isNotEmpty(storeName)) {
            List<Store> storeList = sellerService.getStoreListByName(storeName);
            Set<Integer> storeIds = storeList.stream().map(Store::getStoreId).collect(Collectors.toSet());
            if (storeIds.size() > 0) {
                teamFoundQueryWrapper.in("store_id", storeIds);
            }
        }
        if (StringUtils.isNotEmpty(orderSn)) {
            teamFoundQueryWrapper.eq("order_sn", orderSn);
        }
        teamFoundQueryWrapper.orderByDesc("found_id");
        IPage<TeamFound> teamFoundIPage = teamFoundService.page(new Page<>(page, size), teamFoundQueryWrapper);
        teamFoundService.withStore(teamFoundIPage.getRecords());
        teamFoundService.withOrder(teamFoundIPage.getRecords());
        teamFoundService.withOrderGoods(teamFoundIPage.getRecords());
        teamFoundService.withTeamActivity(teamFoundIPage.getRecords());
        teamFoundService.withTeamFollow(teamFoundIPage.getRecords());
        teamFoundService.withTeamFollowOrder(teamFoundIPage.getRecords());
        teamFoundService.withTeamFollowOrderGoods(teamFoundIPage.getRecords());
        return teamFoundIPage;
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("/found/order/page/seller")
    public IPage<TeamFound> getTeamFoundOrderPage(
            @ApiParam("活动id") @RequestParam(value = "team_id", required = false) Integer teamId,
            @ApiParam("订单编号") @RequestParam(value = "order_sn", required = false) String orderSn,
            @ApiParam("开团id") @RequestParam(value = "found_id", required = false) Integer foundId,
            @ApiParam("开团时间起始") @RequestParam(value = "found_time_start", required = false) Long foundTimeStart,
            @ApiParam("开团时间截止") @RequestParam(value = "found_time_end", required = false) Long foundTimeEnd,
            @ApiParam("0:待开团;1:待成团;2:已成团;3未成团") @RequestParam(value = "status", required = false) Integer status,
            @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
            @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "6") Integer size) {
        QueryWrapper<TeamFound> teamFoundQueryWrapper = new QueryWrapper<>();
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        teamFoundQueryWrapper.eq("store_id", seller.getStoreId());
        if (status != null) {
            teamFoundQueryWrapper.eq("status", status);
        }
        if (teamId != null) {
            teamFoundQueryWrapper.eq("team_id", teamId);
        }
        if (foundId != null) {
            teamFoundQueryWrapper.eq("found_id", foundId);
        }
        if (foundTimeStart != null && foundTimeEnd != null) {
            teamFoundQueryWrapper.between("found_time", foundTimeStart, foundTimeEnd);
        }
        if (StringUtils.isNotEmpty(orderSn)) {
            teamFoundQueryWrapper.eq("order_sn", orderSn);
        }
        teamFoundQueryWrapper.orderByDesc("found_id");
        IPage<TeamFound> teamFoundIPage = teamFoundService.page(new Page<>(page, size), teamFoundQueryWrapper);
        teamFoundService.withSellerOrder(teamFoundIPage.getRecords());
        teamFoundService.withOrderGoods(teamFoundIPage.getRecords());
        teamFoundService.withTeamActivity(teamFoundIPage.getRecords());
        teamFoundService.withTeamFollow(teamFoundIPage.getRecords());
        teamFoundService.withTeamFollowSellerOrder(teamFoundIPage.getRecords());
        teamFoundService.withTeamFollowOrderGoods(teamFoundIPage.getRecords());
        return teamFoundIPage;
    }

    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @GetMapping("order/page")
    public Page<Order> getTeamFoundOrderPage(
            @ApiParam("收货人") @RequestParam(value = "consignee", required = false) String consignee,
            @ApiParam("店铺名称") @RequestParam(value = "store_name", required = false) String storeName,
            @ApiParam("活动id") @RequestParam(value = "team_id", required = false) Integer teamId,
            @ApiParam("开团id") @RequestParam(value = "found_id", required = false) Integer foundId,
            @ApiParam("订单状态") @RequestParam(value = "order_status", required = false) Integer orderStatus,
            @ApiParam("支付状态") @RequestParam(value = "pay_status", required = false) Integer payStatus,
            @ApiParam("支付方式") @RequestParam(value = "pay_code", required = false) String payCode,
            @ApiParam("发货状态") @RequestParam(value = "shipping_status", required = false) Integer shippingStatus,
            @ApiParam("下单时间起始") @RequestParam(value = "add_time_start", required = false) Long addTimeStart,
            @ApiParam("下单时间截止") @RequestParam(value = "add_time_end", required = false) Long addTimeEnd,
            @ApiParam("店铺主键组") @RequestParam(value = "store_id", required = false) Set<Integer> storeId,
            @ApiParam("订单编号") @RequestParam(value = "order_sn", required = false) String orderSn,
            @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
            @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "15") Integer size) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("prom_type", 6);
        map.put("order_sn", orderSn);
        map.put("store_name", storeName);
        map.put("add_time_start", addTimeStart);
        map.put("add_time_end", addTimeEnd);
        map.put("store_id", storeId);
        map.put("order_status", orderStatus);
        map.put("pay_status", payStatus);
        map.put("pay_code", payCode);
        map.put("shipping_status", shippingStatus);
        map.put("consignee", consignee);
        Set<Integer> orderIds = new HashSet<>();
        if (teamId != null) {
            List<TeamFollow> teamFollows = teamFollowService.list((new QueryWrapper<TeamFollow>()).select("order_id").eq("team_id", teamId));
            List<TeamFound> teamFounds = teamFoundService.list((new QueryWrapper<TeamFound>()).select("order_id").eq("team_id", teamId));
            orderIds.addAll(teamFollows.stream().map(TeamFollow::getOrderId).collect(Collectors.toSet()));
            orderIds.addAll(teamFounds.stream().map(TeamFound::getOrderId).collect(Collectors.toSet()));
        }
        if (foundId != null) {
            List<TeamFollow> teamFollows = teamFollowService.list((new QueryWrapper<TeamFollow>()).select("order_id").eq("found_id", foundId));
            List<TeamFound> teamFounds = teamFoundService.list((new QueryWrapper<TeamFound>()).select("order_id").eq("found_id", foundId));
            orderIds.addAll(teamFollows.stream().map(TeamFollow::getOrderId).collect(Collectors.toSet()));
            orderIds.addAll(teamFounds.stream().map(TeamFound::getOrderId).collect(Collectors.toSet()));
        }
        Page<Order> orderIPage = new Page<>();
        if ((teamId != null || foundId != null) && orderIds.size() == 0) {
            return orderIPage;
        } else {
            map.put("order_id", orderIds);
        }
        orderIPage = orderService.getTeamOrderPage(map, page, size);
        teamActivityService.withTeamActivity(orderIPage.getRecords());
        teamFollowService.withTeamFollow(orderIPage.getRecords());
        teamFoundService.withTeamFound(orderIPage.getRecords());
        regionService.inOrder(orderIPage.getRecords());
        return orderIPage;
    }

    @ApiOperation("拼主分页")
    @GetMapping("found/page")
    public Page<TeamFound> getTeamFoundPage(
            @ApiParam("拼团id") @RequestParam(value = "team_id") Integer teamId,
            @ApiParam("0:待开团(表示已下单但是未支付)1:已经开团(团长已支付)2:拼团成功,3拼团失败")
            @RequestParam(value = "status", required = false) Integer status,
            @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
            @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "2") Integer size) {
        QueryWrapper<TeamFound> teamFoundQueryWrapper = new QueryWrapper<>();
        teamFoundQueryWrapper.eq("team_id", teamId);
        teamFoundQueryWrapper.orderByDesc("found_id");
        if (status != null) {
            teamFoundQueryWrapper.eq("status", status);
        }
        return (Page<TeamFound>) teamFoundService.page(new Page<>(page, size), teamFoundQueryWrapper);
    }

    @GetMapping("follows")
    public List<TeamFollow> getTeamFollows(@RequestParam(value = "found_id") Integer foundId,
                                           @RequestParam(value = "status", required = false) Set<Integer> status) {
        QueryWrapper<TeamFollow> teamFollowQueryWrapper = new QueryWrapper<>();
        teamFollowQueryWrapper.eq("found_id", foundId);
        if(null != status && status.size() > 0){
            teamFollowQueryWrapper.in("status", status);
        }
        return teamFollowService.list(teamFollowQueryWrapper);
    }

    @ApiOperation("拼主信息")
    @GetMapping("found")
    public TeamFound getTeamFounds(@ApiParam("订单主键") @RequestParam(value = "order_id", required = false) Integer orderId,
                                   @ApiParam("拼主id") @RequestParam(value = "found_id", required = false) Integer foundId) {
        TeamFound teamFound = null;
        if (foundId != null) {
            teamFound = teamFoundService.getById(foundId);
        }
        if (orderId != null) {
            teamFound = teamFoundService.getOne((new QueryWrapper<TeamFound>()).eq("order_id", orderId));
            if (teamFound == null) {
                TeamFollow teamFollow = teamFollowService.getOne((new QueryWrapper<TeamFollow>()).eq("order_id", orderId));
                teamFound = teamFoundService.getById(teamFollow.getFoundId());
            }
        }
        return teamFound;
    }

    @GetMapping("goods_items")
    public List<TeamGoodsItem> getTeamGoodsItems(@RequestParam(value = "team_id") Integer teamId) {
        return teamGoodsItemService.list((new QueryWrapper<TeamGoodsItem>()).eq("team_id", teamId));
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @PostMapping("found/refund")
    public String refundFound(@RequestBody TeamFound requestTeamFound) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        TeamFound teamFound = teamFoundService.getOne((new QueryWrapper<TeamFound>())
                .eq("found_id", requestTeamFound.getFoundId()).eq("store_id", seller.getStoreId()));
        return teamFoundService.refund(seller.getSellerId(), teamFound);
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @PostMapping("lottery")
    public SBApi lottery(@RequestBody TeamActivity requestTeamActivity) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        TeamActivity teamActivity = teamActivityService.getOne((new QueryWrapper<TeamActivity>())
                .eq("store_id", seller.getStoreId()).eq("team_id", requestTeamActivity.getTeamId()));
        teamActivityService.lottery(seller.getSellerId(), teamActivity);
        return new SBApi();
    }

    @ApiOperation("拼团订单页")
    @GetMapping("/order")
    public Order getTeamOrder(Order order) {
        User user = authenticationFacade.getPrincipal(User.class);
        Order teamOrder = orderService.getUserOrder(order.getOrderSn());
        teamGoodsItemService.checkPayOrder(teamOrder, order);
        teamOrder.setOrderGoods(orderService.getOrderGoodsListByOrderId(teamOrder.getOrderId()));
        return teamGoodsItemService.getMasterOrderByTeamOrder(user, teamOrder, order);
    }

    @ApiOperation("拼团下单")
    @PostMapping("/order")
    public SBApi addTeamOrder(@RequestBody TeamGoodsItem teamGoodsItem) {
        User user = authenticationFacade.getPrincipal(User.class);
        GoodsSku goodsSku = goodsService.getGoodsSku(teamGoodsItem.getGoodsId(), teamGoodsItem.getItemId());
        goodsSku.setIsTeam(true);
        goodsSku.useGoodsProm();
        Order teamMasterOrder = teamGoodsItemService.getMasterOrder(user, goodsSku, teamGoodsItem.getGoodsNum());
        Order masterOrder = orderService.addAndGetMasterOrder(teamMasterOrder);
        Order teamOrder = masterOrder.getOrderList().get(0);
        TeamActivity teamActivity = teamActivityService.getById(goodsSku.getPromId());
        if (teamGoodsItem.getFoundId() != null) {
            //团员
            TeamFollow follow = new TeamFollow();
            follow.setFollowUserId(user.getUserId());
            follow.setFollowUserNickname(user.getNickname());
            follow.setFollowUserHeadPic(user.getHeadPic());
            follow.setFollowTime(System.currentTimeMillis() / 1000);
            follow.setOrderId(teamOrder.getOrderId());
            follow.setOrderSn(teamOrder.getOrderSn());
            TeamFound teamFound = teamFoundService.getById(teamGoodsItem.getFoundId());
            follow.setFoundUserId(teamFound.getUserId());
            follow.setFoundId(teamGoodsItem.getFoundId());
            follow.setTeamId(teamActivity.getTeamId());
            teamFollowService.save(follow);
            redisUtil.lSet("team_follow", follow);//加入未支付团员缓存
        } else {
            TeamFound found = new TeamFound();
            found.setFoundTime(System.currentTimeMillis() / 1000);
            found.setFoundEndTime(Math.toIntExact(found.getFoundTime() + teamActivity.getTimeLimit()));
            found.setUserId(user.getUserId());
            found.setTeamId(goodsSku.getPromId());
            found.setNickname(user.getNickname());
            found.setHeadPic(user.getHeadPic());
            found.setOrderId(teamOrder.getOrderId());
            found.setOrderSn(teamOrder.getOrderSn());
            found.setNeed(teamActivity.getNeeder());
            found.setPrice(goodsSku.getShopPrice());
            found.setGoodsPrice(goodsSku.getCostPrice());
            found.setStoreId(goodsSku.getStoreId());
            teamFoundService.save(found);
        }
        return SBApi.builder().status(ResultEnum.SUCCESS.getCode()).result(masterOrder).build();
    }

    @ApiOperation("拼团订单提交接口")
    @PutMapping("/order")
    public String updateCartOrder(@RequestBody Order order) {
        User user = authenticationFacade.getPrincipal(User.class);
        cartService.checkPreOrder(user, order);
        Order teamOrder = orderService.getUserOrder(order.getOrderSn());
        teamGoodsItemService.checkPayOrder(teamOrder, order);
        teamOrder.setOrderGoods(orderService.getOrderGoodsListByOrderId(teamOrder.getOrderId()));
        Order masterOrder = teamGoodsItemService.getMasterOrderByTeamOrder(user, teamOrder, order);//拼团
        return orderService.updateMasterOrder(masterOrder);
    }

    @GetMapping("count")
    public Integer getCount(@RequestParam(value = "status", required = false) Integer status) {
        QueryWrapper<TeamActivity> wrapper = new QueryWrapper<>();
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.eq("deleted", 0);
        return teamActivityService.count(wrapper);
    }
}
