package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.common.vo.SBApi;
import com.soubao.dto.DeliveryPickOrderRq;
import com.soubao.entity.*;
import com.soubao.entity.vo.PickOrderRq;
import com.soubao.entity.vo.PickOrderVo;
import com.soubao.service.*;
import com.soubao.service.impl.AuthenticationFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 提货订单的控制层
 */
@Slf4j
@RestController
@RequestMapping("pickOrder")
@Api(tags = "提货订单的控制层")
public class PickOrderController {

    @Autowired
    private PickOrderService pickOrderService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private MallService mallService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private UserService userService;

    /**
     * 提货申请
     * 生成提货订单
     * 提货订单状态为 APP显示提货中，商家显示待发货
     *
     * @param pickOrderRq
     * @return
     */
    @ApiOperation("提货申请")
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("pick/req")
    public SBApi pickOrderReq(@Valid @RequestBody PickOrderRq pickOrderRq, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new SBApi(-1, bindingResult.getFieldError().getDefaultMessage(), null);
        }
        // 当前登录人
        User user = authenticationFacade.getPrincipal(User.class);
        pickOrderRq.setUserId(user.getUserId());
        pickOrderService.pickOrderReq(pickOrderRq);
        return new SBApi();
    }

    /**
     * 商家中心提货订单分页
     *
     * @param nickname
     * @param pickOrderStatus
     * @param pickOrderSn
     * @param startTime
     * @param endTime
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("pick_req/page")
    public Page<PickOrder> getPickReqOrderPage(
            @ApiParam("提货人") @RequestParam(value = "nickname", required = false) String nickname,
            @ApiParam("提货订单状态（0：待发货/提货中 1:确认发货 2：已发货/提货中 3：已完成/已提货）")
            @RequestParam(value = "pick_order_status", required = false) Integer pickOrderStatus,
            @ApiParam("提货单号") @RequestParam(value = "pick_order_sn", required = false) String pickOrderSn,
            @ApiParam("开始时间") @RequestParam(value = "start_time", required = false) Integer startTime,
            @ApiParam("结束时间") @RequestParam(value = "end_time", required = false) Integer endTime,
            @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
            @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "15") Integer size) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);

        QueryWrapper<PickOrder> orderQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(nickname)) {
            orderQueryWrapper.like("nickname", nickname);
        }
        if (StringUtils.isNotBlank(pickOrderSn)) {
            orderQueryWrapper.eq("pick_order_sn", pickOrderSn);
        }
        if (startTime != null) {
            orderQueryWrapper.ge("add_time", startTime);
        }
        if (endTime != null) {
            orderQueryWrapper.le("add_time", endTime);
        }
        if (pickOrderStatus != null) {
            orderQueryWrapper.eq("pick_order_status", pickOrderStatus);
        }
        orderQueryWrapper.orderByDesc("add_time");

        // 获取商家发布的商品
        List<Goods> goodsList = mallService.getgoodsBySellerUserId(seller.getUserId());
        if (CollectionUtils.isEmpty(goodsList)) {
            return null;
        }

        orderQueryWrapper.in("goods_id", goodsList.stream().map(Goods::getGoodsId).collect(Collectors.toList()));
        Page<PickOrder> pickOrderPage = pickOrderService.page(new Page<>(page, size), orderQueryWrapper);
        // 组装 购买订单 提货人，发货店铺，商品的详细信息 待确认 TODO
        pickOrderPage.getRecords().forEach(pickOrder -> {
            // 购买订单信息
            pickOrder.setOrder(orderService.getById(pickOrder.getOrderId()));

            // 商品的详细信息
            pickOrder.setOrderGoods(Arrays.asList(orderGoodsService.getOne(new QueryWrapper<OrderGoods>()
                    .eq("goods_id", pickOrder.getGoodsId())
                    .eq("order_id", pickOrder.getOrderId()))));
        });

        return pickOrderPage;
    }

    /**
     * 确认发货
     *
     * @param deliveryPickOrderRq
     * @return
     */
    @ApiOperation("确认发货")
    @PostMapping("batch_delivery")
    public SBApi batchDelivery(@RequestBody DeliveryPickOrderRq deliveryPickOrderRq) {
        pickOrderService.deliveryPickOrder(deliveryPickOrderRq);
        return new SBApi();
    }

    /**
     * 确认收货
     *
     * @param pickOrderId
     * @return
     */
    @ApiOperation("确认收货")
    @GetMapping("confirm_receipt")
    public SBApi confirmReceipt(@RequestParam("pick_order_id") Integer pickOrderId) {
        pickOrderService.confirmReceipt(pickOrderId);
        return new SBApi();
    }

//    /**
//     * 获取提货中的数量
//     * @return
//     */
//    @PreAuthorize("hasAnyRole('USER')")
//    @ApiOperation("我的->我的订单(待提货、代发货)")
//    @GetMapping("/user/pick_order/num")
//    public SBApi getPickOrderNum(){
//        Map<String,Integer> map = new HashMap<>();
//        // 获取提货订单为待提货的数量
//        int count_0 = pickOrderService.count(new QueryWrapper<PickOrder>().eq("pick_order_status", 0));
//        // 获取订单为已发货/待收货的订单
//        int count_2 = pickOrderService.count(new QueryWrapper<PickOrder>().eq("pick_order_status", 2));
//        map.put("pick_order_0",count_0);
//        map.put("pick_order_2",count_2);
//        return new SBApi(1,"获取成功",map);
//    }

    /**
     * pickOrderStatus： 0：待发货/提货中 1:确认发货 2：已发货/提货中 3：已完成/已提货
     *
     * @param pickOrderStatus
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("用户存证中心分页(提货中、已提货)")
    @GetMapping("/user/depositCertificate/page")
    public Page<PickOrder> getUserDepositCertificatePage(@ApiParam("提货订单状态（0：待发货/提货中 1:确认发货 2：已发货/提货中/待收货 3：已完成/已提货）")
                                                         @RequestParam("pick_order_status") Integer pickOrderStatus,
                                                         @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
                                                         @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "15") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        // 活订单为登陆者的提货订单
        QueryWrapper<PickOrder> queryWrapper = new QueryWrapper<PickOrder>().eq("user_id", user.getUserId());
        if (pickOrderStatus == 0) {
            queryWrapper.in("pick_order_status", 0, 1, 2);
        } else if (pickOrderStatus == 3) {
            queryWrapper.in("pick_order_status", pickOrderStatus);
        }
        Page<PickOrder> pickOrderPage = pickOrderService.page(new Page<>(page, size), queryWrapper);
        // 组装 order_goods 数据
        pickOrderPage.getRecords().forEach(pickOrder -> {
            OrderGoods orderGoods = orderGoodsService.getOne(new QueryWrapper<OrderGoods>()
                    .eq("goods_id", pickOrder.getGoodsId())
                    .eq("order_id", pickOrder.getOrderId()));
            Goods goods = mallService.goods(pickOrder.getGoodsId());
            orderGoods.setOriginalImg(goods.getOriginalImg());
            pickOrder.setOrderGoods(Arrays.asList(orderGoods));

            Store store = sellerService.getStoresByUserId(goods.getUserId());
            pickOrder.setStore(store);
        });

        return pickOrderPage;

    }

    /**
     * 全部订单（待发货，待提货，已完成）
     * pickOrderStatus： 0：待发货/提货中 1:确认发货 2：已发货/提货中 3：已完成/已提货
     *
     * @param pickOrderStatus
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("全部订单（待发货，待提货，已完成）")
    @GetMapping("/user/depositCertificate/page1")
    public Page<PickOrder> getUserDepositCertificatePage1(@ApiParam("提货订单状态（0：待发货/提货中 1:确认发货 2：已发货/提货中/待收货 3：已完成/已提货）")
                                                          @RequestParam("pick_order_status") Integer pickOrderStatus,
                                                          @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
                                                          @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "15") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        // 活订单为登陆者的提货订单
        QueryWrapper<PickOrder> queryWrapper = new QueryWrapper<PickOrder>().eq("user_id", user.getUserId());
        queryWrapper.in("pick_order_status", pickOrderStatus);
        Page<PickOrder> pickOrderPage = pickOrderService.page(new Page<>(page, size), queryWrapper);
        // 组装 order_goods 数据
        pickOrderPage.getRecords().forEach(pickOrder -> {
            OrderGoods orderGoods = orderGoodsService.getOne(new QueryWrapper<OrderGoods>()
                    .eq("goods_id", pickOrder.getGoodsId())
                    .eq("order_id", pickOrder.getOrderId()));
            Goods goods = mallService.goods(pickOrder.getGoodsId());
            orderGoods.setOriginalImg(goods.getOriginalImg());
            pickOrder.setOrderGoods(Arrays.asList(orderGoods));

            Store store = sellerService.getStoresByUserId(goods.getUserId());
            pickOrder.setStore(store);
        });

        return pickOrderPage;
    }

    /**
     * pickOrderStatus： 0：待发货/提货中 1:确认发货 2：已发货/提货中 3：已完成/已提货
     *
     * @param pickOrderId
     * @return
     */
    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("用户存证中心详情(提货中、已提货)")
    @GetMapping("/user/depositCertificate/detail")
    public PickOrderVo getUserDepositCertificateDetail(@RequestParam("pick_order_id") Integer pickOrderId) {
        // 获取提货订单
        PickOrder pickOrder = pickOrderService.getById(pickOrderId);

        // 获取提货的购买订单
        Order order = orderService.getById(pickOrder.getOrderId());
        PickOrderVo pickOrderVo = new PickOrderVo();
        // 获取购买商店
        Store store = sellerService.store(order.getStoreId());
        BeanUtils.copyProperties(pickOrder, pickOrderVo);
        User user = userService.getById_1(pickOrderVo.getUserId());
        pickOrderVo.setUser(user);
        pickOrderVo.setOrder(order);
        pickOrderVo.setStore(store);
        Goods goods = mallService.goods(pickOrder.getGoodsId());
        pickOrderVo.setOriginalImg(goods.getOriginalImg());
        pickOrderVo.setDcId(goods.getDcId());
        return pickOrderVo;
    }

    /**
     * 获取全部订单详情（待发货，待收货，完成）
     *
     * @param pickOrderId
     * @return
     */
    @GetMapping("/getPickOrderAllDetail")
    public PickOrderVo getPickOrderAllDetail(@RequestParam("picker_order_id") Integer pickOrderId) {
        // 收货人、收货电话 、收货地址
        // 提货订单号，下单时间
        PickOrder pickOrder = pickOrderService.getById(pickOrderId);
        PickOrderVo pickOrderVo = new PickOrderVo();
        BeanUtils.copyProperties(pickOrder, pickOrderVo);
        String address = mallService.getAddress(pickOrderVo.getCountry(), pickOrder.getProvince(), pickOrderVo.getCity(), pickOrderVo.getDistrict(), pickOrderVo.getTwon());
        pickOrderVo.setAddress(address + "【" + pickOrder.getAddress() + "】");

        // 商品图片，商品名称，商品价格
        Goods goods = mallService.goods(pickOrder.getGoodsId());
        pickOrderVo.setGoodsName(goods.getGoodsName());
        pickOrderVo.setGoodsPrice(goods.getShopPrice());
        pickOrderVo.setOriginalImg(goods.getOriginalImg());

        // 发货商铺
        Set<Integer> set = new HashSet<>();
        set.add(pickOrder.getSellerId());
        List<Seller> sellerList = sellerService.sellers(set);
        Store store = sellerService.store(sellerList.get(0).getStoreId());
        pickOrderVo.setStore(store);
        return pickOrderVo;
    }


}
