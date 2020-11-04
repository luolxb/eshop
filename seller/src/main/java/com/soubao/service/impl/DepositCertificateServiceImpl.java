package com.soubao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.DepositCertificateMapper;
import com.soubao.dao.SellerMapper;
import com.soubao.entity.DepositCertificate;
import com.soubao.entity.Seller;
import com.soubao.service.DepositCertificateService;
import com.soubao.service.SellerService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 卖家用户表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-11-14
 */
@Service("DepositCertificateService")
public class DepositCertificateServiceImpl extends ServiceImpl<DepositCertificateMapper, DepositCertificate> implements DepositCertificateService {
}

