package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.ComplainSubject;

/**
 * <p>
 * 投诉主题表 服务类
 * </p>
 *
 * @author dyr
 * @since 2020-02-28
 */
public interface ComplainSubjectService extends IService<ComplainSubject> {

    //添加投诉主题
    void saveComplainSubject(ComplainSubject complainSubject);
}
