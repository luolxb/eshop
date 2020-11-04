package com.soubao.service.impl;

import com.soubao.entity.BrandType;
import com.soubao.service.BrandTypeService;
import com.soubao.dao.BrandTypeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品类型与品牌对应表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-11-22
 */
@Service
public class BrandTypeServiceImpl extends ServiceImpl<BrandTypeMapper, BrandType> implements BrandTypeService {

}
