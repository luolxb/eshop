package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.GoodsConsult;
import com.soubao.entity.User;
import com.soubao.service.GoodsConsultService;
import com.soubao.dao.GoodsConsultMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-11-27
 */
@Service("goodsConsultService")
public class GoodsConsultServiceImpl extends ServiceImpl<GoodsConsultMapper, GoodsConsult> implements GoodsConsultService {
    @Autowired
    private GoodsConsultMapper goodsConsultMapper;

    @Autowired
    private UserService userService;

    @Override
    public IPage<GoodsConsult> selectConsultPage(Page<GoodsConsult> page, QueryWrapper<GoodsConsult> queryWrapper) {
        return goodsConsultMapper.selectConsultPage(page, queryWrapper);
    }

    @Override
    public void withUser(List<GoodsConsult> records) {
        if(records.size() == 0){
            return;
        }
        Set<Integer> userIds = records.stream().map(GoodsConsult::getUserId).collect(Collectors.toSet());
        List<User> users = userService.listByIds(userIds);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(User::getUserId, user -> user));
        for(GoodsConsult goodsConsult : records){
            goodsConsult.setUser(userMap.get(goodsConsult.getUserId()));
        }
    }

    @Override
    public IPage<GoodsConsult> userConsultPage(Page<GoodsConsult> goodsConsultPage, User user) {
        return goodsConsultMapper.selectUserConsultPage(goodsConsultPage, user);
    }

    @Override
    public void withChildren(List<GoodsConsult> records) {
        if (records.size() == 0) {
            return;
        }
        Set<Integer> ids = records.stream().map(GoodsConsult::getId).collect(Collectors.toSet());
        List<GoodsConsult> goodsConsults = this.list(new QueryWrapper<GoodsConsult>().in("parent_id", ids));
        Map<Integer, List<GoodsConsult>> consultMap = goodsConsults
                .stream()
                .collect(Collectors.groupingBy(GoodsConsult::getParentId));
        for (GoodsConsult goodsConsult : records) {
            if (consultMap.containsKey(goodsConsult.getId())) {
                goodsConsult.setChildrenConsults(consultMap.get(goodsConsult.getId()));
            }
        }
    }
}
