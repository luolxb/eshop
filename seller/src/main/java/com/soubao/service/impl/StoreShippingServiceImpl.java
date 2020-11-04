package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.StoreShippingMapper;
import com.soubao.entity.Shipping;
import com.soubao.entity.StoreShipping;
import com.soubao.service.ShippingService;
import com.soubao.service.StoreShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 店铺快递公司表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-12-10
 */
@Service
public class StoreShippingServiceImpl extends ServiceImpl<StoreShippingMapper, StoreShipping> implements StoreShippingService {

}
