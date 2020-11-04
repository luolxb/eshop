package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.*;
import com.soubao.dao.RebateLogMapper;
import com.soubao.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.vo.UserLowerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-10-28
 */
@Slf4j
@Service
public class RebateLogServiceImpl extends ServiceImpl<RebateLogMapper, RebateLog> implements RebateLogService {
    @Autowired
    private RebateLogMapper rebateLogMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private MallService mallService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DistributLevelService distributLevelService;

    @Override
    public IPage<UserLowerVo> selectLowerPage(Page page, Integer userId, Integer level) {
        return rebateLogMapper.selectLowerPage(page, userId, level);
    }

    @Override
    public IPage<RebateLog> getRebateOrderGoodsPage(Page<RebateLog> page, Integer userId, Set<Integer> status) {
        return rebateLogMapper.selectRebateOrderGoodsList(page, userId, status);
    }

    @Override
    public void withUserByUserLowerVo(List<UserLowerVo> records) {
        if (!records.isEmpty()) {
            Set<Integer> userIds = records.stream().map(UserLowerVo::getBuyUserId).collect(Collectors.toSet());
            Map<Integer, Long> userRegTimeMap = userService.usersByIds(userIds).stream().collect(Collectors.toMap(User::getUserId, User::getRegTime));
            records.forEach(userLowerVo -> {
                userLowerVo.setRegTime(userRegTimeMap.get(userLowerVo.getBuyUserId()));
            });
        }
    }

    @Override
    public void withUser(List<RebateLog> records) {
        if (!records.isEmpty()) {
            Set<Integer> userIds = records.stream().map(RebateLog::getUserId).collect(Collectors.toSet());
            Map<Integer, User> userMap = userService.usersByIds(userIds).stream().collect(Collectors.toMap(User::getUserId, user -> user));
            records.forEach(rebateLog -> {
                rebateLog.setUser(userMap.get(rebateLog.getUserId()));
            });
        }
    }

    @Override
    public void createRebateLog(List<Order> orders) {
        Map<Object, Object> distributionConfigMap = mallService.config();
        Object distributionSwitch = distributionConfigMap.getOrDefault("distribut_switch", null);
        log.debug("总平台分销配置:{}", distributionSwitch);
        if (distributionSwitch == null || Integer.parseInt((String) distributionSwitch) == 0) {
            return;
        }
        Set<Integer> storeIds = new HashSet<>();
        Set<Integer> userIds = new HashSet<>();
        for (Order order : orders) {
            storeIds.add(order.getStoreId());
            userIds.add(order.getUserId());
        }
        Map<Integer, StoreDistribut> storeDistributionMap = sellerService.storeDistributionsByStoreIds(storeIds).stream()
                .collect(Collectors.toMap(StoreDistribut::getStoreId, item -> item));
        List<User> userList = userService.usersByIds(userIds);
        Map<Integer, User> userMap = new HashMap<>();
        Set<Integer> userLeaderIds = new HashSet<>();
        for (User user : userList) {
            userLeaderIds.add(user.getFirstLeader());
            userLeaderIds.add(user.getSecondLeader());
            userLeaderIds.add(user.getThirdLeader());
            userMap.put(user.getUserId(), user);
        }
        if(userLeaderIds.size() == 1 && userLeaderIds.contains(0)){
            log.debug("下单用户没有分销人员");
            return;
        }
        Map<Integer, User> userLeaderMap = userService.usersByIds(userLeaderIds).stream().collect(Collectors.toMap(User::getUserId, item -> item));
        if (userLeaderMap.size() == 0) {
            log.debug("下单用户没有分销人员记录");
            return;
        }
        Map<Integer, DistributLevel> distributionLevelMap = distributLevelService.list().stream().collect(Collectors.toMap(DistributLevel::getLevelId, item -> item));
        MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
        Object setBy = distributionConfigMap.getOrDefault("distribut_distribut_set_by", null);
        List<RebateLog> rebateLogs = new ArrayList<>();
        List<Order> updateOrderList = new ArrayList<>();
        for (Order order : orders) {
            StoreDistribut distribution = new StoreDistribut();
            if (null != setBy && Integer.parseInt((String) setBy) == 1) {
                StoreDistribut storeDistribut = storeDistributionMap.getOrDefault(order.getStoreId(), null);
                if (null == storeDistribut || storeDistribut.getSwitchs() == 0) {
                    log.debug("id：" + order.getStoreId() + "店铺没有开启分销设置");
                    continue;
                }
                distribution = storeDistribut;
            } else {
                distribution.setFirstRate(Integer.parseInt((String) distributionConfigMap.get("distribut_first_rate")));
                distribution.setSecondRate(Integer.parseInt((String) distributionConfigMap.get("distribut_second_rate")));
                distribution.setThirdRate(Integer.parseInt((String) distributionConfigMap.get("distribut_third_rate")));
                distribution.setRegrade(Integer.parseInt((String) distributionConfigMap.get("distribut_regrade")));
            }
            BigDecimal commissionPrice = BigDecimal.ZERO;
            for (OrderGoods orderGoods : order.getOrderGoods()) {
                commissionPrice = commissionPrice.add(orderGoods.getDistribut().add(BigDecimal.valueOf(orderGoods.getGoodsNum())));
            }
            if (commissionPrice.compareTo(BigDecimal.ZERO) == 0) {
                log.debug("该订单商品分销总计等于0");
                continue;
            }
            User user = userMap.get(order.getUserId());
            if (user.getFirstLeader() > 0) {
                BigDecimal firstPrice = commissionPrice.multiply(BigDecimal.valueOf(distribution.getFirstRate() / 100));
                if (firstPrice.compareTo(new BigDecimal(String.valueOf(0.01))) > 0) {
                    User firstLeaderUser = userLeaderMap.get(user.getFirstLeader());
                    RebateLog rebateLog = new RebateLog();
                    rebateLog.setUserId(user.getFirstLeader());
                    rebateLog.setBuyUserId(user.getUserId());
                    rebateLog.setNickname(user.getNickname());
                    rebateLog.setOrderSn(order.getOrderSn());
                    rebateLog.setOrderId(order.getOrderId());
                    rebateLog.setGoodsPrice(order.getGoodsPrice());
                    rebateLog.setLevel(1);
                    rebateLog.setCreateTime(System.currentTimeMillis() / 1000);
                    rebateLog.setStoreId(order.getStoreId());
                    if (firstLeaderUser.getDistributLevel() > 0) {
                        rebateLog.setMoney(commissionPrice.multiply
                                (distributionLevelMap.get(firstLeaderUser.getDistributLevel()).getRate1())
                                .divide(BigDecimal.valueOf(100), mc));
                    } else {
                        rebateLog.setMoney(firstPrice);
                    }
                    rebateLogs.add(rebateLog);
                }
            }
            if (user.getSecondLeader() > 0 && distribution.getRegrade() > 0) {
                BigDecimal secondPrice = commissionPrice.multiply(BigDecimal.valueOf(distribution.getSecondRate() / 100));
                if (secondPrice.compareTo(new BigDecimal(String.valueOf(0.01))) > 0) {
                    User secondLeaderUser = userLeaderMap.get(user.getSecondLeader());
                    RebateLog rebateLog = new RebateLog();
                    rebateLog.setUserId(user.getSecondLeader());
                    rebateLog.setBuyUserId(user.getUserId());
                    rebateLog.setNickname(user.getNickname());
                    rebateLog.setOrderSn(order.getOrderSn());
                    rebateLog.setOrderId(order.getOrderId());
                    rebateLog.setGoodsPrice(order.getGoodsPrice());
                    rebateLog.setLevel(2);
                    rebateLog.setCreateTime(System.currentTimeMillis() / 1000);
                    rebateLog.setStoreId(order.getStoreId());
                    if (secondLeaderUser.getDistributLevel() > 0) {
                        rebateLog.setMoney(commissionPrice.multiply
                                (distributionLevelMap.get(secondLeaderUser.getDistributLevel()).getRate2())
                                .divide(BigDecimal.valueOf(100), mc));
                    } else {
                        rebateLog.setMoney(secondPrice);
                    }
                    rebateLogs.add(rebateLog);
                }
            }
            if (user.getThirdLeader() > 0 && distribution.getRegrade() > 1) {
                BigDecimal thirdPrice = commissionPrice.multiply(BigDecimal.valueOf(distribution.getThirdRate() / 100));
                if (thirdPrice.compareTo(new BigDecimal(String.valueOf(0.01))) > 0) {
                    User thirdLeaderUser = userLeaderMap.get(user.getThirdLeader());
                    RebateLog rebateLog = new RebateLog();
                    rebateLog.setUserId(user.getThirdLeader());
                    rebateLog.setBuyUserId(user.getUserId());
                    rebateLog.setNickname(user.getNickname());
                    rebateLog.setOrderSn(order.getOrderSn());
                    rebateLog.setOrderId(order.getOrderId());
                    rebateLog.setGoodsPrice(order.getGoodsPrice());
                    rebateLog.setLevel(3);
                    rebateLog.setCreateTime(System.currentTimeMillis() / 1000);
                    rebateLog.setStoreId(order.getStoreId());
                    if (thirdLeaderUser.getDistributLevel() > 0) {
                        rebateLog.setMoney(commissionPrice.multiply
                                (distributionLevelMap.get(thirdLeaderUser.getDistributLevel()).getRate3())
                                .divide(BigDecimal.valueOf(100), mc));
                    } else {
                        rebateLog.setMoney(thirdPrice);
                    }
                    rebateLogs.add(rebateLog);
                }
            }
            Order updateOrder = new Order();
            updateOrder.setOrderId(order.getOrderId());
            updateOrder.setDistrict(1);
            updateOrderList.add(updateOrder);//修改订单为已经分成
        }
        if (rebateLogs.size() > 0) {
            saveBatch(rebateLogs);
        }
        if (updateOrderList.size() > 0) {
            orderService.updateBatchById(updateOrderList);
        }
        //todo 微信推送消息
    }
}
