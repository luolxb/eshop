package com.soubao.service.impl;

import com.soubao.entity.SpecType;
import com.soubao.service.SpecTypeService;
import com.soubao.dao.SpecTypeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品类型与规格对应表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-11-21
 */
@Service
public class SpecTypeServiceImpl extends ServiceImpl<SpecTypeMapper, SpecType> implements SpecTypeService {

}
