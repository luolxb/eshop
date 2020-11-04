package com.soubao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.OrderGoods;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderGoodsService")
public interface OrderGoodsService extends IService<OrderGoods> {


}
