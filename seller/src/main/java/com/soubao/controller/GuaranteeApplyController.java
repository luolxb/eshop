package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.GuaranteeApply;
import com.soubao.service.GuaranteeApplyService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 消费者保障服务申请表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-03-11
 */
@RestController
@RequestMapping("/guarantee_apply")
public class GuaranteeApplyController {
    @Autowired
    private GuaranteeApplyService guaranteeApplyService;

    @GetMapping("page")
    public IPage<GuaranteeApply> page(@RequestParam("apply_type") Integer applyType,
                                      @RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<GuaranteeApply> guaranteeApplyIPage = guaranteeApplyService.page(new Page<>(page, size),
                new QueryWrapper<GuaranteeApply>().eq("apply_type", applyType));
        guaranteeApplyService.withGuaranteeItem(guaranteeApplyIPage.getRecords());
        return guaranteeApplyIPage;
    }

    @GetMapping("store/guarantee_apply")
    public GuaranteeApply getOne(@RequestParam("store_id") Integer storeId, @RequestParam("grt_id") Integer grtId) {
        return guaranteeApplyService.getOne(new QueryWrapper<GuaranteeApply>()
                .eq("store_id", storeId).eq("grt_id", grtId).orderByDesc("id"), false);
    }

    @PostMapping("cost_pay")
    public SBApi costPay(@RequestParam("grt_id") Integer grtId,
                         @RequestParam("store_id") Integer storeId,
                         @RequestParam("costimg") String costimg,
                         @RequestParam("seller_id") Integer sellerId) {
        guaranteeApplyService.costPay(grtId, storeId, costimg, sellerId);
        return new SBApi();
    }


    @PutMapping("auditstate")
    public SBApi updateAuditstate(@RequestBody GuaranteeApply guaranteeApply,
                                  @RequestParam(value = "log_msg", required = false) String logMsg) {
        guaranteeApplyService.updateAuditstate(guaranteeApply, logMsg);
        return new SBApi();
    }

}
