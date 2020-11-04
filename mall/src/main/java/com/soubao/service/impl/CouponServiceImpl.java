package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.*;
import com.soubao.dao.CouponMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 优惠券表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-23
 */
@Slf4j
@Service("couponService")
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponListService couponListService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsCouponService goodsCouponService;
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    @Autowired
    private SellerService sellerService;

    @Override
    public IPage<Coupon> getUserCouponListByType(Page page, User user, Integer type) {
        return couponMapper.selectUserCouponListByType(page, user, type);
    }

    @Override
    public IPage<Coupon> selectCouponPage(Page page, Integer catId, Integer type) {
        return couponMapper.selectCouponPage(page, catId, type);
    }

    @Override
    public void addUserCoupon(User user, CouponList couponList) {
        Coupon coupon;
        boolean updateResult;
        if (couponList.getCid() != null) { //领取优惠券
            coupon = getById(couponList.getCid());
            if (coupon == null || coupon.getStatus() != 1) {
                throw new ShopException(ResultEnum.ACTIVITY_NOT_EXISTS);
            }
            if (coupon.getSendEndTime() < System.currentTimeMillis() / 1000) {
                throw new ShopException(ResultEnum.ACTIVITY_TIME_IS_OVER);
            }
            if (coupon.getSendNum() >= coupon.getCreatenum() && coupon.getCreatenum() > 0) {
                throw new ShopException(ResultEnum.COUPON_COUNT_ZERO);
            }
            CouponList cl = couponListService.getOne(new QueryWrapper<CouponList>()
                    .eq("cid", couponList.getCid())
                    .eq("uid", user.getUserId()));
            if (cl != null) {
                throw new ShopException(ResultEnum.USER_COUPON_EXISTS);
            }
            CouponList userCoupon = new CouponList();
            userCoupon.setUid(user.getUserId());
            userCoupon.setCid(couponList.getCid());
            userCoupon.setType(2);
            userCoupon.setSendTime(System.currentTimeMillis() / 1000);
            userCoupon.setStoreId(coupon.getStoreId());
            //更新用户持有的优惠券
            updateResult = couponListService.save(userCoupon);
        } else if (!StringUtils.isEmpty(couponList.getCode())) {  //兑换码兑换优惠券
            CouponList userCoupon = couponListService.getOne(new QueryWrapper<CouponList>().eq("code", couponList.getCode()));
            if (userCoupon == null) {
                throw new ShopException(ResultEnum.COUPON_CODE_INVALID);
            }
            if (userCoupon.getOrderId() > 0) {
                throw new ShopException(ResultEnum.COUPON_IS_USED);
            }
            if (userCoupon.getUid() > 0) {
                throw new ShopException(ResultEnum.COUPON_EXCHANGED);
            }
            coupon = getById(userCoupon.getCid());
            if (System.currentTimeMillis() / 1000 < coupon.getUseStartTime()) {
                throw new ShopException(ResultEnum.FAIL.getCode(), "未到该优惠券使用时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(coupon.getUseStartTime() * 1000)));
            }
            if (System.currentTimeMillis() / 1000 > coupon.getUseEndTime() || coupon.getStatus() == 2) {
                throw new ShopException(ResultEnum.COUPON_INVALID);
            }
            updateResult = couponListService.update(new UpdateWrapper<CouponList>()
                    .set("uid", user.getUserId())
                    .eq("id", userCoupon.getId()));
        } else {
            throw new ShopException(ResultEnum.COUPON_CODE_INVALID);
        }
        //更新优惠券已领数量
        if (updateResult) {
            update(new UpdateWrapper<Coupon>().set("send_num", coupon.getSendNum() + 1).eq("id", coupon.getId()));
        }
    }

    @Override
    public void calculateCouponAmount(User user, Order masterOrder) {
        //订单使用的优惠券id数组
        Integer[] orderUseCouponIds;
        orderUseCouponIds = new Integer[masterOrder.getOrderList().size()];
        Arrays.fill(orderUseCouponIds, 0);
        if (!StringUtils.isEmpty(masterOrder.getOrderUseCouponIds())) {
            String[] orderUseCouponIdsArr = masterOrder.getOrderUseCouponIds().split(",");
            for (int i = 0; i < orderUseCouponIdsArr.length; i++) {
                orderUseCouponIds[i] = Integer.parseInt(orderUseCouponIdsArr[i]);
            }
        }
        //校验重复优惠券id，除第一个外都置为零
        Set<Integer> existCouponIds = new HashSet<>();
        for (int i = 0; i < orderUseCouponIds.length; i++) {
            Integer couponId = orderUseCouponIds[i];
            if (couponId != 0 && existCouponIds.contains(couponId)) {
                orderUseCouponIds[i] = 0;
            } else {
                existCouponIds.add(couponId);
            }
        }
        //设置子订单可用优惠券集合并返回所有子订单可用优惠券id与优惠券对象集合
        Map<Integer, Coupon> orderUsableCouponMap = setAndGetOrderCouponsByOrder(user, masterOrder, Arrays.asList(orderUseCouponIds));
        List<Order> childOrderList = masterOrder.getOrderList();
        for (int i = 0; i < childOrderList.size(); i++) {
            Order childOrder = childOrderList.get(i);
            Integer couponId = orderUseCouponIds[i];
            if (couponId == 0) {
                continue;
            }
            if (orderUsableCouponMap.containsKey(couponId)) {
                Coupon coupon = orderUsableCouponMap.get(couponId);
                childOrder.setOrderAmount(childOrder.getOrderAmount().subtract(coupon.getMoney()));
                childOrder.setCouponPrice(coupon.getMoney());
                childOrder.setCouponListId(coupon.getCouponListId());
            } else {
                throw new ShopException(ResultEnum.COUPON_ILLEGAL);
            }
        }
    }

    @Override
    public List<Coupon> getUserNewCoupons(User user) {
        List<Coupon> coupons = new ArrayList<>();
        if (user.getTotalAmount().compareTo(BigDecimal.ZERO) == 0) {
            int userCouponCount = couponListService.count((new QueryWrapper<CouponList>()).eq("uid", user.getUserId()));
            if (userCouponCount == 0) {
                long now = System.currentTimeMillis() / 1000;
                coupons = list((new QueryWrapper<Coupon>())
                        .eq("type", 4).eq("status", 1)
                        .le("send_start_time", now).gt("send_end_time", now).orderByDesc("add_time")
                        .last("limit 1"));
            }

        }
        return coupons;
    }

    @Override
    public Coupon getCoupon(Integer couponId) {
        Coupon coupon = this.getById(couponId);
        if (coupon.getStoreId() != 0) {
            Store store = sellerService.getStoreById(coupon.getStoreId());
            coupon.setStoreName(store.getStoreName());
        }
        if (coupon.getUseType() == 1) {
            List<GoodsCoupon> goodsCoupons = goodsCouponService.list(new QueryWrapper<GoodsCoupon>().eq("coupon_id",couponId));
            Set<Integer> goodsIds = new HashSet<>();
            for (GoodsCoupon goodsCoupon : goodsCoupons) {
                Integer goodsId = goodsCoupon.getGoodsId();
                goodsIds.add(goodsId);
            }
            List<Goods> goodsList = goodsService.list(new QueryWrapper<Goods>().in("goods_id", goodsIds));
            coupon.setGoodsList(goodsList);
        }
        if (coupon.getUseType() == 2) {
            GoodsCoupon goodsCoupon = goodsCouponService.getOne(new QueryWrapper<GoodsCoupon>().eq("coupon_id", couponId));
            Integer categoryId = goodsCoupon.getGoodsCategoryId();
            GoodsCategory goodsCategory = goodsCategoryService.getById(categoryId);
            coupon.setGoodsCategory(goodsCategory);
        }
        return coupon;
    }

    @Override
    public void deleteCoupon(Integer id) {
        Coupon coupon = getById(id);
        goodsCouponService.removeById(coupon.getId());
        couponListService.remove(new QueryWrapper<CouponList>().eq("cid", id));
        removeById(id);
    }

    @Override
    public void updateCoupon (Coupon coupon) {
        updateById(coupon);
        goodsCouponService.removeById(coupon.getId());
        saveCouponGoods(coupon);
    }

    @Override
    public void addCoupon(Coupon coupon) {
        coupon.setAddTime(System.currentTimeMillis() / 1000);
        save(coupon);
        saveCouponGoods(coupon);
    }

    @Override
    public void withStore(List<Coupon> records) {
        if (records.size() > 0) {
            Set<Integer> storeIds = records.stream().map(Coupon::getStoreId).collect(Collectors.toSet());
            Map<Integer, Store> storeMap = new HashMap<>();
            if(!(storeIds.size() == 0 || (storeIds.size() == 1 && storeIds.contains(0)))){
                storeMap = sellerService.getStoreListByIds(storeIds).stream().collect(Collectors.toMap(Store::getStoreId, store -> store));
            }
            for(Coupon coupon : records){
                if (coupon.getStoreId() == 0) {
                    coupon.setLimitStore("全平台");
                } else {
                    coupon.setLimitStore(storeMap.get(coupon.getStoreId()).getStoreName());
                    coupon.setStoreName(storeMap.get(coupon.getStoreId()).getStoreName());
                }
            }
        }
    }

    private void saveCouponGoods(Coupon coupon) {
        GoodsCoupon goodsCoupon = new GoodsCoupon();
        goodsCoupon.setCouponId(coupon.getId());
        if (coupon.getUseType() == 1) {
            List<Goods> goodsList = coupon.getGoodsList();
            for (Goods goods : goodsList) {
                goodsCoupon.setGoodsId(goods.getGoodsId());
                goodsCouponService.save(goodsCoupon);
            }
        }
        if (coupon.getUseType() == 2) {
            goodsCoupon.setGoodsCategoryId(coupon.getGoodsCategoryId());
            goodsCouponService.save(goodsCoupon);
        }
    }

    private Map<Integer, Coupon> setAndGetOrderCouponsByOrder(User user, Order masterOrder, List<Integer> orderUseCouponIds) {
        //用户可用优惠券集合
        List<Coupon> coupons = getUserCouponListByType(new Page(1, 100), user, 0).getRecords();
        //所有订单可用优惠券集合
        Map<Integer, Coupon> orderUsableCouponMap = new HashMap<>();
        //根据子订单获取可用优惠券
        List<Order> orderList = masterOrder.getOrderList();
        for (int i = 0; i < orderList.size(); i++) {
            List<Coupon> usableCoupons = new ArrayList<>();//可用优惠券
            List<Coupon> disableCoupons = new ArrayList<>();//使用条件不符合优惠券
            //订单商品总价集合：商品购买价 × 购买数量
            Map<Integer, BigDecimal> orderGoodsFeeMap = new HashMap<>();
            //订单商品分类价格集合
            Map<Integer, BigDecimal> orderGoodsCategoryFeeMap = new HashMap<>();
            Set<Integer> goodsIdSet = orderList.get(i).getOrderGoods().stream().map(OrderGoods::getGoodsId).collect(Collectors.toSet());
            //订单商品id与三级分类id集合
            Map<Integer, Integer> goodsIdAndCatId3Map = new HashMap<>();
            if (!goodsIdSet.isEmpty()) {
                goodsIdAndCatId3Map = goodsService.listByIds(goodsIdSet).stream().collect(Collectors.toMap(Goods::getGoodsId, Goods::getCatId3));
            }
            for (OrderGoods orderGoods : orderList.get(i).getOrderGoods()) {
                Integer goodsId = orderGoods.getGoodsId();
                BigDecimal goodsFee = orderGoods.getGoodsFee();
                orderGoodsFeeMap.put(goodsId, goodsFee);
                //统计商品分类价格
                Integer goodsCategoryId = goodsIdAndCatId3Map.get(goodsId);
                if (orderGoodsCategoryFeeMap.containsKey(goodsCategoryId)) {
                    orderGoodsCategoryFeeMap.put(goodsCategoryId, orderGoodsCategoryFeeMap.get(goodsCategoryId).add(goodsFee));
                } else {
                    orderGoodsCategoryFeeMap.put(goodsCategoryId, goodsFee);
                }
            }
            //校验新人优惠券使用情况
            //除此订单外其他订单选择的优惠券id
            List<Integer> otherOrderCouponIds = new ArrayList<>(orderUseCouponIds);
            otherOrderCouponIds.remove(i);

            for (Coupon c : coupons) {
                Integer couponGoodsId = c.getGoodsId();
                Integer couponGoodsCategoryId = c.getGoodsCategoryId();
                Integer couponStoreId = c.getStoreId();
                BigDecimal couponCondition = c.getCondition();
                if (couponGoodsId != null && couponGoodsId > 0 && goodsIdSet.contains(couponGoodsId)) { //指定商品可用
                    //指定商品价格条件
                    if (orderGoodsFeeMap.containsKey(couponGoodsId)) {
                        if (orderGoodsFeeMap.get(couponGoodsId).compareTo(couponCondition) >= 0) {
                            usableCoupons.add(c);
                            orderUsableCouponMap.put(c.getCouponListId(), c);
                        } else {
                            disableCoupons.add(c);
                        }
                    }
                } else if (!otherOrderCouponIds.contains(c.getId()) && couponGoodsCategoryId != null && couponGoodsCategoryId > 0 && goodsIdAndCatId3Map.containsValue(couponGoodsCategoryId)) { //指定分类可用
                    //指定分类价格条件
                    if (orderGoodsCategoryFeeMap.containsKey(couponGoodsCategoryId)) {
                        if (orderGoodsCategoryFeeMap.get(couponGoodsCategoryId).compareTo(couponCondition) >= 0) {
                            usableCoupons.add(c);
                            orderUsableCouponMap.put(c.getCouponListId(), c);
                        } else {
                            disableCoupons.add(c);
                        }
                    }
                } else if (!otherOrderCouponIds.contains(c.getId()) && (couponStoreId != null) && couponStoreId == 0 || (c.getUseType() == 0 && Objects.equals(couponStoreId, orderList.get(i).getStoreId()))) {//全平台/指定店铺可用
                    //全平台/指定店铺价格条件
                    if (orderList.get(i).getOrderAmount().compareTo(couponCondition) >= 0) {
                        usableCoupons.add(c);
                        orderUsableCouponMap.put(c.getCouponListId(), c);
                    } else {
                        disableCoupons.add(c);
                    }
                }
            }
            orderList.get(i).setUsableCoupons(usableCoupons);
            orderList.get(i).setDisableCoupons(disableCoupons);
        }
        return orderUsableCouponMap;
    }

    //@PostConstruct
    //public void setCouponStatus() {
    //    redisUtil.del("coupons");
    //    List<Coupon> list = list(new QueryWrapper<Coupon>().eq("status",1));
    //    if (list.size() > 0) {
    //        for (Coupon coupon : list) {
    //            redisUtil.lSet("coupons",coupon);
    //        }
    //    }
    //    log.info("redis放入" + list.size() + "条有效的代金券的记录");
    //    log.info("coupon_list",redisUtil.lGet("coupons",0,-1));
    //}
}
