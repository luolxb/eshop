package com.soubao.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soubao.entity.DeliveryDoc;
import com.soubao.dao.DeliveryDocMapper;
import com.soubao.entity.Region;
import com.soubao.entity.Seller;
import com.soubao.service.DeliveryDocService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.service.SellerService;
import com.soubao.service.MallService;
import com.soubao.common.utils.HttpClientUtil;
import com.soubao.vo.KD100;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 发货单 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-10-14
 */
@Service("deliveryDocService")
public class DeliveryDocServiceImpl extends ServiceImpl<DeliveryDocMapper, DeliveryDoc> implements DeliveryDocService {
    @Autowired
    private MallService mallService;
    @Autowired
    private SellerService sellerService;

    @Override
    public void withRegions(List<DeliveryDoc> deliveryDocs) {
        Set<Integer> regionIds = new HashSet<>();
        for (DeliveryDoc deliveryDoc : deliveryDocs) {
            regionIds.add(deliveryDoc.getStoreAddressProvinceId());
            regionIds.add(deliveryDoc.getStoreAddressCityId());
            regionIds.add(deliveryDoc.getStoreAddressDistrictId());
        }
        HashSet<Integer> set = new HashSet<>();
        Map<Integer, String> regionMap = mallService.regions(CollectionUtils.isEmpty(regionIds) ? set : regionIds)
                .stream().collect(Collectors.toMap(Region::getId, Region::getName));
        for (DeliveryDoc deliveryDoc : deliveryDocs) {
            deliveryDoc.setStoreAddressProvinceName(regionMap.get(deliveryDoc.getStoreAddressProvinceId()));
            deliveryDoc.setStoreAddressCityName(regionMap.get(deliveryDoc.getStoreAddressCityId()));
            deliveryDoc.setStoreAddressDistrictName(regionMap.get(deliveryDoc.getStoreAddressDistrictId()));
        }
    }

    @Override
    public void withRegions(DeliveryDoc deliveryDoc) {
        if (deliveryDoc == null) {
            return;
        }
        Set<Integer> regionIds = new HashSet<>();
        regionIds.add(deliveryDoc.getStoreAddressProvinceId());
        regionIds.add(deliveryDoc.getStoreAddressCityId());
        regionIds.add(deliveryDoc.getStoreAddressDistrictId());
        Map<Integer, String> regionMap = mallService.regions(regionIds)
                .stream().collect(Collectors.toMap(Region::getId, Region::getName));
        deliveryDoc.setStoreAddressProvinceName(regionMap.get(deliveryDoc.getStoreAddressProvinceId()));
        deliveryDoc.setStoreAddressCityName(regionMap.get(deliveryDoc.getStoreAddressCityId()));
        deliveryDoc.setStoreAddressDistrictName(regionMap.get(deliveryDoc.getStoreAddressDistrictId()));
    }

    @Override
    public void withSeller(List<DeliveryDoc> deliveryDocs) {
        Set<Integer> adminIds = deliveryDocs.stream().map(DeliveryDoc::getAdminId).collect(Collectors.toSet());
        Map<Integer, Seller> sellerMap = sellerService.sellers(adminIds)
                .stream().collect(Collectors.toMap(Seller::getSellerId, seller -> seller));
        for (DeliveryDoc deliveryDoc : deliveryDocs) {
            deliveryDoc.setSeller(sellerMap.get(deliveryDoc.getAdminId()));
        }
    }

    @Override
    public Object getExpress(DeliveryDoc deliveryDoc) {
        Map<Object, Object> config = mallService.config();
        String requestUrl;
        Map<String, String> requestUrlParam = new HashMap<>();
        if ("1".equals(config.get("express_express_switch"))) {
            requestUrl = "http://api.kdniao.com/Ebusiness/EbusinessOrderHandle.aspx";
            //快递鸟
            String requestData = "{'OrderCode':'" + deliveryDoc.getOrderSn()
                    + "','ShipperCode':'" + deliveryDoc.getShippingCode()
                    + "','LogisticCode':'" + deliveryDoc.getInvoiceNo() + "'}";
            try {
                String dataSign = encrypt(requestData, (String) config.get("express_kdniao_key"), "UTF-8");
                requestUrlParam.put("RequestData", URLEncoder.encode(requestData, "UTF-8"));
                requestUrlParam.put("DataSign", URLEncoder.encode(dataSign, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            requestUrlParam.put("EBusinessID", (String) config.get("express_kdniao_id"));
            requestUrlParam.put("RequestType", "1002");
            requestUrlParam.put("DataType", "2");
        } else {
            requestUrl = "http://poll.kuaidi100.com/poll/query.do";
            KD100 kd100 = new KD100();
            kd100.setCom(deliveryDoc.getShippingCode());
            kd100.setNum(deliveryDoc.getInvoiceNo());
            kd100.setPhone(deliveryDoc.getMobile());
            kd100.setResultv2(1);
            requestUrlParam.put("customer", (String) config.get("express_kd100_customer"));
            String param = JSON.toJSONString(kd100);
            requestUrlParam.put("param", param);
            requestUrlParam.put("sign", DigestUtils.md5DigestAsHex((param + config.get("express_kd100_key") + config.get("express_kd100_customer")).getBytes()).toUpperCase());
        }
        return JSON.parseObject(HttpClientUtil.doPost(requestUrl, requestUrlParam));
    }

    private String encrypt(String requestData, String express_kdniao_key, String s) throws UnsupportedEncodingException {
        return new String(Base64.encodeBase64(DigestUtils.md5DigestAsHex((requestData + express_kdniao_key).getBytes(s))
                .toLowerCase().getBytes(s)));
    }
}
