package com.soubao.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.*;
import com.soubao.service.*;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import com.soubao.vo.StoreAddVo;
import com.soubao.vo.StoreSalesVo;
import com.soubao.vo.StoreSlideVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/store")
public class StoreController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreGradeService storeGradeService;
    @Autowired
    private StoreApplyService storeApplyService;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private MallService mallService;
    @Autowired
    private UserService userService;

    @Autowired
    private UserSellerStoreService userSellerStoreService;

    @ApiOperation(value = "店铺信息", notes = "获取单个店铺记录", httpMethod = "GET")
    @GetMapping
    public Store getOne(@ApiParam("店铺主键") @RequestParam("store_id") Integer storeId) {
        Store store = storeService.getById(storeId);
        if (null!=store && store.getGradeId() > 0){
            store.setStoreGrade(storeGradeService.getById(store.getGradeId()));
        }
        return store;
    }

    @GetMapping("/list")
    public List<Store> getStoreList(
            @RequestParam(value = "store_id", required = false) Set<Integer> storeId,
            @RequestParam(value = "store_name", required = false) String storeName,
            @RequestParam(value = "user_id", required = false) Set<Integer> userIds,
            @RequestParam(value = "sc_id", required = false) Integer scId,
            @RequestParam(value = "limit", required = false) Integer limit) {
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        if (null != storeId && storeId.size() > 0) {
            queryWrapper.in("store_id", storeId);
        }
        if (null != storeName) {
            queryWrapper.like("store_name", storeName);
        }
        if (null != userIds && userIds.size() > 0) {
            queryWrapper.in("user_id", userIds);
        }
        if (null != limit) {
            queryWrapper.last("limit " + limit);
        }
        if (null != scId) {
            queryWrapper.eq("sc_id", scId);
        }
        return storeService.list(queryWrapper);
    }

    @ApiOperation(value = "店铺主键集合", notes = "获取店铺主键集合", httpMethod = "GET")
    @GetMapping("/ids")
    public Set<Integer> storeIds(
            @ApiParam("店铺名称") @RequestParam(value = "store_name", required = false) String storeName,
            @ApiParam("店铺分类id") @RequestParam(value = "sc_id", required = false) Set<Integer> scId) {
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(storeName)){
            queryWrapper.like("store_name", storeName);
        }
        if(null != scId && scId.size() > 0){
            queryWrapper.in("sc_id", scId);
        }
        return storeService.list(queryWrapper).stream().map(Store::getStoreId).collect(Collectors.toSet());
    }

    @GetMapping("page")
    public IPage<Store> page(
            @RequestParam("is_own_shop") Integer isOwnShop,
            @RequestParam(value = "grade_id", required = false) Integer gradeId,
            @RequestParam(value = "sc_id", required = false) Integer scId,
            @RequestParam(value = "store_state", required = false) Integer storeState,
            @RequestParam(value = "seller_name", required = false) String sellerName,
            @RequestParam(value = "store_name", required = false) String storeName,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_own_shop", isOwnShop);
        if (storeState == null || storeState != 5) {
            queryWrapper.eq("deleted", 0);
        }
        if (gradeId != null) {
            queryWrapper.eq("grade_id", gradeId);
        }
        if (scId != null) {
            queryWrapper.eq("sc_id", scId);
        }
        if (storeState != null) { // 即将过期（一个月）
            if (storeState == 3) {
                queryWrapper.between(
                        "store_end_time",
                        System.currentTimeMillis() / 1000,
                        System.currentTimeMillis() / 1000 + 86400 * 30);
            } else if (storeState == 4) { // 已过期
                queryWrapper.between("store_end_time", 1, System.currentTimeMillis() / 1000);
            } else if (storeState == 5) { // 已删除
                queryWrapper.eq("deleted", 1);
            } else {
                queryWrapper.eq("store_state", storeState);
            }
        }
        if (StringUtils.isNotEmpty(sellerName)) {
            queryWrapper.like("seller_name", sellerName);
        }
        if (StringUtils.isNotEmpty(storeName)) {
            queryWrapper.like("store_name", storeName);
        }
        queryWrapper.orderByDesc("store_id");
        IPage<Store> storePage = storeService.page(new Page<>(page, size), queryWrapper);
        storeService.withInviteUser(storePage.getRecords());
        storeService.withSeller(storePage.getRecords());
        storeService.withStoreClass(storePage.getRecords());
        storeService.withStoreGrade(storePage.getRecords());
        storeService.withStoreOrderSum(storePage.getRecords());
        storeService.withStoreMemberCount(storePage.getRecords());
        return storePage;
    }


    /**
     * 获取商品（存证）以及商店的分页
     *
     * @param isOwnShop
     * @param gradeId
     * @param scId
     * @param storeState
     * @param sellerName
     * @param storeName
     * @param page
     * @param size
     * @return
     */
    @GetMapping("page1")
    public List<Store> page1(
            @RequestParam(value = "is_own_shop", required = false) Integer isOwnShop,
            @RequestParam(value = "grade_id", required = false) Integer gradeId,
            @RequestParam(value = "sc_id", required = false) Integer scId,
            @RequestParam(value = "store_state", required = false) Integer storeState,
            @RequestParam(value = "seller_name", required = false) String sellerName,
            @RequestParam(value = "store_name", required = false) String storeName,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        if (null != isOwnShop) {
            queryWrapper.eq("is_own_shop", isOwnShop);
        }
        if (storeState == null || storeState != 5) {
            queryWrapper.eq("deleted", 0);
        }
        if (gradeId != null) {
            queryWrapper.eq("grade_id", gradeId);
        }
        if (scId != null) {
            queryWrapper.eq("sc_id", scId);
        }
        if (storeState != null) { // 即将过期（一个月）
            if (storeState == 3) {
                queryWrapper.between(
                        "store_end_time",
                        System.currentTimeMillis() / 1000,
                        System.currentTimeMillis() / 1000 + 86400 * 30);
            } else if (storeState == 4) { // 已过期
                queryWrapper.between("store_end_time", 1, System.currentTimeMillis() / 1000);
            } else if (storeState == 5) { // 已删除
                queryWrapper.eq("deleted", 1);
            } else {
                queryWrapper.eq("store_state", storeState);
            }
        }
        if (StringUtils.isNotEmpty(sellerName)) {
            queryWrapper.like("seller_name", sellerName);
        }
        if (StringUtils.isNotEmpty(storeName)) {
            queryWrapper.like("store_name", storeName);
        }
        queryWrapper.orderByDesc("store_id");
        IPage<Store> storePage = storeService.page(new Page<>(page, size), queryWrapper);
        return storePage.getRecords();
    }


    @GetMapping("/street")
    @ApiOperation(value = "店铺街", notes = "获取店铺列表和店铺商品数据", httpMethod = "GET")
    public IPage<Store> storeStreet(
            @ApiParam("店铺分类主键") @RequestParam(value = "sc_id", required = false) Integer scId,
            @ApiParam("推荐") @RequestParam(value = "store_recommend", required = false) Integer storeRecommend,
            @ApiParam("地区表省主键") @RequestParam(value = "province_id", required = false) Integer provinceId,
            @ApiParam("地区表市主键") @RequestParam(value = "city_id", required = false) Integer cityId,
            @ApiParam("距离(km)") @RequestParam(value = "distance", required = false) BigDecimal distance,
            @ApiParam("经度") @RequestParam(value = "longitude", required = false) BigDecimal longitude,
            @ApiParam("维度") @RequestParam(value = "latitude", required = false) BigDecimal latitude,
            @ApiParam("排序字段") @RequestParam(value = "order_by", defaultValue = "store_sort") String orderBy,
            @ApiParam("是否升序") @RequestParam(value = "asc", defaultValue = "true") boolean asc,
            @ApiParam("是否删除") @RequestParam(value = "deleted", defaultValue = "0") Integer deleted,
            @ApiParam("0关闭，1开启，2审核中") @RequestParam(value = "store_state", defaultValue = "1") Integer storeState,
            @ApiParam("页码") @RequestParam(value = "p", defaultValue = "1") Integer page,
            @ApiParam("单页记录数") @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Store> storeQueryWrapper = new QueryWrapper<>();
        if (null != scId) {
            storeQueryWrapper.eq("sc_id", scId);
        }
        if (null != storeRecommend) {
            storeQueryWrapper.eq("store_recommend", storeRecommend);
        }
        if (null != provinceId) {
            storeQueryWrapper.eq("province_id", provinceId);
        }
        if (null != cityId) {
            storeQueryWrapper.eq("city_id", cityId);
        }
        if (null != longitude && null != latitude) {
            storeQueryWrapper.select("*, " +
                    "ROUND(SQRT((POW(((#{store.longitude} - longitude) * 111),2)) " +
                    "+ (POW(((#{store.latitude} - latitude) * 111),2))),2) AS distance");
            if (null != distance) {
                storeQueryWrapper.apply("ROUND(SQRT((POW(((#{store.longitude} - longitude) * 111),2)) " +
                        "+ (POW(((#{store.latitude} - latitude) * 111),2))),2) < {0}", distance);
            }
        }
        if (null != orderBy) {
            if (asc) {
                storeQueryWrapper.orderByAsc(orderBy);
            } else {
                storeQueryWrapper.orderByDesc(orderBy);
            }
        }
        storeQueryWrapper.eq("deleted", deleted).eq("store_state", storeState);
        IPage<Store> storeIPage = storeService.page(new Page<>(page, size), storeQueryWrapper);
        if (storeIPage.getRecords().size() > 0) {
            Set<Integer> storeIds = storeIPage.getRecords().stream().map(Store::getStoreId).collect(Collectors.toSet());
            List<StoreGoodsCount> storeGoodsCounts = mallService.getStoreGoodsCounts(storeIds);
            Map<Integer, Integer> storeGoodsCountMap = storeGoodsCounts.stream()
                    .collect(Collectors.toMap(StoreGoodsCount::getStoreId, StoreGoodsCount::getGoodsCount));
            for (Store store : storeIPage.getRecords()) {
                store.setGoodsCount(storeGoodsCountMap.getOrDefault(store.getStoreId(), 0));
                store.setGoodsList(mallService.getGoodsList(store.getStoreId(), 1, 1, 4));
            }
        }
        return storeIPage;
    }

    @GetMapping("default_store")
    public Store defaultStore() {
        return storeService.getOne(
                new QueryWrapper<Store>()
                        .eq("default_store", 1)
                        .eq("store_type", 0)
                        .eq("is_own_shop", 1));
    }

    @PutMapping("default_store")
    public SBApi setDefaultStore(@RequestParam("store_id") Integer storeId, SBApi sbApi) {
        storeService.setDefaultStore(storeId);
        return sbApi;
    }

    @GetMapping("info")
    public Store storeInfo(@RequestParam("store_id") Integer storeId) {
        Store store = storeService.getById(storeId);
        StoreApply storeApply = storeApplyService.getOne(new QueryWrapper<StoreApply>().eq("user_id", store.getUserId()));
        storeApplyService.withRegionsName(storeApply);
        store.setStoreApply(storeApply);
        return store;
    }


    @PostMapping
    public SBApi add(@RequestBody StoreAddVo storeAddVo) {
        storeService.addStore(storeAddVo);
        return new SBApi();
    }

    @PutMapping("/admin")
    public SBApi updateStoreDetail(@RequestBody Store store) {
        storeService.updateStoreDetail(store);
        return new SBApi();
    }

    @PutMapping
    public SBApi update(@RequestBody Store store) {
        storeService.updateStore(store);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping
    public SBApi remove(@RequestParam("store_id") Integer storeId) {
        storeService.removeStore(storeId);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @GetMapping("member_store/page")
    public IPage<Store> memberStorePage(
            @RequestParam(value = "user_id", required = false) Integer userId,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "nickname", required = false) String nickname,
            @RequestParam(value = "store_name", required = false) String storeName,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<Store> storeIPage = new Page<>();
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("invite_user_id", (authenticationFacade.getPrincipal(Seller.class)).getUserId());
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (!StringUtils.isEmpty(storeName)) {
            queryWrapper.like("store_name", storeName);
        }
        Set<Integer> userIds = new HashSet<>();
        if(StringUtils.isNotEmpty(mobile)){
            userIds.addAll(userService.userIdsByMobile(mobile));
        }
        if(StringUtils.isNotEmpty(nickname)){
            userIds.addAll(userService.userIdsByNickname(nickname));
        }
        if (userIds.isEmpty()){
            return storeIPage;
        }else{
            queryWrapper.in("user_id", userIds);
        }
        queryWrapper.orderByDesc("user_id");
        storeIPage = storeService.page(new Page<>(page, size), queryWrapper);
        storeService.withUserInfo(storeIPage.getRecords());
        storeService.withStoreOrderSum(storeIPage.getRecords());
        storeService.withStoreMemberCount(storeIPage.getRecords());
        return storeIPage;
    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @GetMapping("store_sales")
    public StoreSalesVo storeStoreSales() {
        Store store = storeService.getById((authenticationFacade.getPrincipal(Seller.class)).getStoreId());
        StoreSalesVo storeSalesVo = new StoreSalesVo();
        if (StringUtils.isNotEmpty(store.getStorePresales())) {
            storeSalesVo.setPreSalesList(JSON.parseArray(store.getStorePresales(), StoreSales.class));
        }
        if (StringUtils.isNotEmpty(store.getStoreAftersales())) {
            storeSalesVo.setAfterSalesList(JSON.parseArray(store.getStoreAftersales(), StoreSales.class));
        }
        storeSalesVo.setWorkingTime(store.getStoreWorkingtime());
        return storeSalesVo;
    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @PutMapping("store_sales")
    public SBApi updateStoreSales(@RequestBody StoreSalesVo storeServiceVo) {
        storeService.update(new UpdateWrapper<Store>()
                .set("store_presales", JSON.toJSONString(storeServiceVo.getPreSalesList()))
                .set("store_aftersales", JSON.toJSONString(storeServiceVo.getAfterSalesList()))
                .set("store_workingtime", storeServiceVo.getWorkingTime())
                .eq("store_id", (authenticationFacade.getPrincipal(Seller.class)).getStoreId()));
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("slide")
    public StoreSlideVO getSlide() {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        return storeService.getSlide(seller);
    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @PutMapping("slide")
    public SBApi updateSlide(@RequestBody StoreSlideVO storeSlideVO) {
        storeService.update(
                new UpdateWrapper<Store>()
                        .set("store_slide", StringUtils.join(storeSlideVO.getSlides(), ","))
                        .set("store_slide_url", StringUtils.join(storeSlideVO.getSlideUrls(), ","))
                        .eq(
                                "store_id",
                                authenticationFacade.getPrincipal(Seller.class).getStoreId()));
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("mobile_slide")
    public StoreSlideVO getMobileSlide() {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        return storeService.getMobileSlide(seller);
    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @PutMapping("mobile_slide")
    public SBApi updateMobileSlide(@RequestBody StoreSlideVO storeSlideVO) {
        storeService.update(
                new UpdateWrapper<Store>()
                        .set("mb_slide", StringUtils.join(storeSlideVO.getSlides(), ","))
                        .set("mb_slide_url", StringUtils.join(storeSlideVO.getSlideUrls(), ","))
                        .eq(
                                "store_id",
                                authenticationFacade.getPrincipal(Seller.class).getStoreId()));
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("point")
    public String getPoint() {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        Store store = storeService.getById(seller.getStoreId());
        return store.getLongitude().toPlainString() + "," + store.getLatitude().toPlainString();
    }

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @PutMapping("point")
    public SBApi updatePoint(@RequestParam("point") String point) {
        String[] longitudeAndLatitude = point.split(",");
        storeService.update(
                new UpdateWrapper<Store>()
                        .set("longitude", longitudeAndLatitude[0])
                        .set("latitude", longitudeAndLatitude[1])
                        .eq(
                                "store_id",
                                authenticationFacade.getPrincipal(Seller.class).getStoreId()));
        return new SBApi();
    }

    /**
     * 修改商店的交易数量
     *  type 0加 1减
     * @param storeId
     * @param userId
     */
    @PutMapping("/store_transaction_num")
    void updateStoreTransactionNum(@RequestParam("type") Integer type,
                                   @RequestParam("store_id") Integer storeId,
                                   @RequestParam("user_id") Integer userId) {
        if (type == 0) {
            // 修改卖家
            storeService.update(new UpdateWrapper<Store>().setSql("transaction_num=transaction_num+1").eq("store_id",storeId));
            // 修改买家
            UserSellerStore userSellerStore = userSellerStoreService.getOne(new QueryWrapper<UserSellerStore>().eq("user_id", userId));
            storeService.update(new UpdateWrapper<Store>().setSql("transaction_num=transaction_num+1").eq("store_id",userSellerStore.getStoreId()));
        } else if (type == 1) {
            // 修改卖家
            storeService.update(new UpdateWrapper<Store>().setSql("transaction_num=transaction_num-1").eq("store_id",storeId));
            // 修改买家
            UserSellerStore userSellerStore = userSellerStoreService.getOne(new QueryWrapper<UserSellerStore>().eq("user_id", userId));
            storeService.update(new UpdateWrapper<Store>().setSql("transaction_num=transaction_num-1").eq("store_id",userSellerStore.getStoreId()));
        }

    }
}
