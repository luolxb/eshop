package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.StoreApplyMapper;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
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
 * @since 2019-12-06
 */
@Service
public class StoreApplyServiceImpl extends ServiceImpl<StoreApplyMapper, StoreApply> implements StoreApplyService {
    @Autowired
    private UserService userService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private MallService mallService;

    @Override
    public void storeAudit(StoreApply storeApply) {
        if (StringUtils.isEmpty(storeApply.getStoreName())) {
            throw new ShopException(ResultEnum.STORE_NAME_NULL);
        }
        updateById(storeApply);
        if (storeApply.getApplyState() == 1) { //同意开店 审核通过
            User user = userService.getUserById(storeApply.getUserId());
            if (user == null) {
                throw new ShopException(ResultEnum.APPLY_ACCOUNT_NOT_EXISTS);
            }
            Seller seller = sellerService.getOne(new QueryWrapper<Seller>().eq("seller_name", storeApply.getSellerName()));
            if (seller != null) {
                throw new ShopException(ResultEnum.SELLER_NAME_EXISTS);
            }
            //开店时长
            Long storeTime = System.currentTimeMillis() / 1000 + 60 * 60 * 24 * 365 * storeApply.getStoreEndTime();
            Store store = new Store();
            store.setUserId(storeApply.getUserId());
            store.setSellerName(storeApply.getSellerName());
            store.setUserName(StringUtils.isEmpty(user.getEmail()) ? user.getMobile() : user.getEmail());
            store.setGradeId(storeApply.getSgId() == null ? 1 : storeApply.getSgId());
            store.setStoreName(storeApply.getStoreName());
            store.setScId(storeApply.getScId());
            store.setCompanyName(storeApply.getCompanyName());
            store.setStorePhone(storeApply.getStorePersonMobile());
            store.setStoreAddress(StringUtils.isEmpty(storeApply.getStoreAddress()) ? "待完善" : storeApply.getStoreAddress());
            store.setStoreTime(System.currentTimeMillis() / 1000);
            store.setStoreState(1);
            store.setStoreQq(storeApply.getStorePersonQq());
            store.setStoreEndTime(storeTime);
            store.setProvinceId(storeApply.getCompanyProvince());
            store.setCityId(storeApply.getCompanyCity());
            store.setDistrict(storeApply.getCompanyDistrict());
            store.setInviteUserId(storeApply.getInviteUserId());
            store.setStorePresales("");
            store.setStoreAftersales("");
            if (storeService.save(store)) {//通过审核开通店铺
                Seller s = new Seller();
                s.setSellerName(storeApply.getSellerName());
                s.setUserId(storeApply.getUserId());
                s.setGroupId(0);
                s.setStoreId(store.getStoreId());
                s.setIsAdmin(1);
                s.setAddTime(System.currentTimeMillis() / 1000);
                sellerService.save(s);
                userService.updateStoreMember(storeApply.getUserId(), store.getStoreId());
                //绑定商家申请类目
                if (!StringUtils.isEmpty(storeApply.getStoreClassIds())) {
                    // TODO 绑定商家申请类目

                }
            }
            // TODO 新增管理员日志

            // TODO 审核通过  调用iM接口 添加店铺与企业管理人

        } else if (storeApply.getApplyState() == 2) {
            // 管理员日志 审核拒绝
        }
    }

    @Override
    public void add(User user, StoreApply storeApply) {
        //检查店铺名称是否重复
        StoreApply sa = getOne(new QueryWrapper<StoreApply>().eq("user_id", user.getUserId()));
        if (sa != null) {
            throw new ShopException(ResultEnum.FAIL);
        }
        storeApply.setUserId(user.getUserId());
        storeApply.setApplyType(0);
        storeApply.setAddTime(System.currentTimeMillis() / 1000);
        save(storeApply);
    }

    @Override
    public void updateByUser(User user, StoreApply storeApply) {
        //检查店铺名称是否重复
        if (storeApply.getStoreName() != null) {
            if (count(new QueryWrapper<StoreApply>().eq("store_name", storeApply.getStoreName()).ne("user_id", user.getUserId())) > 0) {
                throw new ShopException(ResultEnum.STORE_NAME_EXISTS);
            }
        }
        //JSON字符串化已选择经营类目
        if (storeApply.getStoreClassIdList() != null && storeApply.getStoreClassIdList().size() > 0) {
            storeApply.setStoreClassIds(String.join(",", storeApply.getStoreClassIdList()));
        }
        storeApply.setApplyState(0);//每次提交资料回到待审核状态
        storeApply.setAddTime(System.currentTimeMillis() / 1000);
        update(storeApply, new UpdateWrapper<StoreApply>().eq("user_id", user.getUserId()));
    }

    @Override
    public void withUser(List<StoreApply> records) {
        if (!records.isEmpty()) {
            Set<Integer> userIds = records.stream().map(StoreApply::getInviteUserId).collect(Collectors.toSet());
            Map<Integer, User> userMap = userService.users(userIds).stream().collect(Collectors.toMap(User::getUserId, user -> user));
            records.forEach(storeApply -> {
                storeApply.setInviteUser(userMap.get(storeApply.getInviteUserId()));
            });
        }
    }

    @Override
    public void withRegionsName(StoreApply storeApply) {
        if (storeApply != null){
            HashSet<Integer> regionIds = new HashSet<>();
            regionIds.add(storeApply.getCompanyProvince());
            regionIds.add(storeApply.getCompanyCity());
            regionIds.add(storeApply.getCompanyDistrict());
            regionIds.add(storeApply.getBankProvince());
            regionIds.add(storeApply.getBankCity());
            Map<Integer, String> regionMap = mallService.listRegion(regionIds)
                    .stream().collect(Collectors.toMap(Region::getId, Region::getName));
            storeApply.setProvinceName(regionMap.get(storeApply.getCompanyProvince()));
            storeApply.setCityName(regionMap.get(storeApply.getCompanyCity()));
            storeApply.setDistrictName(regionMap.get(storeApply.getCompanyDistrict()));
            storeApply.setBankProvinceName(regionMap.get(storeApply.getBankProvince()));
            storeApply.setBankCityName(regionMap.get(storeApply.getBankCity()));
        }
    }
}
