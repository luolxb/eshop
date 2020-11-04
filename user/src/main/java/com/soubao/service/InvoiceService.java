package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Invoice;
import com.soubao.vo.InvoiceExcel;

import java.util.List;

/**
 * <p>
 * 发票信息表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-11-22
 */
public interface InvoiceService extends IService<Invoice> {
    //获取发票导出数据
    List<InvoiceExcel> getInvoiceExportData(QueryWrapper<Invoice> wrapper);

    void withSource(List<Invoice> records);

    void withUserAndStore(List<InvoiceExcel> invoiceExcelList);
}
