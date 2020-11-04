package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.common.constant.OrderConstant;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.common.vo.SBApi;
import com.soubao.dto.UserOrderSum;
import com.soubao.entity.*;
import com.soubao.entity.vo.OrderAndPickOrderVo;
import com.soubao.entity.vo.OrderVo;
import com.soubao.service.*;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.validation.group.order.Modify;
import com.soubao.vo.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class OrderController {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${security.salt}")
    private String salt;
    @Autowired
    private MallService mallService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private ReturnGoodsService returnGoodsService;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private SellerService sellerService;

    @Autowired
    private PickOrderService pickOrderService;


    @ApiOperation("拼团成功")
    @GetMapping("/team/success")
    public SBApi teamOrderSuccess(
            @ApiParam("团长订单编号") @RequestParam(value = "order_id") Integer orderId) {
        TeamFound teamFound = mallService.teamFound(orderId);
        if (teamFound.getStatus() == 2) {
            TeamActivity teamActivity = mallService.teamActivity(teamFound.getTeamId());
            if (teamActivity.getTeamType() != 2) {
                //自动确认团成员与团长的订单
                List<TeamFollow> teamFollows = mallService.teamFollows(teamFound.getFoundId(), 2);
                Set<Integer> orderIds = teamFollows.stream().map(TeamFollow::getOrderId).collect(Collectors.toSet());
                orderIds.add(teamFound.getOrderId());
                if (orderIds.size() > 0) {
                    orderService.update((new UpdateWrapper<Order>()).set("order_status", OrderConstant.CONFIRMED)
                            .in("order_id", orderIds));
                }
            }
        }
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("用户订单分页(全部订单)")
    @GetMapping("/user/page/all")
    public IPage<OrderAndPickOrderVo> getPageAll(@ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
                                                 @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "15") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        return orderService.getOrderAndPickOrderPage(page, size, user);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("用户订单分页")
    @GetMapping("/user/page")
    public Page<Order> getPage(
            @ApiParam("订单类型:6为拼团订单") @RequestParam(value = "prom_type", required = false) Integer promType,
            @ApiParam("订单状态查询描述;(WAITPAY)待支付,(WAITTEAM)待成团,(WAITSEND)待发货," +
                    "(WAITRECEIVE)待收货,(WAITCCOMMENT)待评价,(CANCEL)已取消,(FINISH)已完成,(CANCELLED)已关闭,(COMPLAIN)可投诉订单")
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "order_by", defaultValue = "order_id") String orderBy,
            @RequestParam(value = "asc", defaultValue = "false") Boolean isAsc,
            @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
            @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "15") Integer size
    ) {
        User user = authenticationFacade.getPrincipal(User.class);
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("deleted", 0).eq("user_id", user.getUserId());
        if (promType != null) {
            orderQueryWrapper.eq("prom_type", promType);
        } else {
            orderQueryWrapper.ne("prom_type", 6);//默认不查询拼团订单
        }
        if (isAsc) {
            orderQueryWrapper.orderByAsc(orderBy);
        } else {
            orderQueryWrapper.orderByDesc(orderBy);
        }
        orderService.setWrapperByType(orderQueryWrapper, type, promType, user.getUserId());
        IPage<Order> orderIPage = orderService.page(new Page<>(page, size), orderQueryWrapper);
        orderService.withOrderGoods(orderIPage.getRecords());
        orderService.withStore(orderIPage.getRecords());
        return (Page<Order>) orderIPage;
    }


    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("用户存证中心分页(已出售)")
    @GetMapping("/user/depositCertificate/page")
    public Page<Order> getUserDepositCertificatePage(@ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
                                                     @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "15") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        Store store = sellerService.getUserSellerStore(user.getUserId());
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (store == null) {
            queryWrapper.eq("store_id", 0);
        } else {
            queryWrapper.eq("store_id", store.getStoreId());
        }
        // 获取已支付 没有取消的订单为登陆者的商店 store_id
        queryWrapper.eq("order_status", 1);
        queryWrapper.eq("pay_status", 1);
        Page<Order> orderPage = orderService.page(new Page<>(page, size), queryWrapper);
        orderPage.getRecords().forEach(order -> {
            // 获取 goods的order_goods
            OrderGoods orderGoods = orderGoodsService.getOne(new QueryWrapper<OrderGoods>().eq("order_id", order.getOrderId()));
            Goods goods = mallService.goods(orderGoods.getGoodsId());
            orderGoods.setOriginalImg(goods.getOriginalImg());
            order.setOrderGoods(Arrays.asList(orderGoods));
        });

        return orderPage;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("用户存证中心(已出售)详情")
    @GetMapping("/user/depositCertificate/detail")
    public OrderVo getUserDepositCertificateDetail(@ApiParam("订单id") @RequestParam("order_id") String orderId,
                                                   @ApiParam("商品id") @RequestParam("goods_id") Integer goodsId) {
        User user = authenticationFacade.getPrincipal(User.class);
        // 根据orderid 获取订单详情
        Order order = orderService.getById(orderId);

        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);
        // 根据orderid 获取提货订单详情
        PickOrder pickOrder = pickOrderService.getOne(new QueryWrapper<PickOrder>().eq("order_id", orderId));
        orderVo.setPickOrder(pickOrder);

        // 获取登陆者最后一次购买该商品的订单信息
        List<Order> orderList = orderService.list(new QueryWrapper<Order>().eq("user_id", user.getUserId()));
        OrderGoods orderGoods = orderGoodsService.getOne(new QueryWrapper<OrderGoods>()
                .in("order_id", orderList.stream().map(Order::getOrderId).collect(Collectors.toSet()))
                .eq("goods_id", goodsId)
                .orderByDesc("rec_id")
                .last("limit 1"));

        Order sellerOrder = orderService.getById(orderGoods.getOrderId());
        Store store = sellerService.store(sellerOrder.getStoreId());
        sellerOrder.setStore(store);
        orderVo.setSellerOrder(sellerOrder);
        Goods goods = mallService.goods(goodsId);
        orderVo.setOriginalImg(goods.getOriginalImg());
        orderVo.setGoodsName(goods.getGoodsName());
        orderVo.setDcId(goods.getDcId());
        User userServiceById = userService.getById_1(orderVo.getUserId());
        orderVo.setBuyName(userServiceById.getNickname());
        return orderVo;
    }


    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @GetMapping("page")
    public Page<Order> getPage(
            @RequestParam(value = "consignee", required = false) String consignee,
            @RequestParam(value = "store_name", required = false) String storeName,
            @RequestParam(value = "store_id", required = false) Integer storeId,
            @RequestParam(value = "add_time_start", required = false) Long addTimeStart,
            @RequestParam(value = "add_time_end", required = false) Long addTimeEnd,
            @RequestParam(value = "order_id", required = false) Set<Integer> orderIds,
            @RequestParam(value = "order_statis_id", required = false) Set<Integer> orderStatisId,
            @RequestParam(value = "order_status", required = false) Set<Integer> orderStatus,
            @RequestParam(value = "pay_status", required = false) Set<Integer> payStatus,
            @RequestParam(value = "pay_code", required = false) Set<String> payCode,
            @RequestParam(value = "order_sn", required = false) String orderSn,
            @RequestParam(value = "prom_type", required = false) Integer promType,
            @RequestParam(value = "user_id", required = false) Integer userId,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "order_by", defaultValue = "order_id") String orderBy,
            @RequestParam(value = "desc", defaultValue = "desc") String sort,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "15") Integer size
    ) {
        IPage<Order> orderIPage = new Page<>();
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("deleted", 0);
        if (!StringUtils.isEmpty(consignee)) {
            orderQueryWrapper.like("consignee", "%" + consignee + "%");
        }
        if (addTimeStart != null && addTimeEnd != null) {
            orderQueryWrapper.between("add_time", addTimeStart, addTimeEnd);
        }

        if (orderIds != null && orderIds.size() > 0) {
            orderQueryWrapper.in("order_id", orderIds);
        }
        if (StringUtils.isNotEmpty(orderSn)) {
            orderQueryWrapper.eq("order_sn", orderSn);
        }
        if (promType != null) {
            orderQueryWrapper.eq("prom_type", promType);
        } else {
            orderQueryWrapper.ne("prom_type", 6);//默认不查询拼团订单
        }
        if (orderStatus != null && orderStatus.size() > 0) {
            orderQueryWrapper.in("order_status", orderStatus);
        }
        if (payStatus != null && payStatus.size() > 0) {
            orderQueryWrapper.in("pay_status", payStatus);
        }
        if (payCode != null && !payCode.isEmpty()) {
            orderQueryWrapper.in("pay_code", payCode);
        }

        if (null != storeId) {
            orderQueryWrapper.eq("store_id", storeId);
        }

        if (userId != null) {
            orderQueryWrapper.eq("user_id", userId);
        }
        if (orderStatisId != null && orderStatisId.size() > 0) {
            orderQueryWrapper.in("order_statis_id", orderStatisId);
        }
        orderService.setWrapperByType(orderQueryWrapper, type, promType, userId);
        if (sort.equals("desc")) {
            orderQueryWrapper.orderByDesc(orderBy);
        } else {
            orderQueryWrapper.orderByAsc(orderBy);
        }

        if (null != storeName) {
            Set<Integer> sIds = sellerService.storeIdsByStoreName(storeName);
            if (sIds.size() > 0) {
                orderQueryWrapper.in("store_id", sIds);
            } else {
                return (Page<Order>) orderIPage;
            }
        }
        orderQueryWrapper.ne("order_status", OrderConstant.CANCELLED);
        orderIPage = orderService.page(new Page<>(page, size), orderQueryWrapper);
        orderService.withOrderGoods(orderIPage.getRecords());
        orderService.withStore(orderIPage.getRecords());
        return (Page<Order>) orderIPage;
    }

    /**
     * 商家中心提货订单  作废
     *
     * @param consignee
     * @param storeName
     * @param storeId
     * @param addTimeStart
     * @param addTimeEnd
     * @param orderIds
     * @param orderStatisId
     * @param orderStatus
     * @param shipping_status
     * @param payStatus
     * @param payCode
     * @param orderSn
     * @param promType
     * @param userId
     * @param type
     * @param orderBy
     * @param sort
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("pick_req/page")
    public Page<Order> getPickReqOrderPage(
            @RequestParam(value = "consignee", required = false) String consignee,
            @RequestParam(value = "store_name", required = false) String storeName,
            @RequestParam(value = "store_id", required = false) Integer storeId,
            @RequestParam(value = "add_time_start", required = false) Long addTimeStart,
            @RequestParam(value = "add_time_end", required = false) Long addTimeEnd,
            @RequestParam(value = "order_id", required = false) Set<Integer> orderIds,
            @RequestParam(value = "order_statis_id", required = false) Set<Integer> orderStatisId,
            @RequestParam(value = "order_status", required = false) Set<Integer> orderStatus,
            @RequestParam(value = "shipping_status", required = false) Set<Integer> shipping_status,
            @RequestParam(value = "pay_status", required = false) Set<Integer> payStatus,
            @RequestParam(value = "pay_code", required = false) Set<String> payCode,
            @RequestParam(value = "order_sn", required = false) String orderSn,
            @RequestParam(value = "prom_type", required = false) Integer promType,
            @RequestParam(value = "user_id", required = false) Integer userId,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "order_by", defaultValue = "order_id") String orderBy,
            @RequestParam(value = "desc", defaultValue = "desc") String sort,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "15") Integer size
    ) {
        IPage<Order> orderIPage = new Page<>();
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("deleted", 0);
        if (!StringUtils.isEmpty(consignee)) {
            orderQueryWrapper.like("consignee", "%" + consignee + "%");
        }
        if (addTimeStart != null && addTimeEnd != null) {
            orderQueryWrapper.between("add_time", addTimeStart, addTimeEnd);
        }

        if (orderIds != null && orderIds.size() > 0) {
            orderQueryWrapper.in("order_id", orderIds);
        }
        if (StringUtils.isNotEmpty(orderSn)) {
            orderQueryWrapper.eq("order_sn", orderSn);
        }
        if (promType != null) {
            orderQueryWrapper.eq("prom_type", promType);
        } else {
            orderQueryWrapper.ne("prom_type", 6);//默认不查询拼团订单
        }
        if (orderStatus != null && orderStatus.size() > 0) {
            orderQueryWrapper.in("order_status", orderStatus);
        }
        if (payStatus != null && payStatus.size() > 0) {
            orderQueryWrapper.in("pay_status", payStatus);
        }
        if (payCode != null && !payCode.isEmpty()) {
            orderQueryWrapper.in("pay_code", payCode);
        }
        orderQueryWrapper.in("pay_status", 1);

        if (userId != null) {
            orderQueryWrapper.eq("user_id", userId);
        }
        if (orderStatisId != null && orderStatisId.size() > 0) {
            orderQueryWrapper.in("order_statis_id", orderStatisId);
        }
        orderService.setWrapperByType(orderQueryWrapper, type, promType, userId);
        if (sort.equals("desc")) {
            orderQueryWrapper.orderByDesc(orderBy);
        } else {
            orderQueryWrapper.orderByAsc(orderBy);
        }

        if (null != storeId) {
            orderQueryWrapper.eq("store_id", storeId);
        }
        if (null != storeName) {
            Set<Integer> sIds = sellerService.storeIdsByStoreName(storeName);
            if (sIds.size() > 0) {
                orderQueryWrapper.in("store_id", sIds);
            } else {
                return (Page<Order>) orderIPage;
            }
        }
        if (null != shipping_status) {
            Iterator it = shipping_status.iterator();
            if (it.hasNext()) {
                Integer status = (Integer) it.next();
                orderQueryWrapper.eq("shipping_status", status);
            }

        } else {
            orderQueryWrapper.in("shipping_status", 0, 4);
        }

        orderIPage = orderService.page(new Page<>(page, size), orderQueryWrapper);
        orderService.withOrderGoods(orderIPage.getRecords());
        orderService.withStore(orderIPage.getRecords());
        return (Page<Order>) orderIPage;
    }


    @GetMapping("report/order/page")
    public Page<Order> reportOrder(
            @RequestParam(value = "store_id", required = false) Integer storeId,
            @RequestParam(value = "start_time") Long startTime,
            @RequestParam(value = "end_time") Long endTime,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        if (storeId != null) {
            orderQueryWrapper.eq("store_id", storeId);
        }
        orderQueryWrapper.eq("pay_status", 1).in("order_status", 0, 1, 2, 4)
                .gt("add_time", startTime).lt("add_time", endTime);
        IPage<Order> orderIPage = orderService.page(new Page<>(page, size), orderQueryWrapper);
        orderService.withStore(orderIPage.getRecords());
        return (Page<Order>) orderIPage;
    }

    @GetMapping("finance/order/page")
    public Page<Order> financeOrder(
            @RequestParam(value = "store_id", required = false) Integer storeId,
            @RequestParam(value = "start_time") Long startTime,
            @RequestParam(value = "end_time") Long endTime,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        if (storeId != null) {
            orderQueryWrapper.eq("store_id", storeId);
        }
        orderQueryWrapper.gt("add_time", startTime).lt("add_time", endTime);
        IPage<Order> orderIPage = orderService.page(new Page<>(page, size), orderQueryWrapper);
        orderService.withStore(orderIPage.getRecords());
        return (Page<Order>) orderIPage;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("用户订单信息")
    @GetMapping("/user")
    public Order getOne(@ApiParam("订单编号") @RequestParam(value = "order_sn", required = false) String orderSn,
                        @ApiParam("订单id") @RequestParam(value = "order_id", required = false) Integer orderId) {
        User user = authenticationFacade.getPrincipal(User.class);
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("user_id", user.getUserId());
        if (StringUtils.isNotEmpty(orderSn)) {
            orderQueryWrapper.eq("order_sn", orderSn);
        }
        if (orderId != null) {
            orderQueryWrapper.eq("order_id", orderId);
        }
        Order order = orderService.getOne(orderQueryWrapper);
        OrderGoods orderGoods = orderGoodsService.getOne(new QueryWrapper<OrderGoods>().eq("order_id", order.getOrderId()));
        Goods goods = mallService.goods(orderGoods.getGoodsId());
        orderGoods.setOriginalImg(goods.getOriginalImg());
        order.setOrderGoods(Arrays.asList(orderGoods));
        return order;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/admin")
    public Order getOrder(@RequestParam(value = "order_sn", required = false) String orderSn,
                          @RequestParam(value = "order_id", required = false) Integer orderId) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(orderSn)) {
            orderQueryWrapper.eq("order_sn", orderSn);
        }
        if (orderId != null) {
            orderQueryWrapper.eq("order_id", orderId);
        }
        Order order = orderService.getOne(orderQueryWrapper);
        order.setOrderGoods(orderGoodsService.list((new QueryWrapper<OrderGoods>()).eq("order_id", order.getOrderId())));
        return order;
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("/seller")
    public Order getSellerOrder(@RequestParam(value = "order_sn", required = false) String orderSn,
                                @RequestParam(value = "order_id", required = false) Integer orderId) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        orderQueryWrapper.eq("store_id", seller.getStoreId());
        if (StringUtils.isNotEmpty(orderSn)) {
            orderQueryWrapper.eq("order_sn", orderSn);
        }
        if (orderId != null) {
            orderQueryWrapper.eq("order_id", orderId);
        }
        Order order = orderService.getOne(orderQueryWrapper);
        order.setOrderGoods(orderGoodsService.list((new QueryWrapper<OrderGoods>()).eq("order_id", order.getOrderId())));
        return order;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("取消订单")
    @PutMapping("/order_status/3")
    public SBApi userCancel(@RequestBody Order requestOrder) {
        User user = authenticationFacade.getPrincipal(User.class);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getUserId());
        if (StringUtils.isNotEmpty(requestOrder.getOrderSn())) {
            queryWrapper.eq("order_sn", requestOrder.getOrderSn());
        }
        if (requestOrder.getOrderId() != null) {
            queryWrapper.eq("order_id", requestOrder.getOrderId());
        }
        Order order = orderService.getOne(queryWrapper);
        if (!StringUtils.isEmpty(requestOrder.getUserNote())) {
            order.setUserNote(requestOrder.getUserNote());
        }
        if (!StringUtils.isEmpty(requestOrder.getConsignee())) {
            order.setConsignee(requestOrder.getConsignee());
        }
        if (!StringUtils.isEmpty(requestOrder.getMobile())) {
            order.setMobile(requestOrder.getMobile());
        }
        orderService.cancel(order);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @ApiOperation("卖家订单退款")
    @PutMapping("/pay_status/3/seller")
    public SBApi sellerCancelPay(
            @ApiParam("退款方式0余额退回，1原路退回") @RequestParam(value = "refund_type") Integer refundType,
            @RequestBody OrderAction requestOrderAction) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        Order order = orderService.getOne((new QueryWrapper<Order>())
                .eq("order_id", requestOrderAction.getOrderId()).eq("store_id", seller.getStoreId()));
        if (!order.getIsAbleRefund()) throw new ShopException(ResultEnum.ORDER_NOT_REFUND);
        if (refundType == 1 && !order.getIsAbleRefundBack()) {
            throw new ShopException(0, "该订单支付方式,不支持在线退回.code:" + order.getPayCode());
        }
        if (refundType.equals(0) && null != requestOrderAction.getAmount() &&
                requestOrderAction.getAmount().compareTo(order.getOrderAmount()) > 0) {
            throw new ShopException(ResultEnum.REFUND_MONEY_MORE_THAN_PAY_MONEY);//余额退回，要退的余额不能大于订单付款金额
        }
        requestOrderAction.setUserType(OrderConstant.SELLER);
        requestOrderAction.setActionUser(seller.getSellerId());
        orderService.cancelPay(order, requestOrderAction, refundType);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @ApiOperation("平台订单退款")
    @PutMapping("/pay_status/3/admin")
    public SBApi adminCancelPay(
            @ApiParam("退款方式0余额退回，1原路退回") @RequestParam(value = "refund_type") Integer refundType,
            @RequestBody OrderAction requestOrderAction) {
        Admin admin = authenticationFacade.getPrincipal(Admin.class);
        Order order = orderService.getById(requestOrderAction.getOrderId());
        if (!order.getIsAbleAdminRefund()) throw new ShopException(ResultEnum.ORDER_NOT_REFUND);
        if (refundType == 1 && !OrderConstant.getThirdPayCode().contains(order.getPayCode())) {
            throw new ShopException(0, "该订单支付方式,不支持在线退回.code:" + order.getPayCode());
        }
        if (refundType.equals(0) && null != requestOrderAction.getAmount() &&
                requestOrderAction.getAmount().compareTo(order.getOrderAmount()) > 0) {
            throw new ShopException(ResultEnum.REFUND_MONEY_MORE_THAN_PAY_MONEY);//余额退回，要退的余额不能大于订单付款金额
        }
        requestOrderAction.setUserType(OrderConstant.ADMIN);
        requestOrderAction.setActionUser(admin.getAdminId());
        orderService.cancelPay(order, requestOrderAction, refundType);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @ApiOperation("不同意退款")
    @PutMapping("/pay_status/4")
    public SBApi noAgreeCancelPay(@RequestBody Order requestOrder) {
        Order order = orderService.getById(requestOrder.getOrderId());
        if (!order.getIsAbleAdminRefund()) throw new ShopException(ResultEnum.ORDER_NOT_REFUND);
        orderService.update(new UpdateWrapper<Order>().eq("order_id", requestOrder.getOrderId())
                .set("pay_status", 4).set("admin_note", requestOrder.getAdminNote()));
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @PutMapping("/team/order_status/3")
    public SBApi cancel(@RequestParam(value = "order_id") Set<Integer> orderIds) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        List<Order> teamOrderList = orderService.list((new QueryWrapper<Order>()).in("order_id", orderIds)
                .eq("pay_status", OrderConstant.PAYED).eq("prom_type", 6).eq("store_id", seller.getStoreId()));
        orderService.cancelTeamOrderList(seller.getSellerId(), teamOrderList);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("订单确认收货")
    @PutMapping("order_status/2")
    public SBApi receive(@RequestBody Order requestOrder) {
        User user = authenticationFacade.getPrincipal(User.class);
        Order order = orderService.getOne((new QueryWrapper<Order>()).eq("user_id", user.getUserId())
                .eq("order_sn", requestOrder.getOrderSn()));
        orderService.receive(order, requestOrder);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @ApiOperation("卖家无效订单")
    @PutMapping("order_status/5")
    public SBApi invalid(@RequestBody Order requestOrder) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        Order order = orderService.getOne((new QueryWrapper<Order>())
                .eq("order_id", requestOrder.getOrderId()).eq("store_id", seller.getStoreId()));
        if (order == null) {
            throw new ShopException(ResultEnum.NO_FIND_ORDER);
        }
        if (!order.getIsAbleInvalid()) {
            throw new ShopException(ResultEnum.ORDER_NOT_INVALID);
        }
        orderService.invalid(order, seller);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @ApiOperation("卖家确认订单")
    @PutMapping("order_status/1")
    public SBApi confirm(@RequestBody Order requestOrder) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        Order order = orderService.getOne((new QueryWrapper<Order>())
                .eq("order_id", requestOrder.getOrderId()).eq("store_id", seller.getStoreId()));
        if (!order.getIsAbleConfirm()) {
            throw new ShopException(ResultEnum.ORDER_NOT_CONFIRM);
        }
        orderService.confirm(order, seller);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @ApiOperation("卖家取消确认订单")
    @PutMapping("order_status/0")
    public SBApi cancelConfirm(@RequestBody Order requestOrder) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        Order order = orderService.getOne((new QueryWrapper<Order>())
                .eq("order_id", requestOrder.getOrderId()).eq("store_id", seller.getStoreId()));
        if (!order.getIsAbleCancelConfirm()) {
            throw new ShopException(ResultEnum.ORDER_NOT_CANCEL_CONFIRM);
        }
        orderService.cancelConfirm(order, seller);
        return new SBApi();
    }

    /**
     * 根据订单ID 获取订单信息  order  ordergoods
     *
     * @param orderId
     * @return
     */
    @GetMapping("order")
    public Order getOrder(@RequestParam("order_id") Integer orderId) {
        return orderService.getOrder(orderId);
    }


    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("order")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Order addOrder(@RequestBody Order order) {
        User user = userService.getUserCurrent();
        return orderService.addAndGetMasterOrder(order, user);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("订单余额支付")
    @PutMapping("/user_money")
    public SBApi orderBalancePay(@RequestBody Order requestOrder) {
        User user = userService.getUserCurrent();
        if (user.getIsLock() == 1) {
            throw new ShopException(ResultEnum.USER_LOCK);
        }
        if (StringUtils.isEmpty(user.getPaypwd())) {
            throw new ShopException(ResultEnum.PAYPWD_NOT_SET);
        }
        if (!passwordEncoder.matches(requestOrder.getPayPwd(), user.getPaypwd().replace(salt, ""))) {
            throw new ShopException(ResultEnum.INVALID_PAY_PWD);
        }
        //List<OrderGoods> list = requestOrder.getOrderGoods();
        requestOrder.setUserId(user.getUserId());

        requestOrder.setUserMoney(user.getUserMoney());

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("user_id", requestOrder.getUserId());
        if (StringUtils.isNotEmpty(requestOrder.getMasterOrderSn()) && StringUtils.isNotEmpty(requestOrder.getOrderSn())) {
            throw new ShopException(ResultEnum.NO_FIND_ORDER);
        }
        if (StringUtils.isNotEmpty(requestOrder.getMasterOrderSn())) {
            orderQueryWrapper.eq("master_order_sn", requestOrder.getMasterOrderSn());
        }
        if (StringUtils.isNotEmpty(requestOrder.getOrderSn())) {
            orderQueryWrapper.eq("order_sn", requestOrder.getOrderSn());
        }
        List<Order> payOrderList = orderService.list(orderQueryWrapper);
        orderService.useMoneyPayOrders(payOrderList, requestOrder.getUserMoney());
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("获取支付订单")
    @GetMapping("pay/master_order")
    public Order getMasterOrder(@ApiParam("主订单号") @RequestParam(value = "master_order_sn", required = false) String masterOrderSn,
                                @ApiParam("子订单号") @RequestParam(value = "order_sn", required = false) String orderSn) {
        User user = authenticationFacade.getPrincipal(User.class);
        Order masterOrder = new Order();
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<Order>().eq("user_id", user.getUserId());
        if (StringUtils.isNotEmpty(orderSn)) {
            orderQueryWrapper.eq("order_sn", orderSn);
        }
        if (StringUtils.isNotEmpty(masterOrderSn)) {
            orderQueryWrapper.eq("master_order_sn", masterOrderSn);
            masterOrder.setMasterOrderSn(masterOrderSn);
        }
        List<Order> orderList = orderService.list(orderQueryWrapper);
        if (orderList.size() == 0) {
            throw new ShopException(ResultEnum.NO_FIND_ORDER);
        }

        masterOrder.setOrderList(orderList);
        return orderService.calculateMasterOrder(masterOrder);
    }

    @ApiOperation("下架")
    @GetMapping("offshelf")
    public SBApi offShelfOrder(@RequestParam("order_id") Integer orderId) {
        // 修改订单状态为 可提货
        Order order = new Order();
        order.setOrderId(orderId);
        order.setShippingStatus(3);
        orderService.updateById(order);

        return new SBApi();
    }


    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("pay/master_order")
    public SBApi updateMasterOrder(@RequestBody Order masterOrder, SBApi sbApi) {
        Order order = masterOrder.getOrderList().get(0);
        OrderGoods orderGoods = order.getOrderGoods().get(0);
        if (order.getOrderAmount().compareTo(BigDecimal.ZERO) == 0) {
            order.setPayStatus(OrderConstant.PAYED);
            order.setPayTime(System.currentTimeMillis() / 1000);
            rabbitTemplate.convertAndSend("pay_order", "", masterOrder.getOrderList());
        }
        orderGoodsService.updateById(orderGoods);
        orderService.updateById(order);
        sbApi.setResult(masterOrder);
        return sbApi;
    }

    /**
     * TODO 去掉
     * 提货申请
     * 生成提货订单
     * 提货订单状态为 APP显示提货中，商家显示待发货
     *
     * @param orderId
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("pick/req")
    public SBApi pickPayedOrder(@RequestParam Integer orderId,
                                @RequestParam Integer userId) {
        SBApi sbApi = new SBApi();
        Order order = orderService.getById(orderId);
        if (userId.equals(order.getUserId())) {
            order.setShippingStatus(4);//申请提货
            order.setApplyDelivery(System.currentTimeMillis() / 1000);  // 申请提货时间
            orderService.updateById(order);
        } else {
            sbApi.setResult(2);
        }

        return sbApi;
    }

    /**
     * 商家确认订单
     *
     * @param orderId
     * @return
     */
    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("pick/ack")
    public SBApi ackPickedOrder(@RequestParam Integer orderId) {
        SBApi sbApi = new SBApi();
        try {
            Order order = orderService.getById(orderId);
            order.setShippingStatus(0);
            orderService.updateById(order);
        } catch (Exception ex) {
            sbApi.setResult(ResultEnum.NO_FIND_ORDER);

        } finally {
            return sbApi;
        }

    }

    /**
     * TODO 去掉
     * 出售
     *
     * @param orderId
     * @param price
     * @return
     */
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("goods/re-sell")
    public SBApi reSellGood(@RequestParam("order_id") Integer orderId,
                            @RequestParam("price") BigDecimal price) {
        SBApi sBApi = new SBApi();
        try {
            OrderGoods orderGoods = orderGoodsService.getOne(new QueryWrapper<OrderGoods>().eq("order_id", orderId));
            if (orderGoods != null) {
                mallService.updateSellState(orderGoods.getGoodsId(), null, 1, price);
                Order order = orderService.getById(orderId);
                order.setShippingStatus(5);
                orderService.updateById(order);
            }
        } catch (Exception ex) {
            sBApi.setResult(ResultEnum.FAIL);
            sBApi.setStatus(-1);
        } finally {
            return sBApi;
        }

    }


    @ApiOperation("卖家修改订单")
    @PreAuthorize("hasAnyRole('SELLER')")
    @PutMapping("/seller")
    public SBApi editOrder(@Validated(Modify.class) @RequestBody Order editOrder) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        Order order = orderService.getOne((new QueryWrapper<Order>()).eq("store_id", seller.getStoreId())
                .eq("order_sn", editOrder.getOrderSn()));
        orderService.modify(seller, order, editOrder);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("订单统计概况")
    @GetMapping("statistic")
    public OrderStatistics orderStatistic() {
        User user = authenticationFacade.getPrincipal(User.class);
        // 代付款
        int waitPayCount = orderService.count(new QueryWrapper<Order>()
                .eq("pay_status", 0).eq("order_status", 0).eq("user_id", user.getUserId()));
        // 待发货
        int waitSendCount = pickOrderService.count(new QueryWrapper<PickOrder>()
                .eq("pick_order_status", 0).eq("user_id", user.getUserId()));
        // 待收货
        int waitReceiveCount = pickOrderService.count(new QueryWrapper<PickOrder>()
                .eq("pick_order_status", 2).eq("user_id", user.getUserId()));
        // 出售中
        Store store = sellerService.getUserSellerStore(user.getUserId());
        List<Goods> goodsList = null;
        if (store == null) {
            goodsList = new ArrayList<>();
        } else {
            goodsList = mallService.getGoodsByStoreId(store.getStoreId());
        }
        OrderStatistics orderStatistics = new OrderStatistics();
        orderStatistics.setWaitPayCount(waitPayCount);
        orderStatistics.setWaitSendCount(waitSendCount);
        orderStatistics.setWaitReceiveCount(waitReceiveCount);
        orderStatistics.setSellCount(goodsList.size());
        return orderStatistics;
    }

    @GetMapping("refund_order/page")
    public IPage<Order> refundOrderPage(@RequestParam(value = "prom_type", required = false) Integer promType,
                                        @RequestParam(value = "consignee", required = false) String consignee,
                                        @RequestParam(value = "order_sn", required = false) String orderSn,
                                        @RequestParam(value = "mobile", required = false) String mobile,
                                        @RequestParam(value = "p", defaultValue = "1") Integer page,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (promType != null) {
            queryWrapper.eq("prom_type", promType);
        }
        if (!StringUtils.isEmpty(consignee)) {
            queryWrapper.like("consignee", consignee);
        }
        if (!StringUtils.isEmpty(orderSn)) {
            queryWrapper.eq("order_sn", orderSn);
        }
        if (!StringUtils.isEmpty(mobile)) {
            queryWrapper.like("mobile", mobile);
        }
        queryWrapper.eq("shipping_status", 0);
        queryWrapper.eq("order_status", 3);
        queryWrapper.in("pay_status", 1, 3);//预售退款不在退款单里
        queryWrapper.orderByDesc("add_time");
        return orderService.page(new Page<>(page, size), queryWrapper);
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @DeleteMapping("order")
    public SBApi remove(@RequestParam("order_id") Integer orderId) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        orderService.removeOrder(orderId, seller.getStoreId());
        return new SBApi();
    }


    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/order_list/user")
    public List<Order> serOrderList(@RequestParam(value = "order_statis_id", required = false) Integer orderStatisId,
                                    @RequestParam(value = "order_ids", required = false) Set<Integer> orderIds) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        User user = authenticationFacade.getPrincipal(User.class);
        queryWrapper.eq("user_id", user.getUserId());
        if (orderStatisId != null) {
            queryWrapper.eq("order_statis_id", orderStatisId);
        }
        if (orderIds != null) {
            queryWrapper.in("order_id", orderIds);
        }
        return orderService.list(queryWrapper);
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("/order_list/seller")
    public List<Order> sellerOrderList(@RequestParam(value = "order_statis_id", required = false) Integer orderStatisId,
                                       @RequestParam(value = "order_ids", required = false) Set<Integer> orderIds) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        queryWrapper.eq("store_id", seller.getStoreId());
        if (orderStatisId != null) {
            queryWrapper.eq("order_statis_id", orderStatisId);
        }
        if (orderIds != null) {
            queryWrapper.in("order_id", orderIds);
        }
        List<Order> orders = orderService.list(queryWrapper);
        orderService.withRegions(orders);
        orderService.withStore(orders);
        return orders;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/order_list")
    public List<Order> orderList(@RequestParam(value = "order_statis_id", required = false) Integer orderStatisId,
                                 @RequestParam(value = "order_ids", required = false) Set<Integer> orderIds) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (orderStatisId != null) {
            queryWrapper.eq("order_statis_id", orderStatisId);
        }
        if (orderIds != null) {
            queryWrapper.in("order_id", orderIds);
        }
        return orderService.list(queryWrapper);
    }

    @GetMapping("/page/delivery")
    public IPage<Order> pageDelivery(
            @RequestParam(value = "shipping_status", defaultValue = "0") Integer shippingStatus,
            @RequestParam(value = "store_id", required = false) Integer storeId,
            @RequestParam(value = "consignee", required = false) String consignee,
            @RequestParam(value = "order_sn", required = false) String orderSn,
            @RequestParam(value = "start_time", required = false) Long startTime,
            @RequestParam(value = "end_time", required = false) Long endTime,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("deleted", 0)
                .eq("shop_id", 0)
                // 发出已提货的请求后才可以被发货商品查出来
                .eq("shipping_status", shippingStatus)
                .eq("store_id", storeId)
                .in("order_status", 0, 1, 2, 4)
                .in("prom_type", 0, 1, 2, 3, 4, 6);
        if (!StringUtils.isEmpty(consignee)) {
            orderQueryWrapper.eq("consignee", consignee);
        }
        if (startTime != null) {
            orderQueryWrapper.ge("add_time", startTime);
        }
        if (endTime != null) {
            orderQueryWrapper.le("add_time", endTime);
        }
        if (!StringUtils.isEmpty(orderSn)) {
            orderQueryWrapper.eq("order_sn", orderSn);
        }
        orderQueryWrapper.orderByDesc("add_time");
        IPage<Order> pageDelivery = orderService.page(new Page<>(page, size), orderQueryWrapper);
        orderService.withOrderGoods(pageDelivery.getRecords());
        orderService.withUser(pageDelivery.getRecords());
        orderService.withRegions(pageDelivery.getRecords());
        return pageDelivery;
    }


    @GetMapping("delivery_info")
    public Order deliveryInfo(@RequestParam("order_id") Integer orderId) {
        return orderService.deliveryInfo(orderId);
    }

    @GetMapping("report")
    public Map<String, Object> report(
            @RequestParam(value = "store_id", required = false) Integer storeId,
            @RequestParam(value = "is_team", required = false) Boolean isTeam,
            @RequestParam(value = "add_time_start", required = false) Long addTimeStart,
            @RequestParam(value = "add_time_end", required = false) Long addTimeEnd) {
        Map<String, Object> report = new HashMap<>();
        QueryWrapper<Order> baseQueryWrapper = new QueryWrapper<>();
        QueryWrapper<PickOrder> pickOrderQueryWrapper = new QueryWrapper<>();
        baseQueryWrapper.eq("deleted", 0);
        List<Goods> goodsList = new ArrayList<>();
        if (storeId != null) {
            baseQueryWrapper.eq("store_id", storeId);
            Store store = sellerService.store(storeId);
            Seller seller = sellerService.getSellerByStoreId(store.getStoreId());
            goodsList = mallService.getgoodsBySellerUserId(seller.getUserId());
        }
        if (isTeam != null) {
            if (isTeam) {
                baseQueryWrapper.eq("prom_type", 6);
            } else {
                baseQueryWrapper.ne("prom_type", 6);
            }
        }

        QueryWrapper<Order> queryWrapper = baseQueryWrapper.clone();
        if (addTimeStart != null && addTimeEnd != null) {
            queryWrapper.between("add_time", addTimeStart, addTimeEnd);
        }
        // 待支付
        report.put("wait_pay_count", orderService.count(queryWrapper.clone().eq("pay_status", 0)));

        // 代发货
        List<Integer> list = goodsList.stream().map(Goods::getGoodsId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)) {
            pickOrderQueryWrapper.eq("goods_id", 0 );
        } else {
            pickOrderQueryWrapper.in("goods_id", list );
        }
        report.put("wait_shipping_count", pickOrderService.count(pickOrderQueryWrapper
                .eq("pick_order_status", 0)));
        report.put("part_shipping_count", pickOrderService.count(pickOrderQueryWrapper
                .eq("pick_order_status", 1)));
        report.put("wait_confirm_count", orderService.count(queryWrapper.clone().eq("order_status", 0)));
        report.put("wait_refund_count", orderService.count(queryWrapper.clone().eq("order_status", 3).notIn("pay_status", 0, 3, 4)));
        Order orderYesterdayReport = orderService.getOne(baseQueryWrapper.clone()
                .select("IFNULL(sum(order_amount),0) as order_amount,count(order_id) as order_id").eq("pay_status", 1)
                .apply("DATE_SUB( CURDATE( ), INTERVAL 1 DAY ) <= from_unixtime( add_time, '%Y-%m-%d' ) "));
        Order orderMonthReport = orderService.getOne(baseQueryWrapper.clone()
                .select("IFNULL(sum(order_amount),0) as order_amount,count(order_id) as order_id").eq("pay_status", 1)
                .apply("from_unixtime(add_time, '%Y%m') = DATE_FORMAT( CURDATE( ), '%Y%m' )"));
        report.put("yesterday_order_amount", orderYesterdayReport.getOrderAmount());
        report.put("yesterday_order_count", orderYesterdayReport.getOrderId());
        report.put("month_order_amount", orderMonthReport.getOrderAmount());
        report.put("month_order_count", orderMonthReport.getOrderId());
        return report;
    }

    @GetMapping("report/days")
    public List<OrderDayReport> daysReport(
            @RequestParam(value = "store_id", required = false) Integer storeId,
            @RequestParam("start_time") Long startTime,
            @RequestParam("end_time") Long endTime) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("pay_status", 1).in("order_status", 0, 1, 2, 4)
                .gt("add_time", startTime).lt("add_time", endTime);
        if (storeId != null) {
            orderQueryWrapper.eq("store_id", storeId);
        }
        return orderService.getOrderDayReportList(orderQueryWrapper, startTime, endTime);
    }

    @GetMapping("finance/days")
    public List<OrderDayReport> daysFinance(
            @RequestParam(value = "store_id", required = false) Integer storeId,
            @RequestParam("start_time") Long startTime,
            @RequestParam("end_time") Long endTime) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        if (storeId != null) {
            orderQueryWrapper.eq("o.store_id", storeId);
        }
        orderQueryWrapper.eq("o.pay_status", 1).in("o.order_status", 2, 4)
                .eq("o.shipping_status", 1).gt("o.add_time", startTime).lt("o.add_time", endTime);
        return orderService.getOrderDayFinanceList(orderQueryWrapper, startTime, endTime);
    }

    @GetMapping("sales_ranking/page")
    public IPage<SalesRanking> getSalesRanking(
            @RequestParam(value = "store_id", required = false) Integer storeId,
            @RequestParam(value = "cat_id", required = false) Integer catId,
            @RequestParam(value = "start_time", required = false) Long startTime,
            @RequestParam(value = "end_time", required = false) Long endTime,
            @RequestParam(value = "p", defaultValue = "1") Integer p,
            @RequestParam(value = "size", defaultValue = "20") Integer size) {
        QueryWrapper<OrderGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("og.is_send", 1);
        IPage<SalesRanking> salesRankingIPage = new Page<>();
        if (storeId != null) {
            queryWrapper.eq("o.store_id", storeId);
        }
        if (null != startTime && null != endTime) {
            queryWrapper.between("o.pay_time", startTime, endTime);
        }
        if (null != catId) {
            Set<Integer> goodsIds = mallService.goodsIdsByCatId(catId);
            if (goodsIds.size() > 0) {
                queryWrapper.in("og.goods_id", goodsIds);
            } else {
                return salesRankingIPage;
            }
        }
        salesRankingIPage = orderService.getSalesRankingPage(new Page<>(p, size), queryWrapper);
        return salesRankingIPage;
    }

    @GetMapping("store_ranking/page")
    public IPage<StoreRanking> getStoreRanking(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "sc_id", required = false) Integer scId,
            @RequestParam(value = "start_time", required = false) Long startTime,
            @RequestParam(value = "end_time", required = false) Long endTime,
            @RequestParam(value = "p", defaultValue = "1") Integer p,
            @RequestParam(value = "size", defaultValue = "20") Integer size) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("order_status", 2, 4).groupBy("store_id");
        IPage<StoreRanking> storeRankingIPage = new Page<>();
        if (null != startTime && null != endTime) {
            queryWrapper.between("add_time", startTime, endTime);
        }
        if (!StringUtils.isEmpty(type)) {
            if ("order".equals(type)) {
                queryWrapper.orderByDesc("num");
            }
            if ("turnover".equals(type)) {
                queryWrapper.orderByDesc("amount");
            }
        }
        if (null != scId) {
            Set<Integer> storeIds = sellerService.storeIdsByScId(scId);
            if (storeIds.size() > 0) {
                queryWrapper.in("store_id", storeIds);
            } else {
                return storeRankingIPage;
            }
        }
        storeRankingIPage = orderService.getStoreRankingPage(new Page<>(p, size), queryWrapper);
        orderService.withRankingStore(storeRankingIPage.getRecords());
        return storeRankingIPage;
    }

    @GetMapping("sale_details/page")
    public IPage<SaleDayDetails> getSaleDayDetails(
            @RequestParam(value = "cat_id", required = false) Integer catId,
            @RequestParam(value = "brand_id", required = false) Integer brandId,
            @RequestParam(value = "start_time") Long startTime,
            @RequestParam(value = "end_time") Long endTime,
            @RequestParam(value = "p", defaultValue = "1") Integer p,
            @RequestParam(value = "size", defaultValue = "20") Integer size) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("o.order_status", 0, 1, 2, 4).groupBy("days,goods_id").orderByDesc("days");
        IPage<SaleDayDetails> saleDayDetailsIPage = new Page<>();
        if (null != catId || null != brandId) {
            Set<Integer> goodsIds = mallService.goodsIdsByCatIdAndBrandId(catId, brandId);
            if (goodsIds.size() > 0) {
                queryWrapper.in("og.goods_id", goodsIds);
            } else {
                return saleDayDetailsIPage;
            }
        }
        if (null != startTime && null != endTime) {
            queryWrapper.between("o.add_time", startTime, endTime);
        }
        return orderService.getSaleDayDetailsPage(new Page<>(p, size), queryWrapper);
    }

    @GetMapping("/users/statistic")
    public List<UserOrderStatistics> userGroupOrder(
            @RequestParam(value = "user_ids") Set<Integer> userIds,
            @RequestParam(value = "start_time", required = false) Long startTime,
            @RequestParam(value = "end_time", required = false) Long endTime
    ) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (null != startTime && null != endTime) {
            queryWrapper.between("add_time", startTime, endTime);
        }
        queryWrapper.in("user_id", userIds);
        queryWrapper.groupBy("user_id");
        return orderService.userOrderStatisticsList(queryWrapper);
    }

    @GetMapping("user/order_sum")
    public Map<Integer, BigDecimal> getUserOrderSumByUserIds(@RequestParam("user_ids") Set<Integer> userIds) {
        List<UserOrderSum> userOrderSumList = orderService.getUserOrderSumByUserIds(userIds);
        return userOrderSumList.stream().collect(Collectors.toMap(UserOrderSum::getUserId, UserOrderSum::getOrderSum));
    }

    @GetMapping("team/order_list")
    public List<Order> getTeamOrderList(@RequestParam("team_id") Integer teamId) {
        return orderService.list(new QueryWrapper<Order>()
                .eq("prom_id", teamId)
                .eq("prom_type", 6)
                .notIn("order_status", 3, 5));
    }

    /**
     * 根据user_id 获取订单以及商店
     *
     * @param userId
     * @return
     */
    @GetMapping("/order/user_id")
    Order getOrderAndStoreByUserIds(@RequestParam(value = "user_id") Integer userId,
                                    @RequestParam(value = "goods_id") Integer goodsId) {
        List<Order> orderList = orderService.list(new QueryWrapper<Order>()
                .eq("user_id", userId)
                .eq("order_status", 1)
                .eq("pay_status", 1));
        OrderGoods orderGoods = orderGoodsService.getOne(new QueryWrapper<OrderGoods>()
                .in("order_id", orderList.stream().map(Order::getOrderId).collect(Collectors.toSet()))
                .eq("goods_id", goodsId)
                .orderByDesc("rec_id")
                .last("limit 1"));

        Order order = orderService.getById(orderGoods.getOrderId());
        Store store = sellerService.store(order.getStoreId());
        order.setStore(store);
        return order;
    }

    /**
     * 根据商店ID获取订单
     *
     * @param storeId
     * @return
     */
    @GetMapping("/order/list/store_id")
    List<Order> getOrderListByStoreId(@RequestParam("store_id") Integer storeId) {
        return orderService.list(new QueryWrapper<Order>().eq("store_id", storeId).eq("order_status", 1).eq("order_status", 1));
    }

    @GetMapping("/order/list/user_id/store_id")
    List<Order> getOrderListByUserId(@RequestParam("user_id") Integer userId,
                                     @RequestParam("store_id") Integer storeId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.and(wrap ->wrap.eq("user_id", userId).or().eq("store_id", storeId));
        wrapper.eq("order_status", 1).eq("order_status", 1);

        return orderService.list(wrapper);
    }
}
