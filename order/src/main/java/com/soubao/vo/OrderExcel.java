package com.soubao.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class OrderExcel extends BaseRowModel {
    @ExcelProperty(value = {"订单编号"}, index = 0)
    private String orderSn;

    @ExcelProperty(value = {"日期"}, index = 1)
    private String createTime;

    @ExcelProperty(value = {"收货人"}, index = 2)
    private String consignee;

    @ExcelProperty(value = {"收货地址"}, index = 3)
    private String shippingAddress;

    private Integer province;
    private Integer city;
    private Integer district;
    private Integer twon;
    private String address;

    @ExcelProperty(value = {"电话"}, index = 4)
    private String mobile;

    @ExcelProperty(value = {"订单金额"}, index = 5)
    private String goodsPrice;

    @ExcelProperty(value = {"应付金额"}, index = 6)
    private String orderAmount;

    @ExcelProperty(value = {"支付方式"}, index = 7)
    private String payName;

    private Integer payStatus;

    @ExcelProperty(value = {"支付状态"}, index = 8)
    private String payStatusDesc;

    public String getPayStatusDesc() {
        if (payStatus != null) {
            switch (payStatus) {
                case 0:
                    return "待支付";
                case 1:
                    return "已支付";
                case 2:
                    return "部分支付";
                case 3:
                    return "已退款";
                case 4:
                    return "拒绝退款";
                default:
                    return "";
            }
        }
        return payStatusDesc;
    }

    private Integer shippingStatus;

    @ExcelProperty(value = {"发货状态"}, index = 9)
    private String shippingStatusDesc;

    public String getShippingStatusDesc() {
        if (shippingStatus != null) {
            switch (shippingStatus){
                case 0:
                    return "未发货";
                case 1:
                    return "已发货";
                case 2:
                    return "部分发货";
                default:
                    return "";
            }
        }

        return shippingStatusDesc;
    }

    @ExcelProperty(value = {"商品数量"}, index = 10)
    private Integer goodsNum;

    @ExcelProperty(value = {"商品信息"}, index = 11)
    private String goodsInfo;
}
