package com.soubao.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.soubao.entity.MiniappConfig;
import com.soubao.entity.Order;

import com.soubao.dto.WxPayOrderDto;
import com.soubao.entity.OrderGoods;
import com.soubao.service.MiniappConfigService;
import com.soubao.service.OrderGoodsService;
import com.soubao.service.OrderService;
import com.soubao.service.WxPayService;


import com.soubao.utils.SBApi;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping(value = "/weixin")
public class WxPayController {
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private MiniappConfigService miniappConfigService;


    private final static String GETOPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    @ApiOperation(value = "统一下单", notes = "统一下单", httpMethod = "POST")
    @PostMapping("unified")
    @ResponseBody
    public SBApi unifiedOrder(@Valid @RequestBody WxPayOrderDto wxPayOrderDto,
                              HttpServletRequest httpServletRequest) {
        //System.out.println("unifiedOrder--wxPayOrderDto.getOrderSn()--------"+wxPayOrderDto.getOrderSn());
        SBApi sbApi = new SBApi();
        List<Order> orderList = orderService.list((new QueryWrapper<Order>()).select("order_sn,order_id,order_amount,goods_price")
                .eq("order_sn", wxPayOrderDto.getOrderSn()).or().eq("master_order_sn", wxPayOrderDto.getOrderSn()));
        if (orderList.size() == 0) {
           sbApi.setStatus(-1);
           sbApi.setResult("the order is not exist!");
           return sbApi;
        }
        BigDecimal totalOrderAmount = BigDecimal.ZERO;
        Set<Integer> orderIds = new HashSet<>();
        for (Order order : orderList) {
            totalOrderAmount = totalOrderAmount.add(order.getGoodsPrice());
            orderIds.add(order.getOrderId());
        }
        List<OrderGoods> orderGoods = orderGoodsService.list((new QueryWrapper<OrderGoods>()).select("goods_name")
                .in("order_id", orderIds));
        String body = orderGoods.stream().map(OrderGoods::getGoodsName).collect(Collectors.joining(","));

        wxPayOrderDto.setBody(body);
        wxPayOrderDto.setAmount(totalOrderAmount);
        wxPayOrderDto.setClientIp(getUserIP(httpServletRequest));

        sbApi.setResult(wxPayService.unifiedOrder(wxPayOrderDto));
        return sbApi;
    }


    private Map<String, String> unifiedOrderForJs(WxPayOrderDto wxPayOrderDto) {
        return wxPayService.unifiedOrder(wxPayOrderDto);
    }

        /**
         * 微信支付异步通知
         */
    @ApiOperation(value = "支付回调", notes = "统一下单")
    @RequestMapping(value = "notify")
    public String payNotify(HttpServletRequest request) {
        InputStream is = null;
        String xmlBack = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml> ";
        try {
            is = request.getInputStream();
            // 将InputStream转换成String
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            xmlBack = wxPayService.notify(sb.toString());
        } catch (Exception e) {
            log.error("微信手机支付回调通知失败：", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return xmlBack;
    }

    @ApiOperation(value = "退款", notes = "退款")
    @PostMapping("refund")
    public SBApi refund(@RequestParam(value = "order_sn") String orderSn,
                        @RequestParam(value = "amount") double amount,
                        @RequestParam(required = false) String refundReason) {
        Order order = orderService.getOne((new QueryWrapper<Order>()).eq("order_sn", orderSn));
        wxPayService.refund(order, amount, refundReason);
        return new SBApi();
    }


    @GetMapping("/MP_verify_LEJPlKKTitC5B4GN.txt")
    public ModelAndView doMpVerify(HttpServletRequest request, String orderSn) {
        byte[] bytes = new byte[1024];


        //this.getClass().getClassLoader().getResourceAsStream()
        //File file = ResourceUtils.getFile("classpath:templates/MP_verify_LEJPlKKTitC5B4GN.txt");
        //InputStream inputStream = new FileInputStream(file);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/MP_verify_LEJPlKKTitC5B4GN.txt");

        try {
            inputStream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str= new String(bytes);
        ModelAndView modelAndView = new ModelAndView("MP_verify_LEJPlKKTitC5B4GN.txt","message",str);
        return modelAndView ;

    }



    @GetMapping("/doPay")
    public String doPay(HttpServletRequest request, String orderSn, Model retMap) {
        WxPayOrderDto wxPayOrderDto = getOrderInfo(orderSn);
        //WxPayOrderDto wxPayOrderDto = new WxPayOrderDto();
        //wxPayOrderDto.setAmount(1000.00d);
        //wxPayOrderDto.setBody("ddddddd");
        //wxPayOrderDto.setOrderSn("13623183399293");
        retMap.addAttribute("totalFee", wxPayOrderDto.getAmount());
        retMap.addAttribute("orderSn", wxPayOrderDto.getOrderSn());
        retMap.addAttribute("body", wxPayOrderDto.getBody());
        return "prePay";
    }

    @GetMapping("/unifiedOrder")
    public String unifiedOrder(HttpServletRequest request, String code, String totalFee, String orderSn, String body, ModelMap retMap) {
        System.out.println("************WxPayController.unifiedOrder()***************");
        String openId = null;
        if (StringUtil.isNullOrEmpty(code)) {
            retMap.addAttribute("chymsg", "get_code_error");
            return "doPay";
        }
        MiniappConfig config = miniappConfigService.getOne((new QueryWrapper<MiniappConfig>()).last("limit 1"));
        //获取 openid;
        openId = getOpenId(code, config, retMap);

        //获取prepayId
        if (!StringUtil.isNullOrEmpty(openId)) {
            WxPayOrderDto wxPayOrderDto = new WxPayOrderDto();
            wxPayOrderDto.setOrderSn(orderSn);
            wxPayOrderDto.setOpenid(openId);
            wxPayOrderDto.setAmount(BigDecimal.valueOf(Long.parseLong(totalFee)));
            wxPayOrderDto.setBody(body);
            wxPayOrderDto.setTradeType("JSAPI");
            String clientIp =getUserIP(request);
            wxPayOrderDto.setClientIp(clientIp);
            Map<String, String> data = unifiedOrderForJs(wxPayOrderDto);
            retMap.put("data", data);
        }
        retMap.addAttribute("chymsg", "OK");
        return "doPay";
    }

    private String getOpenId(String code, MiniappConfig config, ModelMap retMap) {
        String openId = "";
        String getOpenIdparam = "appid=" + config.getAppId() + "&secret=" + config.getApiclientCert().trim() + "&code=" + code + "&grant_type=authorization_code";
        String getOpenIdUrl = GETOPENID_URL + "?" + getOpenIdparam;
        System.out.println("***getOpenId:" + getOpenIdUrl);
        RestTemplate rest = new RestTemplate();
        rest.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        try {
            String resString = rest.getForObject(new URI(getOpenIdUrl), String.class);
            JSONObject opidJsonObject = JSONObject.parseObject(resString);
            System.out.println("***opidJsonObject:" + opidJsonObject);
            openId = opidJsonObject.get("openid").toString();//获取到了openid
            if (StringUtil.isNullOrEmpty(openId)) {
                retMap.addAttribute("chymsg", "获取openId值错误");
            }

        } catch (Exception ex) {
            retMap.put("code", "500");
            retMap.put("msg", ex.getStackTrace());
            ex.printStackTrace();
            System.out.println("***Getting openid is fail ,the code is:" + code);
        }
        return openId;
    }

    private WxPayOrderDto getOrderInfo(String orderSn) {
        WxPayOrderDto wxPayOrderDto = new WxPayOrderDto();
        List<Order> orderList = orderService.list((new QueryWrapper<Order>()).select("order_sn,order_id,order_amount")
                .eq("order_sn", orderSn).or().eq("master_order_sn", orderSn));

        if (orderList.size() == 0) {
            return null;
        }
        BigDecimal totalOrderAmount = BigDecimal.ZERO;
        Set<Integer> orderIds = new HashSet<>();
        for (Order order : orderList) {
            totalOrderAmount = totalOrderAmount.add(order.getOrderAmount());
            orderIds.add(order.getOrderId());
        }
        List<OrderGoods> orderGoods = orderGoodsService.list((new QueryWrapper<OrderGoods>()).select("goods_name")
                .in("order_id", orderIds));
        String body = orderGoods.stream().map(OrderGoods::getGoodsName).collect(Collectors.joining(","));
        wxPayOrderDto.setOrderSn(orderSn);
        wxPayOrderDto.setBody(body);
        wxPayOrderDto.setAmount(totalOrderAmount);

        return wxPayOrderDto;

    }

    private  String getUserIP(HttpServletRequest request) {
        // 优先取 X-Real-IP
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.length() == 0 ||"unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("0:0:0:0:0:0:0:1".equals(ip)) {
                ip = "0.0.0.0";
            }
        }
        if ("unknown".equalsIgnoreCase(ip)) {
            ip = "0.0.0.0";
            return ip;
        }
        int index = ip.indexOf(',');
        if (index >= 0) {
            ip = ip.substring(0, index);
        }
        return ip;
    }


}
