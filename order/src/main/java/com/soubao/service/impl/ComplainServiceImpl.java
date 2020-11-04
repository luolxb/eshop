package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.Complain;
import com.soubao.dao.ComplainMapper;
import com.soubao.service.ComplainService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 投诉表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-02-28
 */
@Service
public class ComplainServiceImpl extends ServiceImpl<ComplainMapper, Complain> implements ComplainService {

    @Autowired
    private ComplainMapper complainMapper;

    @Override
    public IPage<Complain> getComplainPage(Page<Complain> page, QueryWrapper<Complain> queryWrapper) {
        return complainMapper.getComplainPage(page,queryWrapper);
    }
}
