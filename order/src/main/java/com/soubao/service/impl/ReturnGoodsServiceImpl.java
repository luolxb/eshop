package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.dao.ReturnGoodsMapper;
import com.soubao.service.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-20
 */
@Service("returnGoodsService")
public class ReturnGoodsServiceImpl extends ServiceImpl<ReturnGoodsMapper, ReturnGoods> implements ReturnGoodsService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private UserService userService;
    @Autowired
    private MallService mallService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private AdminService adminService;

    @Override
    public int addReturnGoods(OrderGoods orderGoods, ReturnGoods returnGoods) {
        if (orderGoods == null) {
            throw new ShopException(ResultEnum.NO_FIND_ORDER);
        }
        ReturnGoods exReturnGoods = this.getOne((new QueryWrapper<ReturnGoods>()).eq("rec_id", returnGoods.getRecId()).last("limit 1"));
        if (exReturnGoods != null) {
            throw new ShopException(ResultEnum.ORDER_GOODS_ON_RETURN);
        }
        Order order = orderService.getById(orderGoods.getOrderId());
        returnGoods.setOrderId(orderGoods.getOrderId());
        returnGoods.setOrderSn(order.getOrderSn());
        returnGoods.setGoodsId(orderGoods.getGoodsId());
        returnGoods.setStoreId(orderGoods.getStoreId());
        returnGoods.setSpecKey(orderGoods.getSpecKey());
        returnGoods.setAddtime(System.currentTimeMillis() / 1000);
        if (returnGoods.getType() == 0 || returnGoods.getType() == 1) {
            //要退的总价 商品购买单价*申请数量
            BigDecimal useApplyReturnMoney = orderGoods.getFinalPrice().multiply(BigDecimal.valueOf(orderGoods.getGoodsNum()));
            //订单实际，用户实际使用金额
            BigDecimal userExpenditureMoney = order.getGoodsPrice().subtract(order.getOrderPromAmount()).subtract(order.getCouponPrice());
            MathContext mc = new MathContext(3, RoundingMode.HALF_UP);
            BigDecimal rate = useApplyReturnMoney.divide(userExpenditureMoney, mc);
            returnGoods.setRefundIntegral(rate.multiply(BigDecimal.valueOf(order.getIntegral()))
                    .setScale(0, BigDecimal.ROUND_DOWN).intValue());
            //积分抵了多少钱，要扣掉
            int integralDeductionMoney = returnGoods.getRefundIntegral() / Integer.parseInt((String) mallService.config().get("shopping_point_rate"));
            if (order.getOrderAmount().compareTo(BigDecimal.ZERO) > 0) {
                //三方支付总额，预售要退定金
                BigDecimal orderAmount = order.getOrderAmount().add(order.getPaidMoney());
                if (orderAmount.compareTo(order.getShippingPrice()) > 0) {
                    returnGoods.setRefundMoney(orderAmount.subtract(order.getShippingPrice()).multiply(rate)
                            .setScale(2, BigDecimal.ROUND_DOWN));//退款金额
                    returnGoods.setRefundDeposit(order.getUserMoney().multiply(rate).setScale(2, BigDecimal.ROUND_DOWN));
                } else {
                    returnGoods.setRefundDeposit(order.getUserMoney().subtract(order.getShippingPrice())
                            .add(order.getPaidMoney()).multiply(rate).subtract(BigDecimal.valueOf(integralDeductionMoney))
                            .setScale(2, BigDecimal.ROUND_DOWN));
                }
            } else {
                returnGoods.setRefundDeposit(useApplyReturnMoney.subtract(BigDecimal.valueOf(integralDeductionMoney))
                        .setScale(2, BigDecimal.ROUND_DOWN));//该退余额支付部分
            }
            if (!this.save(returnGoods)) throw new ShopException(ResultEnum.UNKNOWN_ERROR);
            //发送消息
        }
        return returnGoods.getRecId();
    }

    @Override
    public List<ReturnGoods> getReturnGoodsByOrderId(Integer orderId) {
        return this.list(new QueryWrapper<ReturnGoods>().eq("order_id", orderId));
    }

    /**
     * 4:根据账单退用户余额，用户积分
     * @param returnGoods
     */
    @Override
    public void refund(ReturnGoods returnGoods) {
        //追回下单赠送的积分,追回下单赠送的优惠券
        OrderGoods orderGoods = orderGoodsService.getById(returnGoods.getRecId());
        if (returnGoods.getIsReceive() == 1) {
            if (orderGoods.getGiveIntegral() > 0) {
                User user = userService.getById(returnGoods.getUserId());
                if (orderGoods.getGiveIntegral() > user.getPayPoints()) {
                    //积分不够则从退款金额里扣
                    BigDecimal refundMoney = BigDecimal.valueOf(orderGoods.getGiveIntegral()).divide(new BigDecimal((String) mallService.config().get("shopping_point_rate")), 3, BigDecimal.ROUND_UP);
                    returnGoods.setRefundMoney(returnGoods.getRefundMoney().subtract(refundMoney));//这时候的值可能是负数
                }else{
                    AccountLog accountLog = new AccountLog();
                    accountLog.setUserId(returnGoods.getUserId());
                    accountLog.setUserMoney(BigDecimal.ZERO);
                    accountLog.setPayPoints(orderGoods.getGiveIntegral());
                    accountLog.setFrozenMoney(BigDecimal.ZERO);
                    accountLog.setDesc("退货积分追回");
                    userService.addAccountLog(accountLog);
                }
            }
            CouponList couponList = mallService.getUserCoupon(returnGoods.getUserId(), orderGoods.getOrderId());
            if (couponList != null) {
                if (couponList.getStatus() == 1) {
                    //如果优惠券被使用,那么从退款里扣
                    Coupon coupon = mallService.getCouponById(couponList.getCid());
                    returnGoods.setRefundMoney(returnGoods.getRefundMoney().subtract(coupon.getMoney()));//这时候的值可能是负数
                } else {
                    //优惠券追回
                    mallService.cancelUserCoupon(couponList.getId());
                }
            }
        }
        //退款金额为负数，匀到要退的用户金额
        if (returnGoods.getRefundMoney().compareTo(BigDecimal.ZERO) < 0) {
            returnGoods.setRefundDeposit(returnGoods.getRefundDeposit().add(returnGoods.getRefundMoney()));
            returnGoods.setRefundMoney(BigDecimal.ZERO);
        }
        returnGoods.setRefundTime(System.currentTimeMillis() / 1000);
        returnGoods.setStatus(5);
        this.updateById(returnGoods); //更新新的退款账单
        //退还积分和用户金额
        if (returnGoods.getRefundDeposit().compareTo(BigDecimal.ZERO) > 0 || returnGoods.getRefundIntegral() > 0) {
            AccountLog accountLog = new AccountLog();
            accountLog.setUserId(returnGoods.getUserId());
            accountLog.setUserMoney(returnGoods.getRefundDeposit());
            accountLog.setPayPoints(returnGoods.getRefundIntegral());
            accountLog.setFrozenMoney(BigDecimal.ZERO);
            accountLog.setDesc("用户申请商品退款");
            userService.addAccountLog(accountLog);
        }
        Order order = orderService.getById(returnGoods.getOrderId());
        //退还在线支付金额
        if (returnGoods.getRefundMoney().compareTo(BigDecimal.ZERO) > 0) {
            if (returnGoods.getRefundType() == 0) {
                //原路退回
                List<String> payCodeArr = Arrays.asList("weixin", "miniAppPay", "appWeixinPay");
                if (payCodeArr.contains(order.getPayCode())) {
                    wxPayService.refund(order, returnGoods.getRefundMoney().doubleValue(), "售后退款");
                } else {
                    throw new ShopException(0, "该订单支付方式,不支持在线退回.code:" + order.getPayCode());
                }
            } else {
                //退到用户余额
                AccountLog accountLog = new AccountLog();
                accountLog.setUserId(returnGoods.getUserId());
                accountLog.setUserMoney(returnGoods.getRefundMoney());
                accountLog.setPayPoints(0);
                accountLog.setFrozenMoney(BigDecimal.ZERO);
                accountLog.setDesc("用户申请商品退款");
                userService.addAccountLog(accountLog);
            }
        }
        int returnGoodsCount = this.count((new QueryWrapper<ReturnGoods>())
                .eq("order_id", order.getOrderId()).eq("status", 5));
        int orderGoodsCount = orderGoodsService.count((new QueryWrapper<OrderGoods>()).eq("order_id", order.getOrderId()));
        order.setPayStatus(3);
        if(returnGoodsCount == orderGoodsCount){
            order.setPayStatus(5);
            //返回下单使用的优惠券
            mallService.returnUserCoupon(order.getUserId(), order.getOrderId());
        }
        orderService.updateById(order);//更新订单状态
        orderGoods.setIsSend(3);//更新订单商品状态
        orderGoodsService.updateById(orderGoods);
        List<OrderGoods> orderGoodsList = new ArrayList<>();
        orderGoodsList.add(orderGoods);
        order.setOrderGoods(orderGoodsList);
        rabbitTemplate.convertAndSend("refund_order.mall", order);//库存扣减丢给shop服务,消息通知什么的丢给mq

        ExpenseLog expenseLog = new ExpenseLog();
        expenseLog.setAdminId(1);
        expenseLog.setMoney(returnGoods.getRefundMoney().add(returnGoods.getRefundDeposit()));
        expenseLog.setIntegral(returnGoods.getRefundIntegral());
        expenseLog.setLogTypeId(returnGoods.getRecId());
        expenseLog.setType(3);
        expenseLog.setUserId(returnGoods.getUserId());
        expenseLog.setStoreId(returnGoods.getStoreId());
        adminService.addExpenseLog(expenseLog);
    }

    @Override
    public void withUser(List<ReturnGoods> records) {
        if (records.size() > 0) {
            Set<Integer> userIds = records.stream().map(ReturnGoods::getUserId).collect(Collectors.toSet());
            Map<Integer, User> userMap = userService.usersByIds(userIds).stream().collect(Collectors.toMap(User::getUserId, user -> user));
            for (ReturnGoods returnGoods : records) {
                returnGoods.setUser(userMap.get(returnGoods.getUserId()));
            }
        }
    }

    @Override
    public void withStore(List<ReturnGoods> records) {
        if (records.size() > 0) {
            Set<Integer> storeIds = records.stream().map(ReturnGoods::getStoreId).collect(Collectors.toSet());
            Map<Integer, Store> storeMap = sellerService.getStoresById(storeIds).stream().collect(Collectors.toMap(Store::getStoreId, store -> store));
            for (ReturnGoods returnGoods : records) {
                returnGoods.setStore(storeMap.get(returnGoods.getStoreId()));
            }
        }
    }

    @Override
    public void withOrderGoods(List<ReturnGoods> records) {
        if (records.size() > 0) {
            Set<Integer> recIds = records.stream().map(ReturnGoods::getRecId).collect(Collectors.toSet());
            Map<Integer, OrderGoods> orderGoodsMap = orderGoodsService.listByIds(recIds).stream().collect(Collectors.toMap(OrderGoods::getRecId, orderGoods -> orderGoods));
            for (ReturnGoods returnGoods : records) {
                returnGoods.setOrderGoods(orderGoodsMap.get(returnGoods.getRecId()));
            }
        }
    }

}
