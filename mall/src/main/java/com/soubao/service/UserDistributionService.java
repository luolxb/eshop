package com.soubao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Goods;
import com.soubao.entity.User;
import com.soubao.entity.UserDistribution;
import com.soubao.vo.UserDistributionGoodsVo;

import java.util.List;

/**
 * <p>
 * 用户选择分销商品表 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-09-10
 */
public interface UserDistributionService extends IService<UserDistribution> {
    //用户批量添加分销商品
    void addDistributionGoodsList(User user, List<Goods> goodsList);

    IPage<UserDistributionGoodsVo> selectGoodsPage(Page page, UserDistribution userDistribution);
}
