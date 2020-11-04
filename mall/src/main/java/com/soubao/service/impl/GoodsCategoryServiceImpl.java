package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.Goods;
import com.soubao.entity.GoodsCategory;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.GoodsCategoryService;
import com.soubao.service.GoodsService;
import com.soubao.dao.GoodsCategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-13
 */
@Slf4j
@Service("goodsCategoryService")
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategory> implements GoodsCategoryService {
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;
    @Autowired
    private GoodsService goodsService;

    @Override
    public List<GoodsCategory> getSecAndThirdCategoryListByFirstId(Integer id) {
        List<GoodsCategory> exGoodsCategoryList = goodsCategoryMapper.selectSecAndThirdCategoryListByFirstId(id);
        List<GoodsCategory> goodsCategoryList = new ArrayList<>();
        Map<Integer, Integer> secIdIndexMap = new HashMap<>();//二级id，下标
        int secIdIndexStart = 0;
        for (GoodsCategory goodsCategory : exGoodsCategoryList) {
            if (goodsCategory.getLevel().equals(2)) {
                goodsCategory.setChildren(new ArrayList<>());
                goodsCategoryList.add(goodsCategory);
                secIdIndexMap.put(goodsCategory.getId(), secIdIndexStart);
                secIdIndexStart++;
            }
            if (goodsCategory.getLevel().equals(3)) {
                break;
            }
        }
        for (int currentLastIndex = (exGoodsCategoryList.size() - 1); currentLastIndex > 0; currentLastIndex--) {
            if (exGoodsCategoryList.get(currentLastIndex).getLevel().equals(3)) {
                Integer parentId = exGoodsCategoryList.get(currentLastIndex).getParentId();
                if (secIdIndexMap.containsKey(parentId)) {
                    int goodsCategoryParentIndex = secIdIndexMap.get(parentId);
                    goodsCategoryList.get(goodsCategoryParentIndex).getChildren().add(exGoodsCategoryList.get(currentLastIndex));
                }
            }
            if (exGoodsCategoryList.get(currentLastIndex).getLevel().equals(2)) {
                break;
            }
        }
        return goodsCategoryList;
    }

    @Override
    public GoodsCategory getGoodsCategoryByGoodsListFilterUrl(String url) {
        UriComponentsBuilder parentSb = ServletUriComponentsBuilder.fromUriString(url);
        List<String> catIdQueryList = parentSb.build().getQueryParams().get("cat_id");
        GoodsCategory goodsCategory = null;
        if (catIdQueryList != null && catIdQueryList.get(0).length() > 0) {
            goodsCategory = getById(catIdQueryList.get(0));
        }
        return goodsCategory;
    }

    @Override
    public List<GoodsCategory> listToTree(List<GoodsCategory> list) {
        //用递归找子。
        List<GoodsCategory> treeList = new ArrayList<>();
        for (GoodsCategory tree : list) {
            if (tree.getParentId() == 0) {
                treeList.add(findChildren(tree, list));
            }
        }
        return treeList;
    }

    @Override
    public List<GoodsCategory> getGoodsCategoryWithGoodsType() {
        return goodsCategoryMapper.selectGoodsCategoryWithGoodsType(0);
    }

    @Override
    public List<GoodsCategory> getGoodsCategoryWithGoodsType(Integer parentId) {
        return goodsCategoryMapper.selectGoodsCategoryWithGoodsType(parentId);
    }

    @Override
    public List<GoodsCategory> getCategoryWithBrand() {
        return goodsCategoryMapper.selectCategoryWithBrand();
    }

    @Override
    public void deleteById(Integer id) {
        int childCount = count((new QueryWrapper<GoodsCategory>()).eq("parent_id", id));
        if (childCount > 0) {
            throw new ShopException(ResultEnum.CAT_HAVE_CHILD);
        }
        int goodsUseCount = goodsService.count((new QueryWrapper<Goods>()).eq("cat_id1", id).
                or().eq("cat_id2", id).or().eq("cat_id3", id));
        if (goodsUseCount > 0) {
            throw new ShopException(ResultEnum.CAT_HAVE_GOODS);
        }
        removeById(id);
    }

    @Override
    public void updateGoodsCategory(GoodsCategory goodsCategory) {
        if (goodsCategory.getParentId().equals(goodsCategory.getId())) {
            throw new ShopException(ResultEnum.PARENT_CANNOT_BE_HIMSELF);
        }
        Set<Integer> pidSet = new HashSet<>();
        pidSet.add(goodsCategory.getId());
        if (goodsCategory.getParentId() > 0) {
            //当前分类所有子级id集合
            Set<Integer> childrenIds = new HashSet<>();
            //获取当前分类到最小的子级的层级数与所有子级id集合
            Integer currHierarchy = findMinChildHierarchy(pidSet, 1, childrenIds);
            //过滤-把分类设定为其子级的子级操作
            if (childrenIds.contains(goodsCategory.getParentId())) {
                throw new ShopException(ResultEnum.NOT_SET_TO_CHILDREN);
            }
            Integer parentLevel = getById(goodsCategory.getParentId()).getLevel();
            //编辑上级分类时校验分类层级是否超过三级
            if (currHierarchy + parentLevel > 3) {
                throw new ShopException(ResultEnum.GOODS_CATEGORY_LEVEL_ERROR);
            }
        }
        updateById(goodsCategory);
        //更改当前分类下所有下级的level与parent_id_path
        changeChildLevelAndPath(pidSet, goodsCategory.getLevel());
    }

    private void changeChildLevelAndPath(Set<Integer> parentIdSet, Integer parentLevel) {
        List<GoodsCategory> goodsCategoryList = list(new QueryWrapper<GoodsCategory>().in("parent_id", parentIdSet));
        if (!goodsCategoryList.isEmpty()) {
            Integer currLevel = parentLevel + 1;
            goodsCategoryList.forEach(goodsCategory -> {
                goodsCategory.setLevel(currLevel);
                String parentIdPath = getById(goodsCategory.getParentId()).getParentIdPath();
                goodsCategory.setParentIdPath(parentIdPath + "_" + goodsCategory.getId());
            });
            updateBatchById(goodsCategoryList);
            Set<Integer> pidSetTemp = goodsCategoryList.stream().map(GoodsCategory::getId).collect(Collectors.toSet());
            changeChildLevelAndPath(pidSetTemp, currLevel);
        }
    }

    private Integer findMinChildHierarchy(Set<Integer> parentIdSet, Integer hierarchy, Set<Integer> childrenIds) {
        List<GoodsCategory> goodsCategoryList = list(new QueryWrapper<GoodsCategory>().in("parent_id", parentIdSet));
        if (goodsCategoryList.isEmpty()) {
            return hierarchy;
        }
        Set<Integer> parentIdSetTemp = goodsCategoryList.stream().map(GoodsCategory::getId).collect(Collectors.toSet());
        childrenIds.addAll(parentIdSetTemp);
        return findMinChildHierarchy(parentIdSetTemp, ++hierarchy, childrenIds);
    }

    private GoodsCategory findChildren(GoodsCategory tree, List<GoodsCategory> list) {
        for (GoodsCategory node : list) {
            if (node.getParentId().equals(tree.getId())) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<>());
                }
                tree.getChildren().add(findChildren(node, list));
            }
        }
        if(tree.getLevel() < 3){
            //一二级没有子节点就设置没有
            tree.setNoChildren(null == tree.getChildren());
        }
        return tree;
    }
}
