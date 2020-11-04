package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.soubao.common.constant.OrderConstant;
import com.soubao.common.constant.WxTradeType;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.dto.WxPayOrderDto;
import com.soubao.entity.*;
import com.soubao.service.*;
import com.soubao.common.utils.IdUtil;
import com.soubao.common.utils.ShopStringUtil;
import com.soubao.common.utils.WxPayAppConfig;

import com.soubao.utils.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("wxPayService")
@Slf4j
public class WxPayServiceImpl implements WxPayService {
    @Autowired
    private WxPayAppConfig wxPayAppConfig;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private MallService mallService;
    @Autowired
    private MiniappConfigService miniappConfigService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Override
    public Map<String, String> unifiedOrder(WxPayOrderDto wxPayOrder) {
        if (!WxTradeType.getSet().contains(wxPayOrder.getTradeType())) {
            throw new ShopException(ResultEnum.NO_SUPPORT_TRADE_TYPE);
        }
        Map<String, String> returnMap = new HashMap<>();
        Map<String, String> responseMap = new HashMap<>();
        Map<String, String> requestMap = new HashMap<>();
        MiniappConfig miniappConfig = miniappConfigService.getOne((new QueryWrapper<MiniappConfig>()).last("limit 1"));
        try {
            wxPayAppConfig.setAppID(miniappConfig.getAppId().trim());
            wxPayAppConfig.setMchID(miniappConfig.getMchId().trim());
            wxPayAppConfig.setKey(miniappConfig.getKey().trim());
            wxPayAppConfig.setPayNotifyUrl("http://dede.ntscic.cn/order/weixin/notify");
            WXPay wxpay = new WXPay(wxPayAppConfig);
            requestMap.put("body", wxPayOrder.getBody());
            requestMap.put("notify_url", wxPayAppConfig.getPayNotifyUrl());   // 接收微信支付异步通知回调地址
//            requestMap.put("openid", wxPayOrder.getOpenid());  //trade_type=JSAPI时（即JSAPI支
            requestMap.put("out_trade_no", wxPayOrder.getOrderSn());
            requestMap.put("sign_type", "MD5");
            requestMap.put("spbill_create_ip", wxPayOrder.getClientIp()); // 终端IP

//            int amount_= new Double(amount).intValue();
            requestMap.put("total_fee", (wxPayOrder.getAmount().multiply(new BigDecimal("100"))).intValue() + "");   // 总金额
            requestMap.put("trade_type", wxPayOrder.getTradeType());                              // App支付类型
            requestMap.put("sign", WXPayUtil.generateSignature(requestMap, miniappConfig.getKey(),WXPayConstants.SignType.MD5));
            log.info("微信支付请求参数：{}", requestMap);

            Map<String, String> resultMap = wxpay.unifiedOrder(requestMap);
            //获取返回码
            String returnCode = resultMap.get("return_code");
            String returnMsg = resultMap.get("return_msg");
            System.out.println("----signature result---returnCode:" + returnCode);
            System.out.println("----signature result---returnMsg:" + returnMsg);
            System.out.println("----signature result---resultMap:" + resultMap.toString());
            //若返回码为SUCCESS，则会返回一个result_code,再对该result_code进行判断
            if ("SUCCESS".equals(returnCode)) {
                String resultCode = resultMap.get("result_code");
                String errCodeDes = resultMap.get("err_code_des");
                if ("SUCCESS".equals(resultCode)) {
                    responseMap = resultMap;
                }
            }
            if (responseMap.isEmpty()) {
                throw new ShopException(ResultEnum.WX_PAY_PRE_ERROR);
            }
            // 3、签名生成算法
            Long time = System.currentTimeMillis() / 1000;
            String timestamp = time.toString();

            returnMap.put("appid", wxPayAppConfig.getAppID());
            returnMap.put("noncestr", responseMap.get("nonce_str"));
            returnMap.put("package", "Sign=WXPay");
            returnMap.put("timestamp", timestamp);
            returnMap.put("partnerid", wxPayAppConfig.getMchID());
            System.out.println("----prepay_id ---:" + responseMap.get("prepay_id"));
            returnMap.put("prepayid", responseMap.get("prepay_id"));
            returnMap.put("sign", WXPayUtil.generateSignature(returnMap, wxPayAppConfig.getKey()));//微信支付签名

            return returnMap;
        } catch (Exception e) {
            log.error("订单号：{}，错误信息：{}", wxPayOrder.getOrderSn(), e.getMessage());
            throw new ShopException(ResultEnum.WX_PAY_ERROR);
        }
    }


    @Override
    public String notify(String notifyStr) throws Exception {
        log.info("进入微信支付回调-====>{}", notifyStr);
        String xmlBack = "<xml>" +
                "<return_code><![CDATA[FAIL]]></return_code>" +
                "<return_msg><![CDATA[报文为空]]></return_msg>" +
                "</xml>";
        try {
            // 转换成map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(notifyStr);
//            WXPay wxpayApp = new WXPay(wxPayAppConfig);
            log.info("1 resultMap ===>{}",resultMap);
//            if (wxpayApp.isPayResultNotifySignatureValid(resultMap)) {
                String returnCode = resultMap.get("return_code");  //状态
                String outTradeNo = resultMap.get("out_trade_no");//商户订单号
                String transactionId = resultMap.get("transaction_id");
                if ("SUCCESS".equals(returnCode)) {
                    if (StringUtils.isNotBlank(outTradeNo)) {
                        /*
                         * 注意！！！
                         * 请根据业务流程，修改数据库订单支付状态，和其他数据的相应状态
                         *
                         */
                        List<Order> orderList = orderService.list((new QueryWrapper<Order>())
                                .eq("order_sn", outTradeNo).or().eq("master_order_sn", outTradeNo));
                        orderList.forEach(payOrder -> {
                            payOrder.setPayName("微信支付");
                            payOrder.setPayStatus(1);
//                            payOrder.setPayStatus(orderService.getPayStatusByPay(payOrder));
                            payOrder.setOrderStatus(1);
                            payOrder.setPayTime(System.currentTimeMillis() / 1000);
                            payOrder.setTransactionId(transactionId);

                            // 修改卖家余额 如果卖家是商家则修改商家店铺
                            User user = userService.getById(payOrder.getUserId());
                            UserSellerStore userSellerStore = sellerService.userSellerStore(user.getNickname(), user.getUserId(), user.getMobile());
                            OrderGoods orderGoods = orderGoodsService.getOne(new QueryWrapper<OrderGoods>().eq("order_id", payOrder.getOrderId()));
                            // 更新商品信息
                            mallService.updateSellStateGoods(orderGoods.getGoodsId(), payOrder.getUserId(), userSellerStore.getStoreId(), 0, payOrder.getGoodsPrice());
                            Goods goods = mallService.goods(orderGoods.getGoodsId());

                            User userTo = userService.getUserInfoById1(goods.getOwnerId());
                            if (null != userTo) {
                                BigDecimal amountTo = userTo.getUserMoney();
                                if (null == amountTo) {
                                    amountTo = new BigDecimal(0.00);
                                }
                                amountTo = amountTo.add(payOrder.getUserMoney());
                                userTo.setUserMoney(amountTo);
                                userService.userUpdate(userTo);
                            }

                            log.info("更新一下通证状态 ===>");
                            // 更新一下通证状态
                            sellerService.updateDepositCertificateStatus(payOrder.getUserId(), goods.getDcId(), null, 1);

                            Integer storeId = orderGoods.getStoreId();
                            // 根据商店ID 判断是否是商家id，如果是就操作店铺余额
                            log.info("根据商店ID 判断是否是商家id，如果是就操作店铺余额 ===>");

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

                        });
                        orderService.updateBatchById(orderList);
                        rabbitTemplate.convertAndSend("pay_order", "", orderList);
                        log.info("微信手机支付回调成功,订单号:{}", outTradeNo);
                        xmlBack = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
                    }
                }
//            }
        } catch (Exception e) {
            log.error("微信支付回调失败e", e);
            e.printStackTrace();
        }
        return xmlBack;
    }

    @Override
    public void refund(Order order, double amount, String refundReason) {
        Map<String, String> requestMap = new HashMap<>();
        if (StringUtils.isBlank(order.getTransactionId())) {
            throw new ShopException(ResultEnum.NO_FIND_ORDER);
        }
        if (amount <= 0) {
            throw new ShopException(ResultEnum.REFUND_AMOUNT_ERROR);
        }

        Map<String, String> responseMap = new HashMap<>();
        WXPay wxpay = new WXPay(wxPayAppConfig);
//      requestMap.put("out_trade_no", orderNo);//out_trade_no、transaction_id至少填一个
        requestMap.put("transaction_id", order.getTransactionId());
        requestMap.put("out_refund_no", String.valueOf(IdUtil.nextId()));
        Order sumOrder = orderService.getOne((new QueryWrapper<Order>()).select("sum(order_amount) as order_amount")
                .eq("transaction_id", order.getTransactionId()));
        requestMap.put("total_fee", String.valueOf(sumOrder.getOrderAmount().multiply(new BigDecimal(100))));//订单支付时的总金额，需要从数据库查
        requestMap.put("refund_fee", String.valueOf((int) (amount * 100)));//所需退款金额
        requestMap.put("refund_desc", refundReason);
        try {
            responseMap = wxpay.refund(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String return_code = responseMap.get("return_code");   //返回状态码
        String return_msg = responseMap.get("return_msg");     //返回信息
        if ("SUCCESS".equals(return_code)) {
            String result_code = responseMap.get("result_code");       //业务结果
            String err_code_des = responseMap.get("err_code_des");     //错误代码描述
            if ("SUCCESS".equals(result_code)) {
                //表示退款申请接受成功，结果通过退款查询接口查询
                //修改用户订单状态为退款申请中或已退款。退款异步通知根据需求，可选
                log.info("第三方平台交易流水号:{},退款申请成功", order.getTransactionId());
            } else {
                log.warn("第三方平台交易流水号:{}错误信息:{}", order.getTransactionId(), err_code_des);
                throw new ShopException(0, err_code_des);
            }
        } else {
            log.info("第三方平台交易流水号:{}错误信息:{}", order.getTransactionId(), return_msg);
            throw new ShopException(0, "第三方平台交易流水号:" + order.getTransactionId() + "错误信息:" + return_msg);
        }
    }
}
