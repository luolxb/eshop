package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.InvoiceMapper;
import com.soubao.entity.Invoice;
import com.soubao.entity.Order;
import com.soubao.entity.Store;
import com.soubao.entity.User;
import com.soubao.service.InvoiceService;
import com.soubao.service.OrderService;
import com.soubao.service.SellerService;
import com.soubao.service.UserService;
import com.soubao.vo.InvoiceExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 发票信息表 服务实现类
 *
 * @author dyr
 * @since 2019-11-22
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice>
        implements InvoiceService {
    @Autowired
    private InvoiceMapper invoiceMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private SellerService sellerService;
    @Override
    public List<InvoiceExcel> getInvoiceExportData(QueryWrapper<Invoice> wrapper) {
        return invoiceMapper.selectInvoiceExportData(wrapper);
    }

    @Override
    public void withSource(List<Invoice> records) {
        Set<Integer> userIds = records.stream().map(Invoice::getUserId).collect(Collectors.toSet());
        Set<Integer> storeIds = records.stream().map(Invoice::getStoreId).collect(Collectors.toSet());

        if (!userIds.isEmpty()) {
            Map<Integer, User> userMap = userService.listByIds(userIds).stream().collect(Collectors.toMap(User::getUserId, user -> user));
            records.forEach(invoice -> {
                if (userMap.containsKey(invoice.getUserId())) {
                    invoice.setNickname(userMap.get(invoice.getUserId()).getNickname());
                }
            });
        }
        if (!storeIds.isEmpty()) {
            Map<Integer, Store> storeMap = sellerService.getStoresById(storeIds).stream().collect(Collectors.toMap(Store::getStoreId, store -> store));
            records.forEach(
                    invoice -> {
                        if (storeMap.containsKey(invoice.getStoreId())) {
                            invoice.setStoreName(storeMap.get(invoice.getStoreId()).getStoreName());
                        }
                    });
        }
    }

    @Override
    public void withUserAndStore(List<InvoiceExcel> invoiceExcelList) {
        Set<Integer> userIds = invoiceExcelList.stream().map(InvoiceExcel::getUserId).collect(Collectors.toSet());
        Set<Integer> storeIds = invoiceExcelList.stream().map(InvoiceExcel::getStoreId).collect(Collectors.toSet());

        if (!userIds.isEmpty()) {
            Map<Integer, User> userMap = userService.listByIds(userIds).stream().collect(Collectors.toMap(User::getUserId, user -> user));
            invoiceExcelList.forEach(invoiceExcel -> {
                if (userMap.containsKey(invoiceExcel.getUserId())) {
                    invoiceExcel.setNickname(userMap.get(invoiceExcel.getUserId()).getNickname());
                }
            });
        }
        if (!storeIds.isEmpty()) {
            Map<Integer, Store> storeMap = sellerService.getStoresById(storeIds).stream().collect(Collectors.toMap(Store::getStoreId, store -> store));
            invoiceExcelList.forEach(invoiceExcel -> {
                if (storeMap.containsKey(invoiceExcel.getStoreId())) {
                    invoiceExcel.setStoreName(storeMap.get(invoiceExcel.getStoreId()).getStoreName());
                }
            });
        }
    }

}
