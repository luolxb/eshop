package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Order;
import com.soubao.entity.Region;

import java.util.List;

public interface RegionService extends IService<Region> {
    //根据id查找父级地区
    List<Region> selectParentListById(Integer id);

    /**
     * 补充订单地址
     * @param records
     */
    void inOrder(List<Order> records);

    //删除地区及子孙地区
    boolean removeAddress(Integer id);

    List<Region> listToTree(List<Region> list);
}
