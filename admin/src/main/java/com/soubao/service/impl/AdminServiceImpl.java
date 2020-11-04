package com.soubao.service.impl;

import com.soubao.entity.Admin;
import com.soubao.dao.AdminMapper;
import com.soubao.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-11-04
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
