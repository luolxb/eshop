package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.ShippingMapper;
import com.soubao.entity.DeliveryDoc;
import com.soubao.entity.Order;
import com.soubao.entity.Shipping;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.OrderService;
import com.soubao.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-12-10
 */
@Service
public class ShippingServiceImpl extends ServiceImpl<ShippingMapper, Shipping> implements ShippingService {

    @Autowired
    private OrderService orderService;

    @Override
    public List<Shipping> shippingPrint(Set<Integer> ids) {
        List<Order> orders = orderService.orderList(ids);
        Map<String, Shipping> shippingMap = list()
                .stream()
                .collect(Collectors.toMap(Shipping::getShippingCode,shipping -> shipping));

        Map<Integer, List<DeliveryDoc>> deliveryDocMap = orderService.deliveryDocByOrderIds(ids)
                .stream()
                .collect(Collectors.groupingBy(DeliveryDoc::getOrderId));

        List<Shipping> shippingList = new ArrayList<>();
        for (Order order : orders) {
            if (deliveryDocMap.containsKey(order.getOrderId())) {
                for (DeliveryDoc deliveryDoc : deliveryDocMap.get(order.getOrderId())) {
                    if (shippingMap.containsKey(deliveryDoc.getShippingCode())) {
                        Shipping shipping = shippingMap.get(deliveryDoc.getShippingCode());
                        String storeAddressProvinceName = deliveryDoc.getStoreAddressProvinceName();
                        String storeAddressCityName = deliveryDoc.getStoreAddressCityName();
                        String storeAddressDistrictName = deliveryDoc.getStoreAddressDistrictName();
                        String provinceName = order.getProvinceName();
                        String cityName = order.getCityName();
                        String districtName = order.getDistrictName();
                        String htmlEncode = shipping.getHtmlEncode().replace("发货点-名称", order.getStoreName())
                                .replace("发货点-联系人", deliveryDoc.getStoreAddressConsignee())
                                .replace("发货点-电话", deliveryDoc.getStoreAddressMobile())
                                .replace("发货点-省份", storeAddressProvinceName != null ? storeAddressProvinceName : "N")
                                .replace("发货点-城市", storeAddressCityName != null ? storeAddressCityName : "N")
                                .replace("发货点-区县", storeAddressDistrictName != null ? storeAddressDistrictName : "N")
                                .replace("发货点-手机", deliveryDoc.getStoreAddressMobile())
                                .replace("发货点-详细地址", deliveryDoc.getStoreAddress())
                                .replace("收件人-姓名", order.getConsignee())
                                .replace("收件人-手机", order.getMobile())
                                .replace("收件人-电话", order.getMobile())//数据库表里没电话这个字段
                                .replace("收件人-省份", provinceName != null ? provinceName : "N")
                                .replace("收件人-城市", cityName != null ? cityName : "N")
                                .replace("收件人-区县", districtName != null ? districtName : "N")
                                .replace("收件人-邮编", order.getZipcode())
                                .replace("收件人-详细地址", order.getAddress())
                                .replace("订单-订单号", order.getOrderSn())
                                .replace("订单-备注", order.getAdminNote())
                                .replace("订单-配送费用", order.getShippingPrice().toString());
                        shipping.setHtmlEncode(htmlEncode);
                        shippingList.add(shipping);
                    }
                }
            } else {
                //物流插件不存在
                throw new ShopException(ResultEnum.SHIPPING_PLUG_IS_NOT_EXIST);
            }
        }
        return shippingList;
    }
}
