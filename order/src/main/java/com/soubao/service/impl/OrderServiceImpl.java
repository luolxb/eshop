package com.soubao.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.common.constant.OrderConstant;
import com.soubao.common.constant.OrderGoodsConstant;
import com.soubao.dto.DeliveryPickOrderRq;
import com.soubao.dto.UserOrderSum;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.dao.OrderMapper;
import com.soubao.dto.BatchDelivery;
import com.soubao.entity.vo.OrderAndPickOrderVo;
import com.soubao.service.*;
import com.soubao.common.utils.IdUtil;
import com.soubao.common.utils.RedisUtil;
import com.soubao.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private ReturnGoodsService returnGoodsService;
    @Autowired
    private MallService mallService;
    @Autowired
    private UserService userService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private DeliveryDocService deliveryDocService;
    @Autowired
    private OrderActionService orderActionService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private ComplainService complainService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PickOrderService pickOrderService;

    @Override
    public void cancel(Order order1) {
        Order order = this.baseMapper.selectById(order1.getOrderId());
        if (order == null) {
            throw new ShopException(ResultEnum.NO_FIND_ORDER);
        }
        if (!order.getIsAbleCancel()) {
            throw new ShopException(ResultEnum.ORDER_NOT_CANCEL);
        }
        if (order.getPayStatus() == OrderConstant.PAYED) {
            // 已付款的取消订单
            // 修改买卖双方的的余额
            // 修改买家（加）
            User byUser = userService.getUserInfoById1(order.getUserId());
            if (byUser != null) {
                BigDecimal amountBy = byUser.getUserMoney();
                if (null == amountBy) {
                    amountBy = new BigDecimal(0.00);
                }
                amountBy = amountBy.add(order.getGoodsPrice());
                byUser.setUserMoney(amountBy);
                userService.userUpdate(byUser);
            }

            User sellUser = null;
            // 修改买家（减）
            UserSellerStore userSellerStore = sellerService.userSellerStoreByStoreId(order.getStoreId());
            if (userSellerStore == null) {
                // 为商家、修改商家店铺余额，修改商家用户
                Store store = sellerService.store(order.getStoreId());
                if (null != store) {
                    // 更新商铺可用余额
                    BigDecimal amount = store.getStoreMoney();
                    BigDecimal amount_ = amount.subtract(order.getGoodsPrice());
                    store.setStoreMoney(amount_);
                    sellerService.updateById(store);
                }
                Seller seller = sellerService.getSellerByStoreId(order.getStoreId());
                sellUser = userService.getUserInfoById1(seller.getUserId());

            } else {
                sellUser = userService.getUserInfoById1(userSellerStore.getUserId());
            }

            BigDecimal amountSell = sellUser.getUserMoney();
            if (null == amountSell) {
                amountSell = new BigDecimal(0.00);
            }
            amountSell = amountSell.subtract(order.getGoodsPrice());
            sellUser.setUserMoney(amountSell);
            userService.userUpdate(sellUser);

            // 修改通证状态 归属
            // 修改商品归属
            OrderGoods orderGoods = orderGoodsService.getOne(new QueryWrapper<OrderGoods>().eq("order_id", order.getOrderId()));
            //更新商品信息
            mallService.updateSellStateGoods(orderGoods.getGoodsId(), sellUser.getUserId(), order.getStoreId(), 1, order.getGoodsPrice());
//            //更新一下通证状态
            Goods goods = mallService.goods(orderGoods.getGoodsId());
            // 判断当前商品是否为第一次出售成功
            List<OrderGoods> orderGoodsList = orderGoodsService.list(new QueryWrapper<OrderGoods>().eq("goods_id", orderGoods.getGoodsId()));
            List<Order> orderList = this.baseMapper.selectList(new QueryWrapper<Order>()
                    .eq("pay_status", 1)
                    .in("order_id", orderGoodsList.stream().map(OrderGoods::getOrderId).collect(Collectors.toList())));
            if (orderList.size() > 1) {
                sellerService.updateDepositCertificateStatus(sellUser.getUserId(), goods.getDcId(), null, 1);
            } else {
                sellerService.updateDepositCertificateStatus(sellUser.getUserId(), goods.getDcId(), null, 0);
            }
        }
        order.setOrderStatus(OrderConstant.CANCELLED);
        order.setPayStatus(5);
        updateById(order);
        order.setOrderGoods(orderGoodsService.list((new QueryWrapper<OrderGoods>()).eq("order_id", order.getOrderId())));
        rabbitTemplate.convertAndSend("cancelled_order", "", order);

//        // 修改买家 卖家店铺商品的交易数 - 1
//        sellerService.updateStoreTransactionNum(0, order.getStoreId(), order.getUserId());
    }

    @Override
    public void cancelTeamOrderList(Integer sellerId, List<Order> teamOrderList) {
        for (Order order : teamOrderList) {
            order.setSellerId(sellerId);
            order.setOrderStatus(OrderConstant.CANCELLED);
        }
        updateBatchById(teamOrderList);
        withOrderGoods(teamOrderList);
        rabbitTemplate.convertAndSend("cancelled_order", "", teamOrderList);
    }

    @Override
    public Order addAndGetMasterOrder(Order masterOrder, User user) {
        Set<Integer> orderIds = addOrderGetOrderIds(masterOrder, user);
        List<Order> orderList = list((new QueryWrapper<Order>()).in("order_id", orderIds));
        Map<Integer, List<OrderGoods>> orderGoodsMap = orderGoodsService.list((new QueryWrapper<OrderGoods>()).in("order_id", orderIds))
                .stream().collect(Collectors.groupingBy(OrderGoods::getOrderId));
        for (Order order : orderList) {
            order.setOrderGoods(orderGoodsMap.get(order.getOrderId()));
        }
        rabbitTemplate.convertAndSend("create_order", "", orderList);
        masterOrder.setOrderList(orderList);
        return masterOrder;
    }

    private Set<Integer> addOrderGetOrderIds(Order masterOrder, User user) {
        masterOrder.setMasterOrderSn(IdUtil.nextId() + "");//先生成主订单号
        for (Order order : masterOrder.getOrderList()) {
            order.setOrderSn(IdUtil.nextId() + "");
            order.setMasterOrderSn(masterOrder.getMasterOrderSn());
        }
        saveBatch(masterOrder.getOrderList());
        List<OrderGoods> orderGoodsList = new ArrayList<>();
        Set<Integer> orderIds = new HashSet<>();
        for (Order order : masterOrder.getOrderList()) {
            orderIds.add(order.getOrderId());
            for (OrderGoods orderGoods : order.getOrderGoods()) {
                orderGoods.setOrderId(order.getOrderId());
                // 根据 goodsId 获取商品信息
                Goods goods = mallService.goods(orderGoods.getGoodsId());
                if (user.getUserId().equals(goods.getOwnerId())) {
                    throw new ShopException(400, "操作失败，不能购买自己上架的商品");
                }
                orderGoodsList.add(orderGoods);
            }
        }
        orderGoodsService.saveBatch(orderGoodsList);
        return orderIds;
    }

    @Override
    public void useMoneyPayOrders(List<Order> payOrderList, BigDecimal userMoney) {
        if (payOrderList.size() == 0) {
            throw new ShopException(ResultEnum.NO_FIND_ORDER);
        }
        if (payOrderList.get(0).getPayStatus() == OrderConstant.PAYED) {
            throw new ShopException(ResultEnum.ORDER_PAYED);
        }
        BigDecimal totalOrderAmount = payOrderList.stream().map(Order::getOrderAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalOrderAmount.compareTo(userMoney) > 0) {
            throw new ShopException(ResultEnum.NOT_ENOUGH_USER_MONEY);
        }
        payOrderList.forEach(payOrder -> {

            payOrder.setUserMoney(payOrder.getOrderAmount());
            payOrder.setOrderAmount(BigDecimal.ZERO);
            payOrder.setPayName("余额支付");
            payOrder.setPayStatus(1);
//            payOrder.setPayStatus(getPayStatusByPay(payOrder));
            // 订单状态(0待确认，1已确认，2已收货，3已取消，4已完成，5已作废
            payOrder.setOrderStatus(1);
            payOrder.setPayTime(System.currentTimeMillis() / 1000);
//            payOrder.setShippingStatus(3);//代表:付款已成功

            OrderGoods orderGoods = orderGoodsService.getOne(new QueryWrapper<OrderGoods>().eq("order_id", payOrder.getOrderId()));
            if (orderGoods != null) {
                payOrder.setStoreId(orderGoods.getStoreId());
                (this.baseMapper).updateById(payOrder);

                Integer goodsId = orderGoods.getGoodsId();
                Goods goods = mallService.goods(goodsId);
                //同步一下买卖双方的钱包余额
                // 卖家
                User userTo = userService.getUserInfoById1(goods.getOwnerId());
                // 买家
                User userFrom = userService.getUserInfoById1(payOrder.getUserId());
                if (null != userTo) {
                    BigDecimal amountTo = userTo.getUserMoney();
                    if (null == amountTo) {
                        amountTo = new BigDecimal(0.00);
                    }
                    amountTo = amountTo.add(payOrder.getUserMoney());
                    userTo.setUserMoney(amountTo);
                    userService.userUpdate(userTo);
                }

                // 买家  扣余额与mq重复
//                User userFrom = userService.getUserInfoById1(payOrder.getUserId());
//                if(null != userFrom){
//                    BigDecimal amountFrom = userFrom.getUserMoney();
//                    if(null == amountFrom) {
//                        amountFrom = new BigDecimal(0.00);
//                    }
//                    amountFrom = amountFrom.subtract(payOrder.getUserMoney());
//                    userFrom.setUserMoney(amountFrom);
//                    userService.userUpdate(userFrom);
//
//                }

                //记录一下买卖双方的记录
                Transaction transaction = new Transaction();
                transaction.setBuyerId(userFrom.getUserId());
                transaction.setSellerId(userTo.getUserId());
                transaction.setGoodsId(goodsId);
                transaction.setOrderId(orderGoods.getOrderId());
                transaction.setPurchaseTime(System.currentTimeMillis() / 1000);
                transaction.setStatus((byte) 1);
                transactionService.save(transaction);

                //  判断用户是否有商店，如果没有就新建一个
                User user = userService.getUserCurrent();
                UserSellerStore userSellerStore = sellerService.userSellerStore(user.getNickname(), user.getUserId(), user.getMobile());
                // 修改物品商店ID
                //更新商品信息
                mallService.updateSellStateGoods(goodsId, payOrder.getUserId(), userSellerStore.getStoreId(), 0, payOrder.getUserMoney());
                //更新一下通证状态
                sellerService.updateDepositCertificateStatus(payOrder.getUserId(), goods.getDcId(), null, 1);

//                // 修改以前的订单为为已出售
//                List<OrderGoods> orderGoods_ = orderGoodsService.list(new QueryWrapper<OrderGoods>().eq("goods_id", orderGoods.getGoodsId()));
//                List<Integer> orderids = orderGoods_.stream().map(OrderGoods::getOrderId).collect(Collectors.toList());
//                List<Order> orderList = list(new QueryWrapper<Order>().in("order_id", orderids).and(wrapper -> wrapper.eq("shipping_status", 5)));
//                orderList.forEach(order -> {
//                    order.setShippingStatus(6);
//                    order.setSellPrice(payOrder.getUserMoney());
//                    order.setSellTime(System.currentTimeMillis()/1000);
//                    order.setNextOrderId(payOrder.getOrderId());
//                    updateById(order);
//                });

                Integer storeId = orderGoods.getStoreId();
//                // 修改买家 卖家店铺商品的交易数 + 1
//                sellerService.updateStoreTransactionNum(0, storeId, payOrder.getUserId());

                // 根据商店ID 判断是否是商家id，如果是就操作店铺余额
                UserSellerStore userSellerStore1 = sellerService.userSellerStoreByStoreId(storeId);
                // 在用户商铺查询不到即为商家店铺
                if (userSellerStore1 == null) {
                    Store store = sellerService.store(storeId);
                    if (null != store) {
                        // 更新商铺可用余额
                        BigDecimal amount = store.getStoreMoney();
                        BigDecimal amount_ = amount.add(payOrder.getUserMoney());
                        store.setStoreMoney(amount_);
                        sellerService.updateById(store);
                    }
                }
            }
        });
        if (!updateBatchById(payOrderList)) {
            throw new ShopException(ResultEnum.UNKNOWN_ERROR);
        }
        rabbitTemplate.convertAndSend("pay_order", "", payOrderList);
    }

    @Override
    public Order calculateMasterOrder(Order masterOrder) {
        masterOrder.setGoodsPrice(new BigDecimal(BigInteger.ZERO));
        masterOrder.setOrderAmount(new BigDecimal(BigInteger.ZERO));
        masterOrder.setTotalAmount(new BigDecimal(BigInteger.ZERO));
        masterOrder.setShippingPrice(new BigDecimal(BigInteger.ZERO));
        //数据库不存在的字段
        masterOrder.setCutFee(new BigDecimal(BigInteger.ZERO));
        masterOrder.setGoodsNum(0);
        for (Order storeOrder : masterOrder.getOrderList()) {
            masterOrder.setGoodsPrice(masterOrder.getGoodsPrice().add(storeOrder.getGoodsPrice()));
            masterOrder.setOrderAmount(masterOrder.getOrderAmount().add(storeOrder.getOrderAmount()));
            masterOrder.setTotalAmount(masterOrder.getTotalAmount().add(storeOrder.getTotalAmount()));
            if (storeOrder.getCutFee() != null) {
                masterOrder.setCutFee(masterOrder.getCutFee().add(storeOrder.getCutFee()));
            }
            masterOrder.setShippingPrice(masterOrder.getShippingPrice().add(storeOrder.getShippingPrice()));
        }
        if (masterOrder.getMasterOrderSn() == null) {
            masterOrder.setMasterOrderSn(masterOrder.getOrderList().get(0).getMasterOrderSn());
        }
        return masterOrder;
    }

    @Override
    public void receive(Order order, Order requestOrder) {
        if (!order.getIsAbleReceive()) {
            throw new ShopException(ResultEnum.ORDER_NOT_RECEIVE);
        }
        // 修改订单状态 shippingStatus
        order.setOrderStatus(OrderConstant.DELIVERY);
        order.setConfirmTime(System.currentTimeMillis() / 1000);
        order.setShippingStatus(7);
        updateById(order);

        log.info("用户确认收货订单队列消费:{}", order);
        orderActionService.save(orderActionService.getOrderActionByReceiveOrder(order));
//        rabbitTemplate.convertAndSend("receive_order", "", order);
    }

    @Override
    public Integer getPayStatusByPay(Order order) {
        int payStatus = OrderConstant.NOT_PAY;
        if (order.getPromType() == 4) {
            if (order.getPayStatus() == OrderConstant.NOT_PAY) {
                if (order.getPaidMoney().compareTo(BigDecimal.ZERO) > 0) {
                    //付订金
                    payStatus = OrderConstant.PRE_PAY;
                } else {
                    //全额
                    payStatus = OrderConstant.PAYED;
                }
            }
            if (order.getPayStatus() == OrderConstant.PRE_PAY) {
                //付尾款
                payStatus = OrderConstant.PAYED;
            }
        } else {
            payStatus = OrderConstant.PAYED;
        }
        return payStatus;
    }

    @Override
    public void removeOrder(Integer orderId, Integer storeId) {
        Order order = getById(orderId);
        if (order == null) {
            throw new ShopException(ResultEnum.NO_FIND_ORDER);
        }
        if (order.getDeleted() == 1) {
            throw new ShopException(ResultEnum.ORDER_DELETED);
        }
        if (order.getOrderStatus() != 5) {
            throw new ShopException(ResultEnum.ORDER_NOT_IS_INVALID);
        }
        update(new UpdateWrapper<Order>().set("deleted", 1)
                .eq("order_id", orderId)
                .eq("store_id", storeId));
    }

    @Override
    public void withOrderGoods(List<Order> records) {
        if (records.size() > 0) {
            Set<Integer> orderIds = records.stream().map(Order::getOrderId).collect(Collectors.toSet());
            Map<Integer, List<OrderGoods>> orderGoodsListMap = orderGoodsService.list(new QueryWrapper<OrderGoods>().in("order_id", orderIds))
                    .stream().collect(Collectors.groupingBy(OrderGoods::getOrderId));
            for (Order order : records) {
                int totalGoodsCount = 0;
                List<OrderGoods> orderGoodsList = orderGoodsListMap.get(order.getOrderId());
                order.setOrderGoods(orderGoodsList);
                for (OrderGoods orderGoods : orderGoodsList) {
                    totalGoodsCount += orderGoods.getGoodsNum();
                }
                order.setTotalGoodsCount(totalGoodsCount);
            }
        }
    }

    @Override
    public void withUser(List<Order> records) {
        Set<Integer> userIds = records.stream().map(Order::getUserId).collect(Collectors.toSet());
        if (userIds.size() > 0) {
            Map<Integer, User> userMap = userService.usersByIds(userIds).stream().collect(Collectors.toMap(User::getUserId, user -> user));
            for (Order order : records) {
                order.setUser(userMap.get(order.getUserId()));
            }
        }
    }

    @Override
    public void withDelivery(List<Order> records) {
        Set<Integer> orderIds = records.stream().map(Order::getOrderId).collect(Collectors.toSet());
        if (!orderIds.isEmpty()) {
            Map<Integer, List<DeliveryDoc>> deliveryDocMap = deliveryDocService.list(new QueryWrapper<DeliveryDoc>()
                    .in("order_id", orderIds))
                    .stream().collect(Collectors.groupingBy(DeliveryDoc::getOrderId));
            for (Order order : records) {
                List<DeliveryDoc> deliveryDocs = deliveryDocMap.get(order.getOrderId());
                if (deliveryDocs != null && !deliveryDocs.isEmpty()) {
                    order.setDeliveryDoc(deliveryDocs.get(deliveryDocs.size() - 1));
                }
            }
        }
    }


    @Override
    public List<Order> listOrderPrint(Set<Integer> orderIds) {
        List<Order> orderList = list(new QueryWrapper<Order>().in("order_id", orderIds));
        withRegions(orderList);
        withOrderGoods(orderList);
        withDelivery(orderList);
        return orderList;
    }

    @Override
    public void withRegions(List<Order> records) {
        Set<Integer> ids = records.stream().map(Order::getProvince).collect(Collectors.toSet());
        ids.addAll(records.stream().map(Order::getCity).collect(Collectors.toSet()));
        ids.addAll(records.stream().map(Order::getDistrict).collect(Collectors.toSet()));
        if (!ids.isEmpty()) {
            Map<Integer, String> regionMap = mallService.regions(ids)
                    .stream().collect(Collectors.toMap(Region::getId, Region::getName));
            for (Order order : records) {
                order.setProvinceName(regionMap.get(order.getProvince()));
                order.setCityName(regionMap.get(order.getCity()));
                order.setDistrictName(regionMap.get(order.getDistrict()));
            }
        }
    }

    @Override
    public void withExcelRegions(List<OrderExcel> orderExcelList) {
        if (orderExcelList.size() > 0) {
            Set<Integer> ids = new HashSet<>();
            for (OrderExcel orderExcel : orderExcelList) {
                ids.add(orderExcel.getProvince());
                ids.add(orderExcel.getCity());
                ids.add(orderExcel.getDistrict());
                ids.add(orderExcel.getTwon());
            }
            if (!ids.isEmpty()) {
                Map<Integer, String> regionMap = mallService.regions(ids)
                        .stream().collect(Collectors.toMap(Region::getId, Region::getName));
                for (OrderExcel orderExcel : orderExcelList) {
                    orderExcel.setShippingAddress(regionMap.getOrDefault(orderExcel.getProvince(), "--") + " "
                            + regionMap.getOrDefault(orderExcel.getCity(), "--") + " "
                            + regionMap.getOrDefault(orderExcel.getDistrict(), "--") + " "
                            + regionMap.getOrDefault(orderExcel.getTwon(), "--") + " " + orderExcel.getAddress());
                }
            }
        }
    }

    @Override
    public void batchDelivery(BatchDelivery batchDelivery) {
        for (Order order : batchDelivery.getOrders()) {
            if (order.getPromType() == 5) {
                throw new ShopException(ResultEnum.ERROR_ORDER);
            }
            DeliveryDoc deliveryDoc = new DeliveryDoc();
            deliveryDoc.setOrderId(order.getOrderId());
            deliveryDoc.setShippingName(batchDelivery.getShippingName());
            deliveryDoc.setShippingCode(batchDelivery.getShippingCode());
            deliveryDoc.setInvoiceNo(order.getInvoiceNo());
            deliveryDoc.setNote(batchDelivery.getNote());
            deliveryDoc.setOrderSn(order.getOrderSn());
            deliveryDoc.setZipcode(order.getZipcode());
            deliveryDoc.setUserId(order.getUserId());
            deliveryDoc.setAdminId(batchDelivery.getSellerId());
            deliveryDoc.setConsignee(order.getConsignee());
            deliveryDoc.setMobile(order.getMobile());
            deliveryDoc.setCountry(order.getCountry());
            deliveryDoc.setProvince(order.getProvince());
            deliveryDoc.setCity(order.getCity());
            deliveryDoc.setDistrict(order.getDistrict());
            deliveryDoc.setAddress(order.getAddress());
            deliveryDoc.setShippingPrice(order.getShippingPrice());
            deliveryDoc.setCreateTime(System.currentTimeMillis() / 1000);
            deliveryDoc.setStoreId(batchDelivery.getStoreId());

            if (order.getShippingStatus() == 1) {
                //修改订单发货信息
                order.setShippingCode(batchDelivery.getShippingCode());
                order.setShippingName(batchDelivery.getShippingName());
                updateById(order);
                //改变售后的信息
                deliveryDocService.update(new UpdateWrapper<DeliveryDoc>()
                        .set("shipping_code", batchDelivery.getShippingCode())
                        .set("shipping_name", batchDelivery.getShippingName())
                        .set("invoice_no", order.getInvoiceNo())
                        .eq("order_id", order.getOrderId()));
                //操作日志
                orderActionService.addOrderActionLog(order, "订单修改发货信息",
                        batchDelivery.getNote(), batchDelivery.getSellerId(), 1, batchDelivery.getStoreId());
                return;
            } else {
                StoreAddress storeAddress;
                if (batchDelivery.getStoreAddressId() != null) {
                    storeAddress = sellerService.storeAddress(batchDelivery.getStoreAddressId());
                } else {
                    storeAddress = sellerService.storeAddressDefault(batchDelivery.getStoreId(), 0);//默认发货地址
                }
                if (storeAddress != null) {
                    deliveryDoc.setStoreAddressConsignee(storeAddress.getConsignee());
                    deliveryDoc.setStoreAddressMobile(storeAddress.getMobile());
                    deliveryDoc.setStoreAddressProvinceId(storeAddress.getProvinceId());
                    deliveryDoc.setStoreAddressCityId(storeAddress.getCityId());
                    deliveryDoc.setStoreAddressDistrictId(storeAddress.getDistrictId());
                    deliveryDoc.setStoreAddress(storeAddress.getAddress());
                }
                deliveryDocService.save(deliveryDoc);

                int deliveryCount = 0;//订单发货商品数量
                for (OrderGoods orderGoods : order.getOrderGoods()) {
                    if (orderGoods.getIsSend() == 1) {
                        deliveryCount++;
                    } else if (orderGoods.getIsSend() == 0 && orderGoods.getChecked()) {
                        orderGoods.setIsSend(1);
                        orderGoods.setDeliveryId(deliveryDoc.getId());
                        orderGoodsService.updateById(orderGoods);//改变订单商品发货状态
                        deliveryCount++;
                    }
                }
                order.setShippingTime(System.currentTimeMillis() / 1000);
                order.setShippingCode(batchDelivery.getShippingCode());
                order.setShippingName(batchDelivery.getShippingName());
                if (deliveryCount == order.getOrderGoodsCount()) {
                    order.setShippingStatus(1);
                } else {
                    order.setShippingStatus(2);
                }
                updateById(order);//改变订单状态
            }
            //通知发货消息

            //商家发货，发送短信给客户

            //发送微信模板消息通知

            //操作日志
            orderActionService.addOrderActionLog(order, "订单发货", batchDelivery.getNote(),
                    batchDelivery.getSellerId(), 1, batchDelivery.getStoreId());
        }
    }

    @Override
    public void invalid(Order order, Seller seller) {
        order.setOrderStatus(OrderConstant.INVALID);
        updateById(order);
        OrderAction orderAction = orderLog(order, seller);
        orderAction.setStatusDesc("无效");
        orderActionService.save(orderAction);
    }

    private OrderAction orderLog(Order order, Seller seller) {
        OrderAction orderAction = new OrderAction();
        orderAction.setOrderId(order.getOrderId());
        orderAction.setActionUser(seller.getSellerId());
        orderAction.setUserType(OrderConstant.SELLER);
        orderAction.setOrderStatus(order.getOrderStatus());
        orderAction.setPayStatus(order.getPayStatus());
        orderAction.setShippingStatus(order.getShippingStatus());
        orderAction.setLogTime(System.currentTimeMillis() / 1000);
        orderAction.setStoreId(seller.getStoreId());
        return orderAction;
    }

    @Override
    public void confirm(Order order, Seller seller) {
        order.setOrderStatus(OrderConstant.CONFIRMED);
        this.update((new UpdateWrapper<Order>()).set("order_status",
                order.getOrderStatus()).eq("order_id", order.getOrderId()));
        OrderAction orderAction = orderLog(order, seller);
        orderAction.setStatusDesc("确认");
        orderActionService.save(orderAction);
    }

    @Override
    public void cancelConfirm(Order order, Seller seller) {
        order.setOrderStatus(OrderConstant.NOT_CONFIRMED);
        this.updateById(order);
        OrderAction orderAction = orderLog(order, seller);
        orderAction.setStatusDesc("取消确认");
        orderActionService.save(orderAction);
    }

    @Override
    public Order deliveryInfo(Integer orderId) {
        Order order = this.getById(orderId);
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        this.withOrderGoods(orders);
        this.withRegions(orders);
        List<ReturnGoods> returnGoodsList = returnGoodsService.getReturnGoodsByOrderId(orderId);
        Set<Integer> recIds = returnGoodsList.stream().map(ReturnGoods::getRecId).collect(Collectors.toSet());
        order.setUnsend(returnGoodsList.isEmpty() ? 0 : 1);//订单是否申请了退换货
        for (OrderGoods og : order.getOrderGoods()) {
            og.setUnsend(recIds.contains(og.getRecId()) ? 1 : 0);//订单商品是否申请了退换货
        }
        return order;
    }

    @Override
    public List<OrderDayReport> getOrderDayReportList(QueryWrapper queryWrapper, Long startTime, Long endTime) {
        return ((OrderMapper) baseMapper).getOrderDayReportList(queryWrapper, startTime, endTime, this.getDayLimit(startTime, endTime));
    }

    @Override
    public List<OrderDayReport> getOrderDayFinanceList(QueryWrapper<Order> orderQueryWrapper, Long startTime, Long endTime) {
        return ((OrderMapper) baseMapper).getOrderDayFinanceList(orderQueryWrapper, startTime, endTime, this.getDayLimit(startTime, endTime));
    }

    private int getDayLimit(Long startTime, Long endTime) {
        int dayLimit = (int) Math.ceil((double) (endTime - startTime) / 86400);
        if (dayLimit == 1 && !DateUtil.isSameDay(new Date(startTime * 1000), new Date(endTime * 1000))) {
            dayLimit++;
        }
        return dayLimit;
    }

    @Override
    public void setWrapperByType(QueryWrapper<Order> orderQueryWrapper, String type, Integer promType, Integer userId) {
        if (!StringUtils.isEmpty(type)) {
            if ("WAITPAY".equals(type)) {
                orderQueryWrapper.in("pay_status", 0, 2).eq("order_status", 0);
            }
            if ("WAITTEAM".equals(type)) {
                orderQueryWrapper.eq("pay_status", 1).ne("shipping_status", 1).eq("order_status", 0);
            }
            if ("WAITSEND".equals(type)) {
                orderQueryWrapper.eq("pay_status", 1).ne("shipping_status", 1);
                if (promType != null && promType == 6) {
                    orderQueryWrapper.eq("order_status", 1);
                } else {
                    orderQueryWrapper.in("order_status", 0, 1);
                }
            }
            if ("WAITRECEIVE".equals(type)) {
                orderQueryWrapper.eq("shipping_status", 1).eq("order_status", 1);
            }
            if ("WAITCCOMMENT".equals(type)) {
                orderQueryWrapper.eq("order_status", 2);
            }
            if ("CANCEL".equals(type)) {
                orderQueryWrapper.eq("order_status", 3);
            }
            if ("FINISH".equals(type)) {
                orderQueryWrapper.eq("order_status", 4);
            }
            if ("CANCELLED".equals(type)) {
                orderQueryWrapper.eq("order_status", 5);
            }
            if ("COMPLAIN".equals(type)) {//可投诉订单
                orderQueryWrapper.in("order_status", 2, 4);
                //查找申请过的订单ID
                Set<Integer> orderIds = complainService.list(new QueryWrapper<Complain>().select("order_id").eq("user_id", userId))
                        .stream().map(Complain::getOrderId).collect(Collectors.toSet());
                //申请过的订单不显示
                if (orderIds.size() > 0) {
                    orderQueryWrapper.notIn("order_id", orderIds);
                }
            }
        }
    }

    @Override
    public IPage<SalesRanking> getSalesRankingPage(Page<SalesRanking> page, QueryWrapper<OrderGoods> queryWrapper) {
        return ((OrderMapper) baseMapper).getSalesRankingPage(page, queryWrapper);
    }

    @Override
    public IPage<StoreRanking> getStoreRankingPage(Page<StoreRanking> page, QueryWrapper<Order> queryWrapper) {
        return ((OrderMapper) baseMapper).getStoreRankingPage(page, queryWrapper);
    }

    @Override
    public IPage<SaleDayDetails> getSaleDayDetailsPage(Page<SaleDayDetails> page, QueryWrapper<Order> queryWrapper) {
        return ((OrderMapper) baseMapper).getSaleDayDetailsPage(page, queryWrapper);
    }

    @Override
    public List<OrderExcel> getOrderExportData(QueryWrapper<Order> wrapper) {
        return ((OrderMapper) baseMapper).selectOrderExportData(wrapper);
    }

    @Override
    public void schedule() {
        List<Object> fList = redisUtil.lGet("team_follow", 0, -1);
        if (fList.size() > 0) {
            String teamOrderLimitTimeStr = (String) mallService.config().get("shopping_team_order_limit_time");
            Long now = System.currentTimeMillis() / 1000;
            int limitTime = 1800;
            if (!StringUtils.isEmpty(teamOrderLimitTimeStr)) {
                limitTime = Integer.parseInt(teamOrderLimitTimeStr);
            }
            Set<Integer> orderIds = new HashSet<>();
            for (Object o : fList) {
                TeamFollow teamFollow = (TeamFollow) o;
                if (now - teamFollow.getFollowTime() > limitTime) {
                    //时间到了
                    orderIds.add(teamFollow.getOrderId());
                    redisUtil.lRemove("team_follow", 1, o);
                }
            }
            if (orderIds.size() > 0) {
                List<Order> orders = list(new QueryWrapper<Order>().in("order_id", orderIds));
                for (Order order : orders) {
                    order.setOrderStatus(OrderConstant.CANCELLED);
                    order.setUserNote("用户超时" + limitTime + "秒未支付，取消订单");
                }
                updateBatchById(orders);
                withOrderGoods(orders);
                rabbitTemplate.convertAndSend("cancelled_order", "", orders);
            }
        }
    }

    @Override
    public List<UserOrderStatistics> userOrderStatisticsList(QueryWrapper<Order> queryWrapper) {
        return ((OrderMapper) baseMapper).selectUserOrderStatisticsList(queryWrapper);
    }

    @Override
    public void withStore(List<Order> records) {
        if (records.size() > 0) {
            Set<Integer> storeIds = records.stream().map(Order::getStoreId).collect(Collectors.toSet());
            Map<Integer, Store> storeMap = sellerService.getStoresById(storeIds).stream().collect(Collectors.toMap(Store::getStoreId, store -> store));
            records.forEach(order -> {
                Integer storeId = order.getStoreId();
                if (null != storeId) {
                    if (null != storeMap.get(storeId)) {
                        order.setStoreName(storeMap.get(storeId).getStoreName());
                    }
                }
            });
        }
    }

    @Override
    public void withRankingStore(List<StoreRanking> records) {
        if (records.size() > 0) {
            Set<Integer> storeIds = records.stream().map(StoreRanking::getStoreId).collect(Collectors.toSet());
            Map<Integer, Store> storeMap = sellerService.getStoresById(storeIds).stream().collect(Collectors.toMap(Store::getStoreId, store -> store));
            records.forEach(order -> {
                order.setStoreName(storeMap.get(order.getStoreId()).getStoreName());
            });
        }
    }

    @Override
    public List<UserOrderSum> getUserOrderSumByUserIds(Set<Integer> userIds) {
        return ((OrderMapper) baseMapper).selectUserOrderSumByUserIds(userIds);
    }

    @Override
    public void modify(Seller seller, Order order, Order editOrder) {
        if (order == null) {
            throw new ShopException(ResultEnum.NO_FIND_ORDER);
        }
        if (!order.getIsAbleModify()) {
            throw new ShopException(ResultEnum.ORDER_NOT_CANCEL_CONFIRM);
        }
        BigDecimal exOrderGoodsPrice = order.getTotalAmount().subtract(order.getShippingPrice());
        //检测修改的价格是否低于需要分销和平台提成的金额
        List<OrderGoods> orderGoodsList = orderGoodsService.list((new QueryWrapper<OrderGoods>()).eq("order_id", order.getOrderId()));
        BigDecimal orderDistributionMoney = BigDecimal.ZERO;
        BigDecimal shoppingPointRate = new BigDecimal((String) mallService.config().get("shopping_point_rate"));
        MathContext mc = new MathContext(3, RoundingMode.HALF_UP);
        for (OrderGoods orderGoods : orderGoodsList) {
            //此商品金额初始值
            BigDecimal tempDistributionMoney = orderGoods.getMemberGoodsPrice().multiply(BigDecimal.valueOf(orderGoods.getGoodsNum()));
            //减去购买该商品赠送的积分金额
            if (orderGoods.getGiveIntegral() > 0 && orderGoods.getIsSend() < 3) {
                tempDistributionMoney = tempDistributionMoney.subtract(((new BigDecimal(orderGoods.getGiveIntegral()))
                        .divide(shoppingPointRate, mc).multiply(BigDecimal.valueOf(orderGoods.getGoodsNum()))));
            }
            //去掉商品优惠的价格  减去优惠券抵扣金额和优惠折扣
            if (order.getOrderPromAmount().compareTo(BigDecimal.ZERO) > 0 || order.getCouponPrice().compareTo(BigDecimal.ZERO) > 0) {
                tempDistributionMoney = tempDistributionMoney.subtract(order.getOrderPromAmount()).subtract(order.getCouponPrice());
            }
            tempDistributionMoney = tempDistributionMoney.multiply(BigDecimal.valueOf(orderGoods.getCommission())).divide((new BigDecimal(100)), mc);
            if (orderGoods.getDistribut().compareTo(BigDecimal.ZERO) > 0) {
                tempDistributionMoney = tempDistributionMoney.add(orderGoods.getDistribut());
            }
            orderDistributionMoney = orderDistributionMoney.add(tempDistributionMoney);
        }
        orderDistributionMoney = orderDistributionMoney.add(editOrder.getShippingPrice());
        if (editOrder.getShippingPrice().compareTo(order.getShippingPrice()) != 0) {
            BigDecimal differenceShippingPrice = editOrder.getShippingPrice().subtract(order.getShippingPrice());
            order.setShippingPrice(editOrder.getShippingPrice());
            order.setOrderAmount(order.getOrderAmount().add(differenceShippingPrice));
            order.setTotalAmount(order.getTotalAmount().add(differenceShippingPrice));
        }
        if (editOrder.getDiscount().compareTo(order.getDiscount()) != 0) {
            BigDecimal differenceDiscount = editOrder.getDiscount().subtract(order.getDiscount());
            order.setDiscount(editOrder.getDiscount());
            order.setOrderAmount(order.getOrderAmount().add(differenceDiscount));
            order.setTotalAmount(order.getTotalAmount().add(differenceDiscount));
        }
        if (order.getOrderAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ShopException(ResultEnum.MODIFY_ORDER_AMOUNT_NOT_ZERO);
        }
        BigDecimal lastMoney = order.getOrderAmount().subtract(orderDistributionMoney);
        if (lastMoney.compareTo(BigDecimal.ZERO) < 0) {
            throw new ShopException(ResultEnum.MODIFY_DISTRIBUTION_NOT_ENOUGH.getCode(),
                    ResultEnum.MODIFY_DISTRIBUTION_NOT_ENOUGH.getMsg() + ",应大于" + lastMoney);
        }
        updateById(order);
        BigDecimal newOrderGoodsPrice = order.getTotalAmount().subtract(order.getShippingPrice());
        if (exOrderGoodsPrice.compareTo(newOrderGoodsPrice) != 0) {
            //修改订单价才用改订单商品表
            List<OrderGoods> updateOrderGoodsList = new ArrayList<>();
            for (OrderGoods orderGoods : orderGoodsList) {
                OrderGoods updateOrderGoods = new OrderGoods();
                updateOrderGoods.setRecId(orderGoods.getRecId());
                updateOrderGoods.setFinalPrice(orderGoods.getFinalPrice().divide(exOrderGoodsPrice, mc).multiply(newOrderGoodsPrice));
                updateOrderGoodsList.add(updateOrderGoods);
            }
            orderGoodsService.updateBatchById(updateOrderGoodsList);
        }
    }

    @Override
    public void cancelPay(Order order, OrderAction orderAction, Integer refundType) {
        order.setOrderStatus(OrderConstant.CANCELLED);
        order.setPayStatus(OrderConstant.REFUND);
        order.setAdminNote(orderAction.getActionNote());
        updateById(order);
        orderAction.setOrderId(order.getOrderStatus());
        orderAction.setOrderStatus(order.getOrderStatus());
        orderAction.setPayStatus(order.getPayStatus());
        orderAction.setShippingStatus(order.getShippingStatus());
        orderAction.setLogTime(System.currentTimeMillis() / 1000);
        orderAction.setStatusDesc("订单退款");
        orderActionService.save(orderAction);
        if (refundType == 0) {
            //用余额退回
            AccountLog accountLog = new AccountLog();
            accountLog.setUserId(order.getUserId());
            accountLog.setUserMoney(null == orderAction.getAmount() ? order.getOrderAmount() : orderAction.getAmount());
            accountLog.setPayPoints(0);
            accountLog.setFrozenMoney(BigDecimal.valueOf(0.00));
            accountLog.setDesc("退款到用户余额");
            userService.addAccountLog(accountLog);
            if (order.getUserMoney().compareTo(BigDecimal.ZERO) > 0 || order.getIntegral() > 0) {
                AccountLog payOtherAccountLog = new AccountLog();
                payOtherAccountLog.setUserId(order.getUserId());
                payOtherAccountLog.setUserMoney(order.getUserMoney());
                payOtherAccountLog.setPayPoints(order.getIntegral());
                payOtherAccountLog.setFrozenMoney(BigDecimal.valueOf(0.00));
                payOtherAccountLog.setDesc("订单取消退款");
                userService.addAccountLog(accountLog);
            }
        }
        if (refundType == 1) {
            Order totalOrder = getOne(new QueryWrapper<Order>().select("sum(order_amount) as order_amount")
                    .eq("transaction_id", order.getTransactionId()));
            wxPayService.refund(order, totalOrder.getOrderAmount().doubleValue(), orderAction.getActionNote());
            //支付宝还没有对接
        }
    }

    @Override
    public Order getOrder(Integer orderId) {
        Order order = this.getById(orderId);
        List<OrderGoods> orderGoodsList = orderGoodsService.list(new QueryWrapper<OrderGoods>().eq("order_id", orderId));
        order.setOrderGoods(orderGoodsList);
        return order;
    }

    /**
     * 获取用户的订单+ 提货订单
     *
     * @param
     * @return
     */
    @Override
    public IPage<OrderAndPickOrderVo> getOrderAndPickOrderPage(Integer page, Integer size, User user) {
        IPage<OrderAndPickOrderVo> orderAndPickOrderPage = this.baseMapper.getOrderAndPickOrderPage(new Page<>(page, size), user.getUserId());
        orderAndPickOrderPage.getRecords().forEach(orderAndPickOrderVo -> {
            // 获取商品图片
            Goods goods = mallService.goods(orderAndPickOrderVo.getGoodsId());
            orderAndPickOrderVo.setOriginalImg(goods.getOriginalImg());
            orderAndPickOrderVo.getOrderStatusDsc(orderAndPickOrderVo.getOrderStatus(), orderAndPickOrderVo.getPickOrderStatus());
            // 获取店铺
            Store store = null;
            if (orderAndPickOrderVo.getPickOrderStatus() == null) {
                store = sellerService.store(goods.getStoreId());
            } else {
                store = sellerService.getStoresByUserId(goods.getUserId());
            }
            orderAndPickOrderVo.setStoreName(store.getStoreName());
            orderAndPickOrderVo.setStoreLog(store.getStoreLogo());
            if (pickOrderService.getOne(new QueryWrapper<PickOrder>().eq("order_id", orderAndPickOrderVo.getOrderId())) == null) {
                orderAndPickOrderVo.setCancel(true);
            }
        });
        return orderAndPickOrderPage;
    }

}
