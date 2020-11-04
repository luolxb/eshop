package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.Help;
import com.soubao.entity.HelpType;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.HelpService;
import com.soubao.service.HelpTypeService;
import com.soubao.dao.HelpTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 帮助类型表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-03-04
 */
@Service
public class HelpTypeServiceImpl extends ServiceImpl<HelpTypeMapper, HelpType> implements HelpTypeService {
    @Autowired
    private HelpService helpService;

    @Override
    public List<HelpType> listToTree(List<HelpType> list) {
        List<HelpType> treeList = new ArrayList<>();
        for (HelpType tree : list) {
            if (tree.getPid() == 0){
                treeList.add(findChildren(tree, list));
            }
        }
        return treeList;
    }

    @Override
    public boolean saveHelpType(HelpType helpType) {
        //重名校验
        if (count(new QueryWrapper<HelpType>().eq("type_name", helpType.getTypeName())) > 0){
            throw new ShopException(ResultEnum.NAME_EXISTS);
        }
        if (helpType.getPid() > 0){
            HelpType parentHelpType = getById(helpType.getPid());
            helpType.setLevel(parentHelpType.getLevel() + 1);
        }
        return save(helpType);
    }

    @Override
    public boolean updateHelpType(HelpType helpType) {
        //重名校验
        if (count(new QueryWrapper<HelpType>()
                .ne("type_id", helpType.getTypeId())
                .eq("type_name", helpType.getTypeName())) > 0){
            throw new ShopException(ResultEnum.NAME_EXISTS);
        }
        if (helpType.getPid() > 0){
            //当顶级分类下存在下级分类时，无法更改为其他顶级分类的下级（帮助分类只能存在两级）
            if (count(new QueryWrapper<HelpType>().eq("pid", helpType.getTypeId())) > 0){
                throw new ShopException(ResultEnum.HELP_TYPE_LEVEL_ERROR);
            }
            HelpType parentHelpType = getById(helpType.getPid());
            helpType.setLevel(parentHelpType.getLevel() + 1);
        }else{
            helpType.setLevel(0);
        }
        if (Objects.equals(helpType.getPid(), helpType.getTypeId())){
            throw new ShopException(ResultEnum.PARENT_CANNOT_BE_HIMSELF);
        }
        return updateById(helpType);
    }

    @Override
    public void removeHelpType(Integer typeId) {
        if (typeId < 11){
            throw new ShopException(ResultEnum.CANNOT_REMOVE_SYSTEM_CAT);
        }
        if (helpService.count(new QueryWrapper<Help>().eq("type_id", typeId)) > 0){
            throw new ShopException(ResultEnum.HELP_TYPE_HAVE_HELP);
        }
        if (count(new QueryWrapper<HelpType>().eq("pid", typeId)) > 0){
            throw new ShopException((ResultEnum.HELP_TYPE_HAVE_CHILD));
        }
        removeById(typeId);
    }

    private HelpType findChildren(HelpType tree, List<HelpType> list) {
        for (HelpType node : list) {
            if (node.getPid().equals(tree.getTypeId())) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<>());
                }
                tree.getChildren().add(findChildren(node, list));
            }
        }
        return tree;
    }
}
