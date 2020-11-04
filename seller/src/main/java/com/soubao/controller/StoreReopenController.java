package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Store;
import com.soubao.entity.StoreReopen;
import com.soubao.service.StoreGradeService;
import com.soubao.service.StoreReopenService;
import com.soubao.service.StoreService;
import com.soubao.common.utils.TimeUtil;
import com.soubao.common.vo.SBApi;
import com.soubao.vo.StoreReopenInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/store_reopen")
public class StoreReopenController {
    @Autowired
    private StoreReopenService storeReopenService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreGradeService storeGradeService;

    @GetMapping("page")
    public IPage<StoreReopen> page(@RequestParam(value = "store_id", required = false) Integer storeId,
                                   @RequestParam(value = "seller_name", required = false) String sellerName,
                                   @RequestParam(value = "store_name", required = false) String storeName,
                                   @RequestParam(value = "sc_id", required = false) Integer scId,
                                   @RequestParam(value = "grade_id", required = false) Integer gradeId,
                                   @RequestParam(value = "p", defaultValue = "1") Integer page,
                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<StoreReopen> storeReopenQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Store> storeQueryWrapper = new QueryWrapper<>();
        if (storeId != null) {
            storeReopenQueryWrapper.eq("re_store_id", storeId);
        }
        if (!StringUtils.isEmpty(sellerName)) {
            storeQueryWrapper.like("seller_name", sellerName);
        }
        if (!StringUtils.isEmpty(storeName)) {
            storeReopenQueryWrapper.like("re_store_name", storeName);
        }
        if (scId != null) {
            storeQueryWrapper.eq("sc_id", scId);
        }
        if (gradeId != null) {
            storeReopenQueryWrapper.eq("re_grade_id", gradeId);
        }
        storeQueryWrapper.select("store_id");
        Set<Integer> storeIds = storeService.list(storeQueryWrapper).stream().map(Store::getStoreId).collect(Collectors.toSet());
        storeReopenQueryWrapper.in("re_store_id", storeIds);
        storeReopenQueryWrapper.orderByDesc("re_id");
        return storeReopenService.page(new Page<>(page, size), storeReopenQueryWrapper);
    }

    @GetMapping
    public StoreReopen getOne(@RequestParam("re_id") Integer reId) {
        return storeReopenService.getById(reId);
    }

    @PostMapping
    public SBApi save(@Valid @RequestBody StoreReopen storeReopen){
        storeReopenService.saveStoreReopen(storeReopen);
        return new SBApi();
    }

    @PutMapping("re_state")
    public boolean checkReopen(@RequestBody StoreReopen storeReopen) {
        if (storeReopen.getReState() == 2) {
            storeService.update(new UpdateWrapper<Store>()
                    .set("store_end_time", storeReopen.getReEndTime())
                    .set("grade_id", storeReopen.getReGradeId())
                    .eq("store_id", storeReopen.getReStoreId()));
        }
        return storeReopenService.update(new UpdateWrapper<StoreReopen>()
                .set("re_state", storeReopen.getReState())
                .set("admin_note", storeReopen.getAdminNote())
                .eq("re_id", storeReopen.getReId()));
    }

    @DeleteMapping
    public boolean remove(@RequestParam("re_id") Integer reId) {
        return storeReopenService.removeById(reId);
    }

    @GetMapping("info")
    public StoreReopenInfoVO storeReopenInfo(@RequestParam("store_id") Integer storeId) {
        StoreReopenInfoVO storeReopenInfoVO = new StoreReopenInfoVO();
        Store store = storeService.getById(storeId);
        storeReopenInfoVO.setReEndTime(store.getStoreEndTimeDesc());
        long continueTime = store.getStoreEndTime() - (30 * 60 * 60 * 24);
        storeReopenInfoVO.setStartApplyTime(TimeUtil.transForDateStr(continueTime, "yyyy-MM-dd"));
        int count = storeReopenService.count(new QueryWrapper<StoreReopen>().eq("re_store_id", storeId).notIn("re_state", -1, 2));
        if (continueTime < System.currentTimeMillis() / 1000 && count < 1) {
            storeReopenInfoVO.setApplyStatus(true);
        } else {
            storeReopenInfoVO.setApplyStatus(false);
        }
        storeReopenInfoVO.setStoreGrade(storeGradeService.getById(store.getGradeId()));
        return storeReopenInfoVO;
    }

    @GetMapping("/count")
    public Integer getCount(@RequestParam(value = "re_state", required = false) Integer reState) {
        QueryWrapper<StoreReopen> wrapper = new QueryWrapper<>();
        if(reState != null){
            wrapper.eq("re_state", reState);
        }
        return storeReopenService.count(wrapper);
    }

}
