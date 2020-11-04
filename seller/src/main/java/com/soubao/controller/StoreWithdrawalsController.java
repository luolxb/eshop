package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Store;
import com.soubao.entity.StoreWithdrawals;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.StoreService;
import com.soubao.service.StoreWithdrawalsService;
import com.soubao.common.utils.TimeUtil;
import com.soubao.utils.excel.ExcelUtil;
import com.soubao.common.vo.SBApi;
import com.soubao.vo.StoreWithdrawalsExcel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2019-12-18
 */
@RestController
@RequestMapping("store_withdrawals")
public class StoreWithdrawalsController {

    @Autowired
    private StoreWithdrawalsService storeWithdrawalsService;
    @Autowired
    private StoreService storeService;

    @GetMapping("page")
    public IPage<StoreWithdrawals> getPage(@RequestParam(value = "store_id", required = false) Integer storeId,
                                           @RequestParam(value = "start_time", required = false) Integer startTime,
                                           @RequestParam(value = "end_time", required = false) Integer endTime,
                                           @RequestParam(value = "status", required = false) Integer status,
                                           @RequestParam(value = "bank_card", required = false) String bankCard,
                                           @RequestParam(value = "realname", required = false) String realname,
                                           @RequestParam(value = "page", defaultValue = "1") Integer page,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return storeWithdrawalsService.getStoreWithdrawalsPage(new Page<>(page, size), storeId,startTime,endTime,status,bankCard,realname);
    }

    @GetMapping
    public StoreWithdrawals getStoreWithdrawalsById(@RequestParam("id") Integer id) {
        return storeWithdrawalsService.getStoreWithdrawalsById(id);
    }

    @PostMapping
    public SBApi addStoreWithdrawals(@Valid @RequestBody StoreWithdrawals storeWithdrawals) {
        Store store = storeService.getById(storeWithdrawals.getStoreId());
        storeWithdrawals.setCreateTime(System.currentTimeMillis() / 1000);
        if (store.getStoreMoney().compareTo(storeWithdrawals.getMoney()) < 0) {
            throw new ShopException(ResultEnum.STORE_WITHDRAWALS_MONEY);
        }

        // 20200916. 需求商店取款（新增申请取款记录就直接扣除商店余额）
        BigDecimal storeMoney = store.getStoreMoney().subtract(storeWithdrawals.getMoney());
        store.setStoreMoney(storeMoney);
        storeService.updateById(store);
        storeWithdrawalsService.save(storeWithdrawals);
        return new SBApi();
    }

    @PutMapping
    public SBApi updateStoreWithdrawals(@Valid @RequestBody StoreWithdrawals storeWithdrawals) {
        StoreWithdrawals oldWithdrawals = storeWithdrawalsService.getById(storeWithdrawals.getId());
        BigDecimal money = storeWithdrawals.getMoney().subtract(oldWithdrawals.getMoney());

        Store store = storeService.getById(storeWithdrawals.getStoreId());
        if (store.getStoreMoney().compareTo(money) < 0) {
            throw new ShopException(ResultEnum.STORE_WITHDRAWALS_MONEY);
        }
        // 20200916. 需求商店取款（新增申请取款记录就直接扣除商店余额）
        BigDecimal storeMoney = store.getStoreMoney().subtract(money);
        store.setStoreMoney(storeMoney);
        storeService.updateById(store);
        storeWithdrawalsService.updateById(storeWithdrawals);
        return new SBApi();
    }

    @DeleteMapping("{id}")
    public SBApi deleteStoreWithdrawals(@PathVariable("id") Integer id) {
        StoreWithdrawals oldWithdrawals = storeWithdrawalsService.getById(id);
        Store store = storeService.getById(oldWithdrawals.getStoreId());
        store.setStoreMoney(store.getStoreMoney().add(oldWithdrawals.getMoney()));
        // 还原店铺余额
        storeService.updateById(store);
        // 20200916. 需求商店取款（新增申请取款记录就直接扣除商店余额）
        storeWithdrawalsService.removeById(id);
        return new SBApi();
    }

    @PutMapping("status")
    public SBApi updateStoreWithdrawalsStatus(@RequestParam("ids") Set<Integer> ids,
                                              @RequestParam("status") Integer status) {
        storeWithdrawalsService.update(new UpdateWrapper<StoreWithdrawals>()
                .set("status", status)
                .set("check_time", System.currentTimeMillis() / 1000)
                .in("id", ids));
        return new SBApi();
    }

    @GetMapping("/export")
    public void export(@RequestParam(value = "ids", required = false) Set<Integer> ids,
                       @RequestParam(value = "store_id", required = false) Integer storeId,
                       @RequestParam(value = "start_time", required = false) Integer startTime,
                       @RequestParam(value = "end_time", required = false) Integer endTime,
                       @RequestParam(value = "status", required = false) Integer status,
                       @RequestParam(value = "bank_card", required = false) String bankCard,
                       @RequestParam(value = "realname", required = false) String realname,
                       HttpServletResponse response) {
        List<StoreWithdrawalsExcel> orderExcelList = storeWithdrawalsService.getStoreWithdrawalsExportData(ids,storeId,startTime,endTime,status,bankCard,realname);
        String fileName = "商家转款_" + TimeUtil.transForDateStr(System.currentTimeMillis() / 1000, "yyyy-MM-dd HH.mm.ss");
        ExcelUtil.writeExcel(response, orderExcelList, fileName, fileName, new StoreWithdrawalsExcel());
    }

    @GetMapping("count")
    public Integer getCount(@RequestParam(value = "status", required = false) Set<Integer> status) {
        QueryWrapper<StoreWithdrawals> wrapper = new QueryWrapper<>();
        if(status != null){
            wrapper.in("status", status);
        }
        return storeWithdrawalsService.count(wrapper);
    }

}
