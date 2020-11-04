package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.GoodsCategory;
import com.soubao.entity.Order;
import com.soubao.entity.Region;
import com.soubao.service.RegionService;
import com.soubao.dao.RegionMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("regionService")
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {
    @Override
    public List<Region> selectParentListById(Integer id) {
        return selectParentListByIdRecursion(id, new ArrayList<>());
    }

    @Override
    public void inOrder(List<Order> records) {
        if(records.size() == 0){
            return;
        }
        Set<Integer> regionIds = new HashSet<>();
        for (Order order : records){
            regionIds.add(order.getProvince());
            regionIds.add(order.getCity());
            regionIds.add(order.getDistrict());
            regionIds.add(order.getTwon());
        }
        List<Region> regions = this.list((new QueryWrapper<Region>()).select("id,name").in("id", regionIds).orderByAsc("level"));
        Map<Integer, Region> regionMap = regions.stream().collect(Collectors.toMap(Region::getId, region -> region));
        List<Region> tempRegionList;
        for(Order order : records){
            tempRegionList = new ArrayList<>();
            tempRegionList.add(regionMap.get(order.getProvince()));
            tempRegionList.add(regionMap.get(order.getCity()));
            tempRegionList.add(regionMap.get(order.getDistrict()));
            if(order.getTwon() > 0){
                tempRegionList.add(regionMap.get(order.getTwon()));
            }
            order.setRegions(tempRegionList);
        }
    }

    @Override
    public boolean removeAddress(Integer id) {
        Set<Integer> childIds = new HashSet<>();
        this.getRegionChildIds(id, childIds);
        childIds.add(id);
        return this.removeByIds(childIds);
    }

    @Override
    public List<Region> listToTree(List<Region> list) {
        //用递归找子。
        List<Region> treeList = new ArrayList<>();
        for (Region tree : list) {
            if (tree.getParentId() == 0) {
                treeList.add(findChildren(tree, list));
            }
        }
        return treeList;
    }

    private Region findChildren(Region tree, List<Region> list) {
        for (Region node : list) {
            if (node.getParentId().equals(tree.getId())) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<>());
                }
                tree.getChildren().add(findChildren(node, list));
            }
        }
        return tree;
    }

    //获取指定地区所有子孙地区id集合
    private void getRegionChildIds(Integer id, Set<Integer> childIds){
        List<Region> regions = this.list(new QueryWrapper<Region>().eq("parent_id", id));
        for (Region region: regions) {
            childIds.add(region.getId());
            this.getRegionChildIds(region.getId(), childIds);
        }
    }

    private List<Region> selectParentListByIdRecursion(Integer id, List<Region> regionList) {
        Region region = this.getById(id);
        if(region != null){
            regionList.add(region);
            if (region.getParentId() > 0) {
                return selectParentListByIdRecursion(region.getParentId(), regionList);
            }
        }
        return regionList;
    }

}
