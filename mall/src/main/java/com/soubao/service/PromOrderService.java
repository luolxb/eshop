package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.PromOrder;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-11-07
 */
public interface PromOrderService extends IService<PromOrder> {

    //关闭订单促销
    void closeProm(Integer promId);
}
