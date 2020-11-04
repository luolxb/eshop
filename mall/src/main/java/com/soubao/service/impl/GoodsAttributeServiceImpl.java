package com.soubao.service.impl;

import com.soubao.entity.GoodsAttribute;
import com.soubao.service.GoodsAttributeService;
import com.soubao.dao.GoodsAttributeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-15
 */
@Service("goodsAttributeService")
public class GoodsAttributeServiceImpl extends ServiceImpl<GoodsAttributeMapper, GoodsAttribute> implements GoodsAttributeService {

}
