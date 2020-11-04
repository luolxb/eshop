package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Guarantee;
import com.soubao.entity.GuaranteeItem;
import com.soubao.dto.GuaranteeApplyDto;
import com.soubao.service.GuaranteeItemService;
import com.soubao.service.GuaranteeService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 店铺消费者保障服务加入情况表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-03-11
 */
@RestController
@RequestMapping("/guarantee")
public class GuaranteeController {
    @Autowired
    private GuaranteeService guaranteeService;
    @Autowired
    private GuaranteeItemService guaranteeItemService;

    @GetMapping("page")
    public IPage<Guarantee> page(@RequestParam(value = "store_name", required = false) String storeName,
                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Guarantee> guaranteeQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(storeName)) {
            guaranteeQueryWrapper.like("store_name", storeName);
        }
        IPage<Guarantee> guaranteeIPage = guaranteeService.page(new Page<>(page, size), guaranteeQueryWrapper);
        guaranteeService.withGuaranteeItem(guaranteeIPage.getRecords());
        return guaranteeIPage;
    }

    @GetMapping
    public Guarantee getOne(@RequestParam("id") Integer id) {
        Guarantee guarantee = guaranteeService.getById(id);
        GuaranteeItem guaranteeItem = guaranteeItemService.getById(guarantee.getGrtId());
        guarantee.setGrtName(guaranteeItem.getGrtName());
        return guarantee;
    }

    @GetMapping("store_guarantee")
    public Guarantee getOne(@RequestParam("store_id") Integer storeId,
                            @RequestParam("grt_id") Integer grtId) {
        return guaranteeService.getOne(new QueryWrapper<Guarantee>().eq("store_id", storeId).eq("grt_id", grtId));
    }

    @PostMapping
    public SBApi apply(@RequestBody GuaranteeApplyDto guaranteeApplyDto) {
        guaranteeService.apply(guaranteeApplyDto);
        return new SBApi();
    }


    @PutMapping("isopen")
    public SBApi updateIsOpen(@RequestBody Guarantee guarantee,
                              @RequestParam(value = "log_msg", required = false) String logMsg) {
        guaranteeService.updateIsOpen(guarantee, logMsg);
        return new SBApi();
    }

}
