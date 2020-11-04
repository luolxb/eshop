package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.MallService;
import com.soubao.service.OrderGoodsService;
import com.soubao.service.ReturnGoodsService;
import com.soubao.service.SellerService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/return_goods")
public class ReturnGoodsController {
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private ReturnGoodsService returnGoodsService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private MallService mallService;
    @Autowired
    private SellerService sellerService;

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("订单商品退货记录")
    @GetMapping("page/user")
    public Page<ReturnGoods> getUserReturnGoodsPage(
            @RequestParam(value = "status", required = false) Set<Integer> status,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        User user = authenticationFacade.getPrincipal(User.class);
        QueryWrapper<ReturnGoods> returnGoodsQueryWrapper = new QueryWrapper<>();
        returnGoodsQueryWrapper.eq("user_id", user.getUserId());
        if (status != null && status.size() > 0) {
            returnGoodsQueryWrapper.eq("status", status);
        }
        IPage<ReturnGoods> returnGoodsIPage = returnGoodsService.page(new Page<>(page, size), returnGoodsQueryWrapper);
        returnGoodsService.withOrderGoods(returnGoodsIPage.getRecords());
        return (Page<ReturnGoods>) returnGoodsIPage;
    }

    @GetMapping("page/store")
    public IPage<ReturnGoods> getStoreReturnGoodsPage(
            @RequestParam(value = "store_id") Integer storeId,
            @RequestParam(value = "type", required = false) Set<Integer> type,
            @RequestParam(value = "status", required = false) Set<Integer> status,
            @RequestParam(value = "order_sn", required = false) String orderSn,
            @RequestParam(value = "add_time_begin", required = false) Long addTimeBegin,
            @RequestParam(value = "add_time_end", required = false) Long addTimeEnd,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<ReturnGoods> returnGoodsQueryWrapper = new QueryWrapper<>();
        returnGoodsQueryWrapper.eq("store_id", storeId).orderByDesc("id");
        if (!CollectionUtils.isEmpty(type)) {
            returnGoodsQueryWrapper.in("type", type);
        }
        if (!CollectionUtils.isEmpty(status)) {
            returnGoodsQueryWrapper.eq("status", status);
        }
        if (addTimeBegin != null && addTimeEnd != null) {
            returnGoodsQueryWrapper.between("addtime", addTimeBegin, addTimeEnd);
        }
        if (StringUtils.isNotEmpty(orderSn)) {
            returnGoodsQueryWrapper.eq("order_sn", orderSn);
        }
        IPage<ReturnGoods> returnGoodsIPage = returnGoodsService.page(new Page<>(page, size), returnGoodsQueryWrapper);
        returnGoodsService.withUser(returnGoodsIPage.getRecords());
        returnGoodsService.withOrderGoods(returnGoodsIPage.getRecords());
        return returnGoodsIPage;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("page/admin")
    public IPage<ReturnGoods> getAdminReturnGoodsPage(
            @RequestParam(value = "goods_name", required = false) String goodsName,
            @RequestParam(value = "store_name", required = false) String storeName,
            @RequestParam(value = "status", required = false) Set<Integer> status,
            @RequestParam(value = "add_time_begin", required = false) Long addTimeBegin,
            @RequestParam(value = "add_time_end", required = false) Long addTimeEnd,
            @RequestParam(value = "order_sn", required = false) String orderSn,
            @RequestParam(value = "types", required = false) Set<Integer> types,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<ReturnGoods> returnGoodsQueryWrapper = new QueryWrapper<>();
        returnGoodsQueryWrapper.orderByDesc("id");
        IPage<ReturnGoods> returnGoodsIPage = new Page<>();
        if (StringUtils.isNotEmpty(goodsName)) {
            Set<Integer> goodsIds = mallService.goodsIdsByGoodsName(goodsName);
            if (goodsIds.size() == 0) {
                return returnGoodsIPage;
            } else {
                returnGoodsQueryWrapper.in("goods_id", goodsIds);
            }
        }
        if (StringUtils.isNotEmpty(storeName)) {
            Set<Integer> storeIds = sellerService.storeIdsByStoreName(storeName);
            if (storeIds.isEmpty()) {
                return returnGoodsIPage;
            } else {
                returnGoodsQueryWrapper.in("store_id", storeIds);
            }
        }
        if (!CollectionUtils.isEmpty(status)) {
            returnGoodsQueryWrapper.in("status", status);
        }
        if (null != addTimeBegin && null != addTimeEnd) {
            returnGoodsQueryWrapper.between("addtime", addTimeBegin, addTimeEnd);
        }
        if (StringUtils.isNotEmpty(orderSn)) {
            returnGoodsQueryWrapper.eq("order_sn", orderSn);
        }
        if (!CollectionUtils.isEmpty(types)) {
            returnGoodsQueryWrapper.in("type", types);
        }
        returnGoodsIPage = returnGoodsService.page(new Page<>(page, size), returnGoodsQueryWrapper);
        returnGoodsService.withStore(returnGoodsIPage.getRecords());
        returnGoodsService.withUser(returnGoodsIPage.getRecords());
        returnGoodsService.withOrderGoods(returnGoodsIPage.getRecords());
        return returnGoodsIPage;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("订单商品退货记录")
    @GetMapping
    public ReturnGoods getReturnGoods(
            @RequestParam(value = "rec_id") Integer recId) {
        User user = authenticationFacade.getPrincipal(User.class);
        QueryWrapper<ReturnGoods> returnGoodsQueryWrapper = new QueryWrapper<>();
        returnGoodsQueryWrapper.eq("rec_id", recId);
        returnGoodsQueryWrapper.eq("user_id", user.getUserId());
        return returnGoodsService.getOne(returnGoodsQueryWrapper.orderByDesc("id").last("limit 1"));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @ApiOperation("退货记录")
    @GetMapping("/admin")
    public ReturnGoods getOne(
            @RequestParam(value = "id") Integer id) {
        return returnGoodsService.getById(id);
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @ApiOperation("卖家的用户退货记录")
    @GetMapping("/seller")
    public ReturnGoods getSellerReturnGoods(
            @RequestParam(value = "id") Integer id) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        QueryWrapper<ReturnGoods> returnGoodsQueryWrapper = new QueryWrapper<>();
        returnGoodsQueryWrapper.eq("store_id", seller.getStoreId()).eq("id", id);
        return returnGoodsService.getOne(returnGoodsQueryWrapper);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("订单商品添加售后")
    @PostMapping
    public int addReturnGoods(@RequestBody ReturnGoods requestReturnGoods) {
        User user = authenticationFacade.getPrincipal(User.class);
        requestReturnGoods.setUserId(user.getUserId());
        OrderGoods orderGoods = orderGoodsService.getOne((new QueryWrapper<OrderGoods>())
                .eq("rec_id", requestReturnGoods.getRecId()));
        return returnGoodsService.addReturnGoods(orderGoods, requestReturnGoods);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("订单商品取消售后")
    @PutMapping("status/-2")
    public SBApi returnGoodsCancel(@RequestParam(value = "id") Integer id) {
        User user = authenticationFacade.getPrincipal(User.class);
        ReturnGoods returnGoods = returnGoodsService.getOne((new QueryWrapper<ReturnGoods>())
                .eq("id", id).eq("user_id", user.getUserId()));
        if (returnGoods != null) {
            returnGoods.setStatus(-2);
            returnGoods.setCanceltime(System.currentTimeMillis() / 1000);
            returnGoodsService.updateById(returnGoods);
        }
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("status/5")
    public SBApi returnGoodsRefund(@RequestBody ReturnGoods returnGoodsRequest) {
        ReturnGoods returnGoods = returnGoodsService.getOne((new QueryWrapper<ReturnGoods>()).eq("id", returnGoodsRequest.getId()));
        returnGoods.setRefundMark(returnGoodsRequest.getRefundMark());
        returnGoods.setRefundType(returnGoodsRequest.getRefundType());
        returnGoodsService.refund(returnGoods);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @ApiOperation("售后单同意操作")
    @PutMapping("/status/1")
    public SBApi returnGoodsAgree(@RequestBody ReturnGoods returnGoodsRequest) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        ReturnGoods returnGoods = returnGoodsService.getOne(new QueryWrapper<ReturnGoods>()
                .eq("store_id", seller.getStoreId()).eq("id", returnGoodsRequest.getId()));

        if(!returnGoods.getIsCanAgreeOrRefuse()){
            throw new ShopException(ResultEnum.NOT_AGREE_REFUND);
        }
        if (returnGoodsRequest.getRefundMoney().add(returnGoodsRequest.getRefundDeposit())
                .compareTo(returnGoods.getRefundMoney().add(returnGoods.getRefundDeposit())) > 0) {
            throw new ShopException(ResultEnum.REFUND_MONEY_MORE_THAN_PAY_MONEY);
        }
        ReturnGoods updateReturnGoods = new ReturnGoods();
        updateReturnGoods.setId(returnGoods.getId());
        updateReturnGoods.setStatus(1);
        updateReturnGoods.setChecktime(System.currentTimeMillis() / 1000);
        updateReturnGoods.setRemark(returnGoodsRequest.getRemark());
        if (returnGoods.getIsReceive() == 0 || returnGoods.getType() == 0) {
            updateReturnGoods.setStatus(3);
        }
        if(returnGoodsRequest.getRefundMoney().compareTo(returnGoods.getRefundMoney()) != 0){
            updateReturnGoods.setGap(returnGoods.getRefundMoney().subtract(returnGoodsRequest.getRefundMoney()));
        }
        returnGoodsService.updateById(updateReturnGoods);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("售后单退货")
    @PutMapping("/status/2")
    public SBApi returnGoods(@RequestBody ReturnGoods returnGoodsRequest) {
        User user = authenticationFacade.getPrincipal(User.class);
        ReturnGoods returnGoods = returnGoodsService.getOne((new QueryWrapper<ReturnGoods>())
                .eq("id", returnGoodsRequest.getId()).eq("user_id", user.getUserId()));
        if (!returnGoods.getIsCanReturn()) {
            throw new ShopException(ResultEnum.NOT_RETURN_GOODS);
        }
        ReturnGoods updateReturnGoods = new ReturnGoods();
        updateReturnGoods.setId(returnGoods.getId());
        updateReturnGoods.setStatus(2);
        //还有发货信息要填，php的序列化，要新增一张表
        returnGoodsService.updateById(updateReturnGoods);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @ApiOperation("售后单拒绝操作")
    @PutMapping("/status/-1")
    public SBApi returnGoodsRefuse(@RequestBody ReturnGoods returnGoodsRequest) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        ReturnGoods returnGoods = returnGoodsService.getOne(new QueryWrapper<ReturnGoods>()
                .eq("store_id", seller.getStoreId()).eq("id", returnGoodsRequest.getId()));
        if(!returnGoods.getIsCanAgreeOrRefuse()){
            throw new ShopException(ResultEnum.NOT_AGREE_REFUND);
        }
        ReturnGoods updateReturnGoods = new ReturnGoods();
        updateReturnGoods.setId(returnGoods.getId());
        updateReturnGoods.setStatus(-1);
        updateReturnGoods.setChecktime(System.currentTimeMillis() / 1000);
        updateReturnGoods.setRemark(returnGoodsRequest.getRemark());
        returnGoodsService.updateById(updateReturnGoods);
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @PutMapping("/status/3")
    public SBApi receive(@RequestBody ReturnGoods returnGoodsRequest) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        ReturnGoods returnGoods = returnGoodsService.getOne(new QueryWrapper<ReturnGoods>()
                .eq("store_id", seller.getStoreId()).eq("id", returnGoodsRequest.getId()));
        if(returnGoods.getStatus() != 2){
            throw new ShopException(ResultEnum.FAIL);
        }
        ReturnGoods updateReturnGoods = new ReturnGoods();
        updateReturnGoods.setId(returnGoods.getId());
        updateReturnGoods.setStatus(3);
        updateReturnGoods.setReceivetime(System.currentTimeMillis() / 1000);
        returnGoodsService.updateById(updateReturnGoods);
        return new SBApi();
    }

    @GetMapping("report")
    public Map<String, Object> report(@RequestParam(value = "store_id", required = false) Integer storeId) {
        Map<String, Object> report = new HashMap<>();
        QueryWrapper<ReturnGoods> baseQueryWrapper = new QueryWrapper<>();
        if (storeId != null) {
            baseQueryWrapper.eq("store_id", storeId);
        }
        report.put("refund_pay_count", returnGoodsService.count(baseQueryWrapper.clone().lt("type", 2)));
        report.put("change_goods_count", returnGoodsService.count(baseQueryWrapper.clone().gt("type", 1)));
        report.put("wait_refund_count", returnGoodsService.count(baseQueryWrapper.clone().gt("status", 0)));
        return report;
    }

}
