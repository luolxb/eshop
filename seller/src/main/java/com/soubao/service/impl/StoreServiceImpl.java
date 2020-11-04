package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.StoreMapper;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.*;
import com.soubao.vo.StoreAddVo;
import com.soubao.vo.StoreSlideVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 店铺数据表 服务实现类
 *
 * @author dyr
 * @since 2019-11-19
 */
@Service("storeService")
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {
    @Autowired
    private SellerService sellerService;
    @Autowired
    private UserService userService;
    @Autowired
    private StoreExtendService storeExtendService;
    @Autowired
    private StoreApplyService storeApplyService;
    @Autowired
    private StoreClassService storeClassService;
    @Autowired
    private StoreGradeService storeGradeService;
    @Autowired
    private MallService mallService;
    @Autowired
    private OrderService orderService;


    public void withInviteUser(List<Store> storeList) {
        // 获取店铺推荐用户id集合并过滤默认为0的id
        Set<Integer> userIds = storeList.stream()
                .filter(store -> store.getInviteUserId() != 0)
                .map(Store::getInviteUserId)
                .collect(Collectors.toSet());
        if (!userIds.isEmpty()) {
            Map<Integer, User> userMap = userService.users(userIds)
                    .stream().collect(Collectors.toMap(User::getUserId, user -> user));
            storeList.forEach(store -> {
                store.setInviteUser(userMap.get(store.getInviteUserId()));
            });
        }
    }

    @Override
    public void withStoreOrderSum(List<Store> records) {
        if (!records.isEmpty()){
            Set<Integer> storeIds = records.stream().map(Store::getStoreId).collect(Collectors.toSet());
            Map<Integer, Set<Integer>> storeMemberIds = userService.getUserIdsByIsStoreMember(storeIds);
            if (storeMemberIds.size() > 0) {
                Set<Integer> userIds = new HashSet<>();
                storeMemberIds.forEach( (k, v) -> {
                    userIds.addAll(v);
                });
                Map<Integer, BigDecimal> userOrderSumMap = orderService.getUserOrderSumByUserIds(userIds);
                if (!userOrderSumMap.isEmpty()){
                    for (Store store : records) {
                        if (storeMemberIds.containsKey(store.getStoreId())){
                            Set<Integer> memberIds = storeMemberIds.get(store.getStoreId());
                            BigDecimal storeOrderSum = new BigDecimal(BigInteger.ZERO);
                            for (Integer userId: memberIds) {
                                if(userOrderSumMap.containsKey(userId)){
                                    storeOrderSum = storeOrderSum.add(userOrderSumMap.get(userId));
                                }
                            }
                            store.setStoreOrderSum(storeOrderSum);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void withStoreMemberCount(List<Store> records) {
        if (!records.isEmpty()){
            Set<Integer> storeIds = records.stream().map(Store::getStoreId).collect(Collectors.toSet());
            Map<Integer, Integer> storeMemberCountGroupMap = userService.getStoreMemberCountGroup(storeIds);
            if (storeMemberCountGroupMap != null && !storeMemberCountGroupMap.isEmpty()){
                records.forEach(store -> {
                    store.setStoreMemberCount(storeMemberCountGroupMap.get(store.getStoreId()));
                });
            }
        }
    }

    @Override
    public void withSeller(List<Store> records) {
        if (!records.isEmpty()){
            Set<Integer> storeIds = records.stream().map(Store::getStoreId).collect(Collectors.toSet());
            Map<Integer, Seller> sellerMap = sellerService.list(new QueryWrapper<Seller>()
                    .eq("is_admin", 1).in("store_id", storeIds).select("store_id, seller_name"))
                    .stream().collect(Collectors.toMap(Seller::getStoreId, seller -> seller));
            records.forEach(store -> {
                store.setSeller(sellerMap.get(store.getStoreId()));
            });
        }
    }

    @Override
    public void withStoreClass(List<Store> records) {
        if (!records.isEmpty()){
            Set<Integer> scIds = records.stream().map(Store::getScId).collect(Collectors.toSet());
            if(scIds.contains(0) && scIds.size() == 1){
                return;
            }
            Map<Integer, StoreClass> storeClassMap = storeClassService.listByIds(scIds)
                    .stream().collect(Collectors.toMap(StoreClass::getScId, storeClass -> storeClass));
            records.forEach(store -> {
                store.setStoreClass(storeClassMap.getOrDefault(store.getScId(), null));
            });
        }
    }

    @Override
    public void withStoreGrade(List<Store> records) {
        if (!records.isEmpty()){
            Set<Integer> gradeIds = records.stream().map(Store::getGradeId).collect(Collectors.toSet());
            Map<Integer, StoreGrade> storeGradeMap = storeGradeService.listByIds(gradeIds)
                    .stream().collect(Collectors.toMap(StoreGrade::getSgId, storeGrade -> storeGrade));
            records.forEach(store -> {
                store.setStoreGrade(storeGradeMap.get(store.getGradeId()));
            });
        }
    }

    @Override
    public void withUserInfo(List<Store> records) {
        if (!records.isEmpty()){
            Set<Integer> userIds = records.stream().map(Store::getUserId).collect(Collectors.toSet());
            if (!userIds.isEmpty()){
                Map<Integer, User> userMap = userService.users(userIds)
                        .stream().collect(Collectors.toMap(User::getUserId, user -> user));
                records.forEach(store -> {
                    User user = userMap.get(store.getUserId());
                    store.setNickname(user.getNickName());
                    store.setMobile(user.getMobile());
                    store.setRegTime(user.getRegTime());
                });
            }
        }
    }

    @Override
    public StoreSlideVO getSlide(Seller seller) {
        StoreSlideVO storeSlideVO = new StoreSlideVO();
        Store store = getById(seller.getStoreId());
        String storeSlide = store.getStoreSlide();
        String storeSlideUrl = store.getStoreSlideUrl();
        if (!StringUtils.isEmpty(storeSlide)) {
            storeSlideVO.setSlides(storeSlide.split(","));
        } else {
            storeSlideVO.setSlides(new String[5]);
        }
        if (!StringUtils.isEmpty(storeSlideUrl)) {
            storeSlideVO.setSlideUrls(storeSlideUrl.split(","));
        } else {
            storeSlideVO.setSlideUrls(new String[5]);
        }
        return storeSlideVO;
    }

    @Override
    public StoreSlideVO getMobileSlide(Seller seller) {
        StoreSlideVO storeSlideVO = new StoreSlideVO();
        Store store = getById(seller.getStoreId());
        String mbSlide = store.getMbSlide();
        String mbSlideUrl = store.getMbSlideUrl();
        if (!StringUtils.isEmpty(mbSlide)) {
            storeSlideVO.setSlides(mbSlide.split(","));
        } else {
            storeSlideVO.setSlides(new String[5]);
        }
        if (!StringUtils.isEmpty(mbSlideUrl)) {
            storeSlideVO.setSlideUrls(mbSlideUrl.split(","));
        } else {
            storeSlideVO.setSlideUrls(new String[5]);
        }
        return storeSlideVO;
    }

    @Override
    public Integer addStore(StoreAddVo storeAddVo) {
        if (StringUtils.isEmpty(storeAddVo.getPassword())
                || storeAddVo.getPassword().trim().length() < 6) {
            throw new ShopException(ResultEnum.PWD_LENGTH_ERROR);
        }
        int count = count(new QueryWrapper<Store>().eq("store_name", storeAddVo.getStoreName()).eq("deleted", 0));
        if (count > 0) {
            throw new ShopException(ResultEnum.STORE_NAME_EXISTS);
        }
        Seller seller = sellerService.getOne(new QueryWrapper<Seller>()
                .eq("seller_name", storeAddVo.getSellerName()), false);
        if (seller != null) {
            int countTemp = count(new QueryWrapper<Store>().eq("store_id", seller.getStoreId()).eq("deleted", 0));
            if (countTemp > 0) {
                throw new ShopException(ResultEnum.SELLER_NAME_EXISTS);
            }
        }
        User user = userService.getUserByMobileOrEmail(storeAddVo.getMobile(), storeAddVo.getEmail());
        Integer userId;
        if (user != null) {
            int countTemp = count(new QueryWrapper<Store>().eq("user_id", user.getUserId()).eq("deleted", 0));
            if (countTemp > 0) {
                throw new ShopException(ResultEnum.STORE_EXISTS);
            }
            userId = user.getUserId();
        } else {
            // 创建用户
            UserInfo newUser = new UserInfo();
            if (StringUtils.isNotEmpty(storeAddVo.getMobile())){
                newUser.setNickName(storeAddVo.getMobile());
            }else{
                newUser.setNickName(storeAddVo.getEmail());
            }
            newUser.setEmail(storeAddVo.getEmail());
            newUser.setMobile(storeAddVo.getMobile());
            newUser.setPassword(storeAddVo.getPassword());
            newUser.setRegTime(System.currentTimeMillis() / 1000);
            userId = userService.addInnerSeller(newUser);

        }
        // 添加店铺信息
        Store store = new Store();
        store.setStoreName(storeAddVo.getStoreName());
        store.setUserId(userId);
        store.setUserName(!StringUtils.isEmpty(storeAddVo.getMobile()) ? storeAddVo.getMobile() : storeAddVo.getEmail());
        store.setStoreState(1);
        store.setGradeId(1);
        store.setSellerName(storeAddVo.getSellerName());
        store.setStoreTime(System.currentTimeMillis() / 1000);
        store.setIsOwnShop(storeAddVo.getIsOwnShop());
        store.setStorePresales("");
        store.setStoreAftersales("");
        store.setStorePhone(storeAddVo.getMobile());
        save(store);
        Integer storeId = store.getStoreId();
        int count2 = storeExtendService.count(new QueryWrapper<StoreExtend>().eq("store_id", storeId));
        if (count2 == 0) {
            StoreExtend storeExtend = new StoreExtend();
            storeExtend.setStoreId(storeId);
            storeExtend.setExpress("");
            storeExtend.setPricerange("");
            storeExtend.setOrderpricerange("");
            storeExtendService.save(storeExtend);
        }
        if (storeAddVo.getIsOwnShop() == 0) {
            // 添加驻外店铺
            StoreApply storeApply = StoreApply.builder().sellerName(storeAddVo.getSellerName()).userId(userId)
                    .storeName(storeAddVo.getStoreName()).companyProvince(0).scBail(0).applyState(1).storeClassIds("").build();
            storeApplyService.save(storeApply);
        }
        // 添加店铺管理员
        Seller s = new Seller();
        s.setSellerName(storeAddVo.getSellerName());
        s.setStoreId(storeId);
        s.setUserId(userId);
        s.setIsAdmin(1);
        sellerService.save(s);
        return storeId;
    }

    @Override
    public void updateStoreDetail(Store store) {
        if (store.getStoreApply().getCompanyCity() < 1
                || store.getStoreApply().getCompanyDistrict() < 1) {
            throw new ShopException(ResultEnum.ADDRESS_ERROR);
        }
        updateById(store);
        StoreApply sa = storeApplyService.getOne(new QueryWrapper<StoreApply>().eq("user_id", store.getUserId()));
        if (sa == null) {
            StoreApply storeApply = store.getStoreApply();
            if(null == storeApply.getStoreClassIds()){
                storeApply.setStoreClassIds("");
            }
            storeApplyService.save(storeApply);
        } else {
            if (store.getStoreApply().getScId() > 0) {
                StoreClass storeClass = storeClassService.getById(store.getStoreApply().getScId());
                store.getStoreApply().setScName(storeClass.getScName());
            }
            storeApplyService.updateById(store.getStoreApply());
        }
        if (store.getStoreState() == 0) {
            // 关闭店铺，同时下架店铺所有商品
            mallService.updateIsOnSale(store.getStoreId());
        }
    }

    @Override
    public void updateStore(Store store) {
        String storeDomain = store.getStoreDomain();
        if (!StringUtils.isEmpty(storeDomain)) {
            if (storeDomain.equals("www")) {
                throw new ShopException(ResultEnum.SUBDOMAINS_ERROR);
            }
            String hostAddress = "";
            try {
                hostAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            if (storeDomain.equals(hostAddress.split("\\.")[0])) {
                throw new ShopException(ResultEnum.SUBDOMAINS_EQUALS_DOMAIN);
            }
            Store s = getOne(new QueryWrapper<Store>().eq("store_domain", storeDomain)
                    .ne("store_id", store.getStoreId()));
            if (s != null) {
                throw new ShopException(ResultEnum.SUBDOMAINS_EXISTS);
            }
        }
        updateById(store);
    }

    @Override
    public void removeStore(Integer storeId) {
        if (storeId == 1) {
            throw new ShopException(ResultEnum.BASIC_STORE_CANNOT_REMOVE);
        }
        Store store = getById(storeId);
        int storeGoodsCount = mallService.getStoreGoodsCount(storeId);
        if (storeGoodsCount > 0) {
            throw new ShopException(ResultEnum.STORE_GOODS_EXISTS);
        }
        update(new UpdateWrapper<Store>().set("deleted", 1).set("store_state", 0).eq("store_id", storeId));
        sellerService.update(new UpdateWrapper<Seller>().set("enabled", 1).eq("store_id", storeId));
        userService.updateStoreMember(store.getUserId(), null);
        storeApplyService.update(
                new UpdateWrapper<StoreApply>().set("apply_state", 2).eq("user_id", store.getUserId()));
    }

    @Override
    public void setDefaultStore(Integer storeId) {
        int count = count(new QueryWrapper<Store>()
                .eq("is_own_shop", 1)
                .eq("store_type", 0)
                .eq("default_store", 1));
        if (count > 0) {
            throw new ShopException(ResultEnum.DEFAULT_STORE_EXISTS);
        }
        update(new UpdateWrapper<Store>().set("default_store", 1).eq("is_own_shop", 1)
                .eq("store_type", 0).eq("store_id", storeId));
    }
}
