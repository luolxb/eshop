package com.soubao.service;

import com.soubao.entity.Help;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 帮助内容表 服务类
 * </p>
 *
 * @author dyr
 * @since 2020-03-04
 */
public interface HelpService extends IService<Help> {

    void withHelpType(List<Help> helpList);

    void removeHelp(Integer helpId);

    void saveHelp(Help help);

    void updateHelp(Help help);
}
