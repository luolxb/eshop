package com.soubao.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.soubao.entity.*;

import com.soubao.dao.OrderMapper;
import com.soubao.service.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;


import java.math.BigDecimal;


@Slf4j
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {


    @Override
    public Integer getPayStatusByPay(Order order) {
        int payStatus = -1;
        if (order.getPromType() == 4) {
            if (order.getPayStatus() == -1) {
                if (order.getPaidMoney().compareTo(BigDecimal.ZERO) > 0) {
                    //付订金
                    payStatus = 4;
                } else {
                    //全额
                    payStatus =3;
                }
            }
            if (order.getPayStatus() == 4) {
                //付尾款
                payStatus = 3;
            }
        } else {
            payStatus =2;
        }
        return payStatus;
    }

}