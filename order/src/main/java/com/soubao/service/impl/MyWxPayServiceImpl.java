package com.soubao.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.soubao.common.vo.SBApi;
import com.soubao.config.NtsWxPayConfig;
import com.soubao.service.MyWxPayService;
import com.soubao.utils.QRCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class MyWxPayServiceImpl implements MyWxPayService {

    @Autowired
    private NtsWxPayConfig ntsWxPayConfig;

    @Autowired
    private QRCodeGenerator qrCodeGenerator;

//    @Value("${server.ip}")
//    private String serverIp;

    @Override
    public SBApi unifiedOrder(String orderNo, String trade_type, String body, String code) {
        Map<String, String> returnMap = new HashMap<>();
        Map<String, String> responseMap = new HashMap<>();
        Map<String, String> requestMap = new HashMap<>();
        try {
            WXPay wxpay = new WXPay(ntsWxPayConfig);
            // 商品描述
            requestMap.put("body", StringUtils.isBlank(body) ? "测试" : body);
            // 设备号 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
            requestMap.put("device_info", "web");
            // 随机字符串
            requestMap.put("nonce_str", WXPayUtil.generateNonceStr());
            // 商户订单号
            requestMap.put("out_trade_no", WXPayUtil.generateNonceStr());
            // 总金额
            requestMap.put("total_fee", String.valueOf(1));
            // 终端IP
            requestMap.put("spbill_create_ip", "47.113.105.209");

            // trade_type=JSAPI时（即JSAPI支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识
            if (StringUtils.equals("JSAPI", trade_type)) {
                if (StringUtils.isBlank(code)) {
                    throw new RuntimeException("JSAPI支付模式 CODE不能为空");
                }
                requestMap.put("openid", getOpenid(code));
            }
            // 支付类型
            requestMap.put("trade_type", trade_type);
            // 场景信息值是个json字符串，注意给值时转义“双引号”，
            requestMap.put("scene_info", "{\"h5_info\":{\"type\":\"Wap\",\"wap_url\":\"可以填应该网站的URL地址\",\"wap_name\": \"订单支付\"}}");
            // 接收微信支付异步通知回调地址
            requestMap.put("notify_url", "http://dede.ntscic.cn/order/weixin/notify");
//            requestMap.put("notify_url", ntsWxPayConfig.getPayNotifyUrl());
            requestMap.put("sign", WXPayUtil.generateSignature(requestMap, ntsWxPayConfig.getKey()));
            // 调用wxpay
            Map<String, String> resultMap = wxpay.unifiedOrder(requestMap);

            log.info("微信统一下单方式：{}, resultMap==>{}", trade_type, resultMap);
            String str = null;
            Map<String, String> map = getMap(responseMap, resultMap);
            switch (trade_type) {
                case "APP":
                case "JSAPI":
                    // 3、签名生成算法
                    Long time = System.currentTimeMillis() / 1000;
                    String timestamp = time.toString();
                    returnMap.put("appid", ntsWxPayConfig.getAppID());
                    returnMap.put("noncestr", map.get("nonce_str"));
                    returnMap.put("package", "Sign=WXPay");
                    returnMap.put("timestamp", timestamp);
                    returnMap.put("partnerid", ntsWxPayConfig.getMchID());
                    returnMap.put("prepayid", map.get("prepay_id"));
                    returnMap.put("sign", WXPayUtil.generateSignature(returnMap, ntsWxPayConfig.getKey()));
                    str =  JSONObject.toJSONString(returnMap);
                    break;
                case "NATIVE":
                    String codeUrl = map.get("code_url");
                    str = qrCodeGenerator.generateQRCodeImage(codeUrl);
                    break;
                case "MWEB":
//                    String urlString = URLEncoder.encode("http://mall.ntscic.cn/mall", "GBK");
//                    str = map.get("mweb_url")+ "&redirect_url="+ urlString;
                    str = map.get("mweb_url");
                    break;
            }

            return new SBApi(1, "成功", str);
        } catch (Exception e) {
            log.error("统一下单方式：{},订单号：{}，错误信息：{}", trade_type, orderNo, e.getMessage());
            return new SBApi(0, "失败", e.getMessage());
        }
    }

    private Map<String, String> getMap(Map<String, String> responseMap,
                                       Map<String, String> resultMap) {
        //获取返回码
        String returnCode = resultMap.get("return_code");
        //若返回码为SUCCESS，则会返回一个result_code,再对该result_code进行判断
        if ("SUCCESS".equals(returnCode)) {
            String resultCode = resultMap.get("result_code");
            if ("SUCCESS".equals(resultCode)) {
                responseMap = resultMap;
            }
        } else {
            throw new RuntimeException(resultMap.get("return_msg"));
        }
        return responseMap;
    }

    private String getOpenid(String code) {
        String baseUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
        // 获取openid
        String getOpenIdparam = "appid=" + ntsWxPayConfig.getAppID() + "&secret=" + ntsWxPayConfig.getSecret() + "&code=" + code + "&grant_type=authorization_code";
        String url = baseUrl + "?" + getOpenIdparam;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> str = restTemplate.getForEntity(url, String.class);
        log.info("getOpenid===>{}", str);
        JSONObject opidJsonObject = JSONObject.parseObject(str.getBody());
        log.info("***opidJsonObject==>{}",opidJsonObject);
        //获取到了openid
        return opidJsonObject.get("openid").toString();
    }

    @Override
    public String notify(String notifyStr) {
        String xmlBack = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml> ";
        try {
            // 转换成map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(notifyStr);
            WXPay wxpayApp = new WXPay(ntsWxPayConfig);
            if (wxpayApp.isPayResultNotifySignatureValid(resultMap)) {
                //状态
                String returnCode = resultMap.get("return_code");
                //商户订单号
                String outTradeNo = resultMap.get("out_trade_no");
                String transactionId = resultMap.get("transaction_id");
                if (returnCode.equals("SUCCESS")) {
                    if (StringUtils.isNotBlank(outTradeNo)) {
                        /**
                         * 注意！！！
                         * 请根据业务流程，修改数据库订单支付状态，和其他数据的相应状态
                         *
                         */
                        log.info("微信手机支付回调成功,订单号:{}", outTradeNo);
                        xmlBack = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlBack;
    }

//    @Override
//    public SBApi refund(String orderNo, double amount, String refundReason) {
//
//        if (StringUtils.isBlank(orderNo)) {
//            return SBApi.error("订单编号不能为空");
//        }
//        if (amount <= 0) {
//            return SBApi.error("退款金额必须大于0");
//        }
//
//        Map<String, String> responseMap = new HashMap<>();
//        Map<String, String> requestMap = new HashMap<>();
//        WXPay wxpay = new WXPay(ntsWxPayConfig);
//        requestMap.put("out_trade_no", orderNo);
//        requestMap.put("out_refund_no", UUID.randomUUID().toString().replaceAll("-", ""));
//        requestMap.put("total_fee", "0.01");
//        //所需退款金额
//        requestMap.put("refund_fee", String.valueOf((int) (amount * 100)));
//        requestMap.put("refund_desc", refundReason);
//        try {
//            responseMap = wxpay.refund(requestMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //返回状态码
//        String return_code = responseMap.get("return_code");
//        //返回信息
//        String return_msg = responseMap.get("return_msg");
//        if ("SUCCESS".equals(return_code)) {
//            //业务结果
//            String result_code = responseMap.get("result_code");
//            //错误代码描述
//            String err_code_des = responseMap.get("err_code_des");
//            if ("SUCCESS".equals(result_code)) {
//                //表示退款申请接受成功，结果通过退款查询接口查询
//                //修改用户订单状态为退款申请中或已退款。退款异步通知根据需求，可选
//                //
//                return ResultMap.ok("退款申请成功");
//            } else {
//                log.info("订单号:{}错误信息:{}", orderNo, err_code_des);
//                return ResultMap.error(err_code_des);
//            }
//        } else {
//            log.info("订单号:{}错误信息:{}", orderNo, return_msg);
//            return ResultMap.error(return_msg);
//        }
//    }

}
