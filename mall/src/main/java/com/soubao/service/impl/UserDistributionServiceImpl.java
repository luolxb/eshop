package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.UserDistributionMapper;
import com.soubao.entity.Goods;
import com.soubao.entity.User;
import com.soubao.entity.UserDistribution;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.UserDistributionService;
import com.soubao.vo.UserDistributionGoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户选择分销商品表 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-09-10
 */
@Service
public class UserDistributionServiceImpl extends ServiceImpl<UserDistributionMapper, UserDistribution> implements UserDistributionService {
    @Autowired
    private UserDistributionMapper userDistributionMapper;

    @Override
    public void addDistributionGoodsList(User user, List<Goods> goodsList){
        if (user.getIsDistribut() != 1) {
            throw new ShopException(ResultEnum.NOT_DISTRIBUTORS);
        }
        Long addTime = System.currentTimeMillis() / 1000;
        List<UserDistribution> userDistributionList = new ArrayList<>();
        for (Goods g : goodsList) {
            UserDistribution ud = new UserDistribution();
            ud.setUserId(user.getUserId()).setUserName(user.getNickname()).setAddtime(addTime).setGoodsId(g.getGoodsId())
                    .setGoodsName(g.getGoodsName()).setCatId(g.getCatId3()).setBrandId(g.getBrandId()).setStoreId(g.getStoreId());
            userDistributionList.add(ud);
        }
        saveBatch(userDistributionList);
    }

    @Override
    public IPage<UserDistributionGoodsVo> selectGoodsPage(Page page, UserDistribution userDistribution) {
        return userDistributionMapper.selectGoodsPage(page, userDistribution);
    }

}
