package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.HelpType;

import java.util.List;

/**
 * <p>
 * 帮助类型表 服务类
 * </p>
 *
 * @author dyr
 * @since 2020-03-04
 */
public interface HelpTypeService extends IService<HelpType> {

    //获取树状结构文章分类
    List<HelpType> listToTree(List<HelpType> list);

    //添加帮助类型
    boolean saveHelpType(HelpType helpType);

    //更新帮助类型
    boolean updateHelpType(HelpType helpType);

    //删除帮助类型
    void removeHelpType(Integer typeId);
}
