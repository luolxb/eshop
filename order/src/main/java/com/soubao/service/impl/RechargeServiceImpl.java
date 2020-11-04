package com.soubao.service.impl;

import com.soubao.entity.Recharge;
import com.soubao.dao.RechargeMapper;
import com.soubao.service.RechargeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-02-27
 */
@Service("rechargeService")
public class RechargeServiceImpl extends ServiceImpl<RechargeMapper, Recharge> implements RechargeService {

}
