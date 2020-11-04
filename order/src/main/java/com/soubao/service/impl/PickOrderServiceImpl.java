package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.common.exception.ShopException;
import com.soubao.common.utils.IdUtil;
import com.soubao.dao.PickOrderMapper;
import com.soubao.dto.DeliveryPickOrderRq;
import com.soubao.entity.*;
import com.soubao.entity.vo.PickOrderDeliveryRq;
import com.soubao.entity.vo.PickOrderRq;
import com.soubao.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 提货订单的服务实现类
 */
@Slf4j
@Service
public class PickOrderServiceImpl extends ServiceImpl<PickOrderMapper, PickOrder> implements PickOrderService {

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private DeliveryDocService deliveryDocService;

    @Autowired
    private PickOrderActionService pickOrderActionService;

    @Autowired
    private MallService mallService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    /**
     * 提货申请
     * 生成提货订单
     * 提货订单状态为 APP显示提货中，商家显示待发货
     */
    @Override
    public void pickOrderReq(PickOrderRq pickOrderRq) {
        // 判断是否已提交订单,根据orderid
        Integer count = this.baseMapper.selectCount(new QueryWrapper<PickOrder>().eq("order_id", pickOrderRq.getOrderId()));
        if (count > 0) {
            throw new ShopException(400, "操作失败");
        }
        // 判断商品是否上架
        Goods goods = mallService.goods(pickOrderRq.getGoodsId());
        if (null == goods || goods.getIsOnSale() != 0 || !goods.getOwnerId().equals(pickOrderRq.getUserId())) {
            throw new ShopException(400, "商品状态错误，不能提货");
        }
        // 获取当前商品的购买订单
        List<OrderGoods> orderGoodsList = orderGoodsService.list(new QueryWrapper<OrderGoods>().eq("goods_id", pickOrderRq.getGoodsId()));
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.in("order_id", orderGoodsList.stream().map(OrderGoods::getOrderId).collect(Collectors.toList()));
        orderQueryWrapper.eq("user_id", pickOrderRq.getUserId());
        orderQueryWrapper.eq("order_status", 1);
        orderQueryWrapper.eq("pay_status", 1);
        orderQueryWrapper.orderByDesc("order_id");
        orderQueryWrapper.last("limit 1");
        Order order = orderService.getOne(orderQueryWrapper);
        PickOrder pickOrder = new PickOrder();
        BeanUtils.copyProperties(pickOrderRq, pickOrder);

        pickOrder.setOrderId(order.getOrderId());
        pickOrder.setPickOrderSn(IdUtil.nextId() + "");
        long time = System.currentTimeMillis() / 1000;
        pickOrder.setAddTime(time);
        pickOrder.setUpdateTime(time);
        pickOrder.setPickOrderStatus(0);
        this.save(pickOrder);

        // 将商品delete_flag 修改为1
        Goods goods1 = new Goods();
        goods1.setGoodsId(pickOrderRq.getGoodsId());
        goods1.setDeleteFlag(1);
        goods1.setIsOnSale(0);
        mallService.updateGoods(goods1);
        //操作日志
        pickOrderActionService.addActionLog(pickOrder, "提交提货订单", "您提交了提货订单", null, 2, null);
    }

    /**
     * 确认发货提货订单
     *
     * @param deliveryPickOrderRq
     */
    @Override
    public void deliveryPickOrder(DeliveryPickOrderRq deliveryPickOrderRq) {
        for (PickOrderDeliveryRq pickOrderDeliveryRq : deliveryPickOrderRq.getPickOrderDeliveryRqList()) {
            PickOrder pickOrder = this.baseMapper.selectById(pickOrderDeliveryRq.getPickOrderId());
            Seller seller = authenticationFacade.getPrincipal(Seller.class);
            pickOrder.setSellerId(seller.getSellerId());
            pickOrder.setPickOrderType(pickOrderDeliveryRq.getPickOrderType());
            pickOrder.setShippingCode(pickOrderDeliveryRq.getShippingCode());
            pickOrder.setShippingName(pickOrderDeliveryRq.getShippingName());
            pickOrder.setShippingPrice(pickOrderDeliveryRq.getShippingPrice());
            long time = System.currentTimeMillis() / 1000;
            pickOrder.setShippingTime(time);

            DeliveryDoc deliveryDoc = new DeliveryDoc();
            deliveryDoc.setOrderId(pickOrder.getPickOrderId());
            deliveryDoc.setShippingName(pickOrder.getShippingName());
            deliveryDoc.setShippingCode(pickOrder.getShippingCode());
            deliveryDoc.setInvoiceNo(deliveryPickOrderRq.getInvoiceNo());
            deliveryDoc.setNote(deliveryPickOrderRq.getNote());
            deliveryDoc.setZipcode(pickOrder.getZipcode());
            deliveryDoc.setOrderSn(pickOrder.getPickOrderSn());
            deliveryDoc.setUserId(pickOrder.getUserId());
            deliveryDoc.setAdminId(pickOrder.getSellerId());
            deliveryDoc.setConsignee(pickOrder.getNickname());
            deliveryDoc.setMobile(pickOrder.getMobile());
            deliveryDoc.setCountry(pickOrder.getCountry());
            deliveryDoc.setProvince(pickOrder.getProvince());
            deliveryDoc.setCity(pickOrder.getCity());
            deliveryDoc.setDistrict(pickOrder.getDistrict());
            deliveryDoc.setAddress(pickOrder.getAddress());
            deliveryDoc.setShippingPrice(pickOrder.getShippingPrice());
            deliveryDoc.setCreateTime(time);
            Set<Integer> collect = Arrays.asList(pickOrder.getSellerId()).stream().collect(Collectors.toSet());
            List<Seller> sellers = sellerService.sellers(collect);
            deliveryDoc.setStoreId(sellers.get(0).getStoreId());

            StoreAddress storeAddress;
            if (deliveryPickOrderRq.getStoreAddressId() != null) {
                storeAddress = sellerService.storeAddress(deliveryPickOrderRq.getStoreAddressId());
            } else {
                //默认发货地址
                storeAddress = sellerService.storeAddressDefault(sellers.get(0).getStoreId(), 0);
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

            pickOrder.setUpdateTime(time);
            pickOrder.setPickOrderStatus(2);
            //操作日志
            pickOrderActionService.addActionLog(pickOrder, "订单发货", deliveryPickOrderRq.getNote(), pickOrder.getSellerId(), 1, sellers.get(0).getStoreId());

            //修改提货订单发货信息
            updateById(pickOrder);
        }

    }

    /**
     * 确认收货
     *
     * @param pickOrderId
     */
    @Override
    public void confirmReceipt(Integer pickOrderId) {
        User user = authenticationFacade.getPrincipal(User.class);
        PickOrder pickOrder = this.baseMapper.selectById(pickOrderId);
        pickOrder.setUpdateTime(System.currentTimeMillis() / 1000);
        pickOrder.setPickOrderStatus(3);
        pickOrder.setConsigneeId(user.getUserId());

        this.baseMapper.updateById(pickOrder);

        //操作日志
        pickOrderActionService.addActionLog(pickOrder, "确认收货", "您已确认收货", null, 2, null);
    }

}
