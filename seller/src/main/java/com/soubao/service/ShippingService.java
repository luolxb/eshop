package com.soubao.service;

import com.soubao.entity.Shipping;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyr
 * @since 2019-12-10
 */
@Service
public interface ShippingService extends IService<Shipping> {

    List<Shipping> shippingPrint(Set<Integer> ids);
}
