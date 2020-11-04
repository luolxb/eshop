package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Invoice;
import com.soubao.entity.Seller;
import com.soubao.service.InvoiceService;
import com.soubao.common.utils.TimeUtil;
import com.soubao.utils.excel.ExcelUtil;
import com.soubao.vo.InvoiceExcel;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/export")
    public void exportInvoice(@RequestParam(value = "invoice_ids", required = false) Set<Integer> invoiceIds,
                              @RequestParam(value = "ctime_begin", required = false) Long ctimeBegin,
                              @RequestParam(value = "ctime_end", required = false) Long ctimeEnd,
                              @RequestParam(value = "status", required = false) Integer status,
                              HttpServletResponse response) {
        QueryWrapper<Invoice> wrapper = new QueryWrapper<>();
        if (invoiceIds != null && !invoiceIds.isEmpty()) {
            wrapper.in("invoice_id", invoiceIds);
        } else {
            if (ctimeBegin != null) {
                wrapper.ge("ctime", ctimeBegin);
            }
            if (ctimeEnd != null) {
                wrapper.lt("ctime", ctimeEnd);
            }
            if (status != null) {
                wrapper.eq("status", status);
            }
        }
        wrapper.apply("1=1");
        wrapper.orderByAsc("invoice_id");
        List<InvoiceExcel> invoiceExcelList = invoiceService.getInvoiceExportData(wrapper);
        invoiceService.withUserAndStore(invoiceExcelList);
        String fileName = "发票_" + TimeUtil.transForDateStr(System.currentTimeMillis() / 1000, "yyyy-MM-dd HH.mm.ss");
        ExcelUtil.writeExcel(response, invoiceExcelList, fileName, fileName, new InvoiceExcel());
    }

    @PreAuthorize("hasAnyRole('SELLER, ADMIN')")
    @GetMapping("/page")
    public IPage<Invoice> getUser(@RequestParam(value = "begin_time", required = false) Long beginTime,
                                  @RequestParam(value = "end_time", required = false) Long endTime,
                                  @RequestParam(value = "status", required = false) Integer status,
                                  @RequestParam(value = "order_sn", required = false) String orderSn,
                                  @RequestParam(value = "page", defaultValue = "1") Integer page,
                                  @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QueryWrapper<Invoice> invoiceQueryWrapper = new QueryWrapper<>();
        if (beginTime != null) {
            invoiceQueryWrapper.ge("ctime", beginTime);
        }
        if (endTime != null) {
            invoiceQueryWrapper.le("ctime", endTime);
        }
        if (status != null) {
            invoiceQueryWrapper.eq("status", status);
        }
        if (!StringUtils.isEmpty(orderSn)) {
            invoiceQueryWrapper.eq("order_sn", orderSn);
        }
        if (obj instanceof Seller) {
            invoiceQueryWrapper.eq("store_id", ((Seller) obj).getStoreId());
        }
        invoiceQueryWrapper.orderByDesc("invoice_id");
        IPage<Invoice> invoicePage = invoiceService.page(new Page<>(page, size), invoiceQueryWrapper);
        invoiceService.withSource(invoicePage.getRecords());
        return invoicePage;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/statistic")
    public Map<String, Integer> getInvoiceStatistic() {
        int waitCount = invoiceService.count(new QueryWrapper<Invoice>().eq("status", 0));
        int totalCount = invoiceService.count(new QueryWrapper<Invoice>().eq("status", 1));
        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("waitCount", waitCount);
        resultMap.put("totalCount", totalCount);
        return resultMap;
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @PutMapping
    public SBApi update(@RequestBody Invoice invoice) {
        if (invoice.getStatus() == 1) {
            invoice.setAtime(System.currentTimeMillis() / 1000);
        } else if (invoice.getStatus() == 0) {
            invoice.setAtime(0L);
        }
        invoiceService.updateById(invoice);
        return new SBApi();
    }
}
