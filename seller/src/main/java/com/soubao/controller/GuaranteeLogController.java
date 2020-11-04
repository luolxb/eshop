package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.GuaranteeLog;
import com.soubao.service.GuaranteeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 店铺消费者保障服务日志表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-03-11
 */
@RestController
@RequestMapping("/guarantee_log")
public class GuaranteeLogController {
    @Autowired
    private GuaranteeLogService guaranteeLogService;

    @GetMapping("page")
    public IPage<GuaranteeLog> page(@RequestParam("log_storeid") Integer logStoreId,
                                    @RequestParam("log_grtid") Integer logGrtId,
                                    @RequestParam(value = "page", defaultValue = "1") Integer page,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return guaranteeLogService.page(new Page<>(page, size), new QueryWrapper<GuaranteeLog>().eq("log_storeid", logStoreId)
                .eq("log_grtid", logGrtId).orderByDesc("log_id"));
    }
}
