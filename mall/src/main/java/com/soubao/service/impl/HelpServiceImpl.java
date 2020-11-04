package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.Help;
import com.soubao.entity.HelpType;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.HelpService;
import com.soubao.service.HelpTypeService;
import com.soubao.dao.HelpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 帮助内容表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-03-04
 */
@Service
public class HelpServiceImpl extends ServiceImpl<HelpMapper, Help> implements HelpService {
    @Autowired
    private HelpTypeService helpTypeService;

    @Override
    public void withHelpType(List<Help> helpList) {
        Set<Integer> typeIds = helpList.stream().map(Help::getTypeId).collect(Collectors.toSet());
        if (!typeIds.isEmpty()) {
            Map<Integer, HelpType> helpTypeMap = helpTypeService.listByIds(typeIds).stream().collect(Collectors.toMap(HelpType::getTypeId, helpType -> helpType));
            for (Help help : helpList) {
                help.setHelpType(helpTypeMap.get(help.getTypeId()));
            }
        }
    }

    @Override
    public void removeHelp(Integer helpId) {
        if (helpId < 11) {
            throw new ShopException(ResultEnum.CANNOT_REMOVE_SYSTEM_CAT);
        }
        this.removeById(helpId);
    }

    @Override
    public void saveHelp(Help help) {
        if (this.count(new QueryWrapper<Help>().eq("help_title", help.getHelpTitle())) > 0) {
            throw new ShopException(ResultEnum.NAME_EXISTS);
        }
        help.setAddTime(System.currentTimeMillis() / 1000);
        this.save(help);
    }

    @Override
    public void updateHelp(Help help) {
        if (this.count(new QueryWrapper<Help>().ne("help_id", help.getHelpId()).eq("help_title", help.getHelpTitle())) > 0) {
            throw new ShopException(ResultEnum.NAME_EXISTS);
        }
        this.updateById(help);
    }

}

