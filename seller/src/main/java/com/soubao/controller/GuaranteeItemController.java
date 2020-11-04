package com.soubao.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.GuaranteeItem;
import com.soubao.service.GuaranteeItemService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 消费者保障服务项目表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-03-11
 */
@RestController
@RequestMapping("/guarantee_item")
public class GuaranteeItemController {
    @Autowired
    private GuaranteeItemService guaranteeItemService;

    @GetMapping("page")
    public IPage<GuaranteeItem> page(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return guaranteeItemService.page(new Page<>(page, size));
    }

    @GetMapping("list/store_guarantee_info")
    public List<GuaranteeItem> listStoreGuaranteeInfo(@RequestParam("store_id") Integer storeId) {
        return guaranteeItemService.listStoreGuaranteeInfo(storeId);
    }

    @GetMapping
    public GuaranteeItem getOne(@RequestParam("grt_id") Integer grtId) {
        return guaranteeItemService.getById(grtId);
    }

    @PostMapping
    public SBApi save(@Valid @RequestBody GuaranteeItem guaranteeItem) {
        guaranteeItemService.save(guaranteeItem);
        return new SBApi();
    }

    @PutMapping
    public SBApi update(@Valid @RequestBody GuaranteeItem guaranteeItem) {
        guaranteeItemService.updateById(guaranteeItem);
        return new SBApi();
    }

    @DeleteMapping
    public SBApi remove(@RequestParam("grt_id") Integer grtId) {
        guaranteeItemService.removeById(grtId);
        return new SBApi();
    }

}
