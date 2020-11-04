package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.PromOrder;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.PromOrderService;
import com.soubao.dao.PromOrderMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-11-07
 */
@Service
public class PromOrderServiceImpl extends ServiceImpl<PromOrderMapper, PromOrder> implements PromOrderService {

    @Override
    public void closeProm(Integer promId) {
        PromOrder promOrder = getById(promId);
        if (promOrder == null) {
            throw new ShopException(ResultEnum.ACTIVITY_NOT_EXISTS);
        }
        update(new UpdateWrapper<PromOrder>().set("status", 0).eq("id", promId));
    }

}
