package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.dao.StoreGoodsClassMapper;
import com.soubao.entity.StoreGoodsClass;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.StoreGoodsClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 店铺商品分类表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-12-11
 */
@Service
public class StoreGoodsClassServiceImpl extends ServiceImpl<StoreGoodsClassMapper, StoreGoodsClass> implements StoreGoodsClassService {

    @Override
    public List<StoreGoodsClass> listToTree(List<StoreGoodsClass> list) {
        //用递归找子。
        List<StoreGoodsClass> treeList = new ArrayList<>();
        for (StoreGoodsClass tree : list) {
            if (tree.getParentId() == 0) {
                treeList.add(findChildren(tree, list));
            }
        }
        return treeList;
    }

    @Override
    public void updateStoreGoodsClass(StoreGoodsClass storeGoodsClass) {
        //上级分类不能时自身
        if (storeGoodsClass.getCatId().equals(storeGoodsClass.getParentId())){
            throw new ShopException(ResultEnum.PARENT_CANNOT_BE_HIMSELF);
        }
        //分类层级不能超过两级
        if (storeGoodsClass.getParentId() > 0){
            int childredCount = this.count(new QueryWrapper<StoreGoodsClass>().eq("parent_id", storeGoodsClass.getCatId()));
            if (childredCount > 0){
                throw new ShopException(ResultEnum.STORE_GOODS_CLASS_LEVEL_ERROR);
            }
        }
        this.updateById(storeGoodsClass);
    }

    private static StoreGoodsClass findChildren(StoreGoodsClass tree, List<StoreGoodsClass> list) {
        for (StoreGoodsClass node : list) {
            if (node.getParentId().equals(tree.getCatId())) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<>());
                }
//                tree.getChildren().add(findChildren(node, list));
                tree.getChildren().add(node);   //该分类为两级结构,直接添加
            }
        }
        return tree;
    }
}
