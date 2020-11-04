package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.dao.ComplainSubjectMapper;
import com.soubao.entity.ComplainSubject;
import com.soubao.service.ComplainSubjectService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 投诉主题表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-02-28
 */
@Service
public class ComplainSubjectServiceImpl extends ServiceImpl<ComplainSubjectMapper, ComplainSubject> implements ComplainSubjectService {

    @Override
    public void saveComplainSubject(ComplainSubject complainSubject) {
        int count = count(new QueryWrapper<ComplainSubject>().eq("subject_name", complainSubject.getSubjectName()));
        if (count > 0) {
            throw new ShopException(ResultEnum.SUBJECT_NAME_EXISTS);
        }
        complainSubject.setSubjectState(1);
        save(complainSubject);
    }
}
