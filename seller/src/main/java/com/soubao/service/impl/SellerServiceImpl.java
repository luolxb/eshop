package com.soubao.service.impl;

import com.soubao.dao.SellerMapper;
import com.soubao.entity.Seller;
import com.soubao.service.SellerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 卖家用户表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-11-14
 */
@Service("sellerService")
public class SellerServiceImpl extends ServiceImpl<SellerMapper, Seller> implements SellerService {
}
